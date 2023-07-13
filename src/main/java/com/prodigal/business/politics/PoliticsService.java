package com.prodigal.business.politics;

import com.prodigal.annotation.RestServerException;
import com.prodigal.business.politics.comment.CommentService;
import com.prodigal.commons.BaseAutoToolsUtil;
import com.prodigal.commons.SqlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.prodigal.commons.MonthControl.LocalDateTimeToSecond;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2022/4/7
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class PoliticsService extends BaseAutoToolsUtil {


    private final CommentService commentService;
    private final PoliticsRepository politicsRepository;

    public Page<Politics> page(PoliticsRequest request, Pageable pageable) {
        List<Politics> query = super.jdbcTemplate.query(request.whereSql() +
                SqlUtils.applyPage(pageable), DataClassRowMapper.newInstance(Politics.class, conversionService));
        for (Politics politics : query) {
            //如果创建人的用户uuid=租户uuid则这个是管理员
            setPoliticsKeyOverdue(politics, false);
        }
        Long count = super.jdbcTemplate.queryForObject(request.countSql(), Long.class);
        return new PageImpl<>(query, pageable, count == null ? 0L : count);
    }

    public List<Politics> search(PoliticsRequest request) {
        List<Politics> query = super.jdbcTemplate.query(request.whereSql(),
                DataClassRowMapper.newInstance(Politics.class, conversionService));
        for (Politics politics : query) {
            //如果创建人的用户uuid=租户uuid则这个是管理员
            setPoliticsKeyOverdue(politics, false);
        }
        return query;
    }

    public List<Politics> comment(String repUuid) {
        List<Politics> query = new ArrayList<>();
        //查询用户的评论次数
        String sql = "select distinct data_id as id from  network_politics_comment where type = 1 and (reply_user_uuid='"
                + repUuid + "' or creator_user_uuid='" + repUuid + "')";
        List<Politics> politicsList = super.jdbcTemplate.query(sql, DataClassRowMapper.newInstance(Politics.class, conversionService));
        for (Politics politics : politicsList) {
            Optional<Politics> byId = this.politicsRepository.findById(politics.getId());
            if (byId.isPresent()) {
                String sqlComment = "select count(*) from network_politics_comment where type = '1' and data_id = " + politics.getId();
                Politics politics1 = byId.get();
                Long count = super.jdbcTemplate.queryForObject(sqlComment, Long.class);
                politics1.setCommentNumber(String.valueOf(count));
                query.add(politics1);
            }
            setPoliticsKeyOverdue(politics, false);
        }
        return query;
    }

    public Politics operation(PoliticsRequest request) {
        return this.save(request.toPolitics());
    }

    public void deleteBatch(List<Long> ids) {
        ids.forEach(this::delete);
    }

    public void delete(Long id) {
        Optional<Politics> byId = this.politicsRepository.findById(id);
        if (byId.isPresent()) {
            Politics politics = byId.get();
            if (politics.getStatus().equals("1")) {
                throw RestServerException.withMsg(500, "ID:" + politics.getId() + "的议政信息,正在进行中,不可以删除!");
            }
            this.politicsRepository.delete(politics);
            super.executor.execute(() -> {
                //删除评论，根据数据id以及评论类型，防止删错
                this.commentService.deleteByTypeAndDataId("1", id);
            });
        }
    }

    /**
     * 更改记录
     * <p>意见提出人: 胡祥梅</p>
     * <p>意见时间: 2022-04-21 18:06:48</p>
     * <p>意见内容:</p>
     * <p>网络议政-添加议题-议题状态为开始前，可以进行修改，删除。</p>
     * <p>议题进行中，不可以修改，删除。</p>
     * <p>结束的议题不可以在修改，只能删除</p>
     *
     * @param politics 需要操作的数据
     * @author <a href="https://github.com/motcs">motcs</a>
     * @since 2022-04-22 11:25:11
     */
    private Politics save(Politics politics) {
        //设置修改时间
        politics.setUpdatedTime(LocalDateTime.now());
        if (politics.isNew()) {
            //设置为未开始
            politics.setStatus("0");
            politics.setCreatedTime(LocalDateTime.now());
            Politics save = this.politicsRepository.save(politics);
            //设置数据的过期时间
            return this.setPoliticsKeyOverdue(save, true);
        } else {
            assert !ObjectUtils.isEmpty(politics.getId());
            Optional<Politics> byId = this.politicsRepository.findById(politics.getId());
            if (byId.isPresent()) {
                Politics old = byId.get();
                if (!old.getStatus().equals("0")) {
                    throw RestServerException.withMsg(500, "ID:" + politics.getId() + "的议政信息"
                            + (old.getStatus().equals("1") ? "正在进行中" : "已结束") + "不能进行修改!");
                }
                politics.setCreator(old.getCreator());
                politics.setCreatedTime(old.getCreatedTime());
                //设置数据的过期时间
                return this.setPoliticsKeyOverdue(politics, true);
            } else {
                throw RestServerException.withMsg(500, "没有找到ID为:" + politics.getId() + "的议政信息,无法进行修改,请重试!");
            }
        }
    }

    /**
     * 设置议政信息数据的过期时间
     *
     * @param politics 议政信息
     */
    public Politics setPoliticsKeyOverdue(Politics politics, boolean isSave) {
        long second = LocalDateTimeToSecond(politics.getStartTime());
        //如果开始时间减去当前时间大于0，则代表时间没到，此时设置redis过期时间
        if (second > 0 && !politics.isNew() &&
                !Objects.equals(politics.getStatus(), "0")) {
            politics.setStatus("0");
        }
        if (second <= 0) {
            long secondEnd = LocalDateTimeToSecond(politics.getEndTime());
            //如果新添加时过期时间到了，则直接判断结束时间是否已过
            if (secondEnd > 0) {
                //没有结束直接设置为进行中
                politics.setStatus("1");
            } else {
                //否则直接设置为结束
                politics.setStatus("2");
            }
        }
        if (isSave) {
            if (politics.isNew()) {
                return this.politicsRepository.saveAndFlush(politics);
            } else {
                return this.politicsRepository.save(politics);
            }
        }
        return politics;
    }
}