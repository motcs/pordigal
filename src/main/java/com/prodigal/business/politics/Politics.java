package com.prodigal.business.politics;

import com.prodigal.security.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
@Data
@Entity
@Table(name = "network_politics_subject")
@Schema(title = "网络议政主体")
public class Politics implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "主键")
    private Long id;

    @Schema(title = "租户uuid")
    private String tenantUuid;

    @Schema(title = "标题")
    private String title;

    @Schema(title = "内容")
    private String content;

    /**
     * 0.未开始
     * 1.进行中
     * 2.已结束
     */
    @Schema(title = "状态")
    private String status;

    @Schema(title = "参与类型，1.公众 2.指定参与人")
    private Integer type;

    @Schema(title = "参与人的代表uuid集合，逗号分割")
    private String participantUuid;

    @Schema(title = "参与人的代表姓名集合，逗号分割")
    private String participantName;

    @Transient
    @Schema(title = "评论次数")
    private String commentNumber;

    @Transient
    @Schema(title = "年度")
    private Integer year;

    @Transient
    @Schema(title = "评论人数")
    private Integer personNumber;

    @Schema(title = "开始时间")
    private LocalDateTime startTime;

    @Schema(title = "结束时间")
    private LocalDateTime endTime;

    @Schema(title = "创建人用户uuid")
    private String creator;

    @Schema(title = "修改人用户uuid")
    private String updater;

    @Schema(title = "创建人用户uuid")
    private String creatorName;

    @Schema(title = "修改人用户uuid")
    private String updaterName;

    @Schema(title = "创建时间")
    private LocalDateTime createdTime;

    @Schema(title = "修改时间")
    private LocalDateTime updatedTime;

}
