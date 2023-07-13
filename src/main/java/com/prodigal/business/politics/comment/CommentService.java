package com.prodigal.business.politics.comment;

import com.prodigal.annotation.RestServerException;
import com.prodigal.business.politics.comment.collection.CommentCollectionRepository;
import com.prodigal.business.politics.comment.thumb.CommentThumbService;
import com.prodigal.commons.BaseAutoToolsUtil;
import com.prodigal.commons.SqlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2022/4/8
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class CommentService extends BaseAutoToolsUtil {

    private final CommentRepository commentRepository;
    private final CommentThumbService commentThumbService;
    private final CommentCollectionRepository commentCollectionRepository;

    public Page<CommentRequest> page(CommentRequest request, Pageable pageable) {
        List<CommentRequest> query = search(request, pageable);
        Long count = super.jdbcTemplate.queryForObject(request.countSql(true), Long.class);
        return new PageImpl<>(query, pageable, count == null ? 0L : count);
    }

    /**
     * 一级评论
     *
     * @param request  参数
     * @param pageable 分页
     * @return 一级评论
     */
    public List<CommentRequest> search(CommentRequest request, Pageable pageable) {
        List<CommentRequest> query = super.jdbcTemplate.query(request.whereSql(true)
                + SqlUtils.applyPage(pageable), DataClassRowMapper.newInstance(CommentRequest.class, conversionService));
        return getUserInfo(request.getThumbUserUuid(), query);
    }

    /**
     * 查询评论一级评论根据分页信息，二级评论5条
     *
     * @param request  参数
     * @param pageable 分页
     * @return 返回评论
     */
    public Page<CommentRequest> pageOnly(CommentRequest request, Pageable pageable) {
        //设置pid默认为0L
        request.setPid(0L);
        //查询一级评论
        List<CommentRequest> query = search(request, pageable);
        //查询一级评论下的子评论
        for (CommentRequest commentRequest : query) {
            CommentRequest bind = new CommentRequest().bind(commentRequest);
            List<CommentRequest> commentRequests = searchOnly(bind);
            Long count = super.jdbcTemplate.queryForObject(bind.countSql(false), Long.class);
            commentRequest.setSubordinate(commentRequests);
            commentRequest.setCommentNumber(count);
        }
        Long count = super.jdbcTemplate.queryForObject(request.countSql(true), Long.class);
        return new PageImpl<>(query, pageable, count == null ? 0L : count);
    }

    /**
     * 获取数据对应的评论总数
     *
     * @param request 请求参数
     * @return 返回个数
     */
    public Long commentNumber(CommentRequest request) {
        return super.jdbcTemplate.queryForObject(request.countSql(true), Long.class);
    }

    /**
     * 查询二级评论
     *
     * @param request 参数
     * @return 返回二级评论
     */
    public List<CommentRequest> searchOnly(CommentRequest request) {
        String sql = " ORDER BY updated_time desc limit 0,5";
        List<CommentRequest> query = super.jdbcTemplate.query(request.whereSql(false) + sql,
                DataClassRowMapper.newInstance(CommentRequest.class, conversionService));
        return getUserInfo(request.getThumbUserUuid(), query);
    }

    /**
     * 设置评论用户信息
     *
     * @param thumbUserUuid 当前登陆人的用户uuid
     * @param query         查询到的结果
     */
    private List<CommentRequest> getUserInfo(String thumbUserUuid, List<CommentRequest> query) {
        for (CommentRequest commentRequest : query) {
            commentRequest.setThumbUserUuid(thumbUserUuid);
        }
        return query;
    }

    public CommentRequest operation(CommentRequest request) {
        //待解决 敏感词检测
        return this.save(request.toPoliticsComment());
    }

    public void deleteBatch(List<Long> ids) {
        ids.forEach(this::delete);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void delete(Long id) {
        //删除所有点赞
        this.commentThumbService.delete(id);
        //删除所有子评论
        this.commentRepository.deleteByPid(id);
        //删除评论
        this.commentRepository.deleteById(id);
        //删除收藏
        this.commentCollectionRepository.deleteAllByCommentId(id);
    }

    /**
     * 评论删除根据type以及dataId
     *
     * @param type   类型
     * @param dataId 数据ID
     */
    @Transactional(rollbackOn = {Exception.class})
    public void deleteByTypeAndDataId(String type, Long dataId) {
        List<Comment> allByTypeAndDataId = this.commentRepository.findAllByTypeAndDataId(type, dataId);
        if (allByTypeAndDataId.size() > 0) {
            for (Comment comment : allByTypeAndDataId) {
                this.delete(comment.getId());
            }
        }
    }

    private CommentRequest save(Comment comment) {
        if (!Objects.equals(comment.getType(), "100") && ObjectUtils.isEmpty(comment.getDataId())) {
            throw RestServerException.withMsg(500, "评论时，必须带上所属ID!");
        }
        //设置修改时间
        comment.setUpdatedTime(LocalDateTime.now());
        if (comment.isNew()) {
            comment.setCreatedTime(LocalDateTime.now());
            //保存评论信息
            Comment save = this.commentRepository.save(comment);
            CommentRequest request = new CommentRequest();
            BeanUtils.copyProperties(save, request);
            //设置创建人信息
            return request;
        } else {
            assert !ObjectUtils.isEmpty(comment.getId());
            Optional<Comment> byId = this.commentRepository.findById(comment.getId());
            if (byId.isPresent()) {
                Comment old = byId.get();
                comment.setCreatedTime(old.getCreatedTime());
                //保存评论信息
                Comment save = this.commentRepository.save(comment);
                CommentRequest request = new CommentRequest();
                BeanUtils.copyProperties(save, request);
                return request;
            } else {
                throw RestServerException.withMsg(500, "id:" + comment.getId() + "的评论信息,无法进行修改,请重试!");
            }
        }
    }

}