package com.prodigal.business.politics;

import com.prodigal.commons.MonthControl;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
@Data
public class PoliticsRequest implements Serializable {

    @Schema(title = "主键")
    private Long id;

    @NotNull(message = "租户UUID[tenantUuid]不可为空")
    @Schema(title = "租户uuid")
    private String tenantUuid;

    @NotNull(message = "标题[title]不可为空")
    @Schema(title = "标题")
    private String title;

    @NotNull(message = "内容[content]不可为空")
    @Schema(title = "内容")
    private String content;

    @NotNull(message = "状态[status]不可为空")
    @Schema(title = "状态")
    private String status;

    @NotNull(message = "参与类型[type]不可为空")
    @Schema(title = "参与类型，1.公众 2.指定参与人")
    private Integer type;

    @Schema(title = "参与人的代表uuid集合，逗号分割")
    private String participantUuid;

    @Schema(title = "参与人的代表姓名集合，逗号分割")
    private String participantName;

    @NotNull(message = "开始时间[startTime]不可为空")
    @Schema(title = "开始时间")
    private LocalDateTime startTime;

    @NotNull(message = "endTime[结束时间]不可为空")
    @Schema(title = "结束时间")
    private LocalDateTime endTime;

    @Schema(title = "创建人代表uuid")
    private String creator;

    @Schema(title = "修改人代表uuid")
    private String updater;

    /**
     * 查询公众议政以及自身参与的议政
     */
    @Schema(title = "代表UUID")
    private String repUuid;

    private Integer year;
    private Integer month;

    public Politics toPolitics() {
        Politics politics = new Politics();
        BeanUtils.copyProperties(this, politics);
        return politics;
    }

    public String whereSql() {
        StringBuilder sql = new StringBuilder("select *,(select count(*) from network_politics_comment")
                .append(" where network_politics_comment.data_id = network_politics_subject.id")
                .append(" and network_politics_comment.type = '1') as comment_number")
                .append(" from network_politics_subject where id > 0");
        return getWhereSql(sql);
    }

    public String countSql() {
        StringBuilder sql = new StringBuilder("select count(*) from network_politics_subject where id > 0");
        return getWhereSql(sql);
    }

    @NotNull
    private String getWhereSql(StringBuilder sql) {
        if (!ObjectUtils.isEmpty(this.id)) {
            sql.append(" and id = ").append(this.id);
        }
        if (!ObjectUtils.isEmpty(this.type)) {
            sql.append(" and type = ").append(this.type);
        }
        if (StringUtils.hasLength(this.tenantUuid)) {
            sql.append(" and tenant_uuid = '").append(this.tenantUuid).append("'");
        }
        if (StringUtils.hasLength(this.title)) {
            sql.append(" and title like '%").append(this.title.replace("%", "\\%")).append("%'");
        }
        if (StringUtils.hasLength(this.participantUuid)) {
            sql.append(" and participant_uuid like '%").append(this.participantUuid).append("%'");
        }
        if (StringUtils.hasLength(this.status)) {
            sql.append(" and status = '").append(this.status).append("'");
        }
        if (StringUtils.hasLength(this.creator)) {
            sql.append(" and creator = '").append(this.creator).append("'");
        }
        if (StringUtils.hasLength(this.updater)) {
            sql.append(" and updater = '").append(this.updater).append("'");
        }
        if (StringUtils.hasLength(this.repUuid)) {
            sql.append(" and (participant_uuid like '%").append(this.repUuid)
                    .append("%' or type = 1 or creator ='").append(this.repUuid).append("')");
        }
        if (!ObjectUtils.isEmpty(this.year)) {
            sql.append(" created_time between '").append(MonthControl.startDate(this.year, this.month))
                    .append("' and '").append(MonthControl.endDate(this.year, this.month)).append("'");
        }
        return sql.toString();
    }

}
