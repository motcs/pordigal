package com.prodigal.business.politics.comment;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.prodigal.security.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
@Data
@Entity
@Table(name = "network_politics_comment")
@Schema(title = "网络议政-留言评论")
public class Comment implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Schema(title = "主键")
    private Long id;

    @Schema(title = "回复的父级ID默认为0")
    private Long pid;

    @Schema(title = "租户uuid")
    private String tenantUuid;

    @Schema(title = "所属ID")
    private Long dataId;

    /**
     * 1.议政评论
     * 2.代表会客厅
     * 3.代表专访
     * 4.理论研究
     * 5.远程协商
     * 10.新代表圈
     * 100 时，为代表圈记录发布此时所属ID可以为空
     */
    @Schema(title = "类型")
    private String type;

    @Schema(title = "发起人用户uuid")
    private String creatorUserUuid;

    @Schema(title = "答复人用户uuid")
    private String replyUserUuid;

    @Schema(title = "点赞数量")
    private Long thumbNumber;

    @Schema(title = "评论内容")
    private String content;

    @Schema(title = "预留字段")
    private ObjectNode extend;

    @Schema(title = "创建时间")
    private LocalDateTime createdTime;

    @Schema(title = "修改时间")
    private LocalDateTime updatedTime;

}