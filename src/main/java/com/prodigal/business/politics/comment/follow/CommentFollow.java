package com.prodigal.business.politics.comment.follow;

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
@Table(name = "network_politics_comment_follow")
@Schema(title = "网络议政-留言关注代表")
public class CommentFollow implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Schema(title = "主键")
    private Long id;

    @Schema(title = "用户ID")
    private String userId;

    @Schema(title = "关注的用户ID")
    private String followUserId;

    @Schema(title = "预留字段")
    private ObjectNode extend;

    @Schema(title = "创建时间")
    private LocalDateTime createdTime;

    @Schema(title = "修改时间")
    private LocalDateTime updatedTime;

}