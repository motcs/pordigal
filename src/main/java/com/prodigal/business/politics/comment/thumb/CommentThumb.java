package com.prodigal.business.politics.comment.thumb;

import com.prodigal.security.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
@Data
@Entity
@Schema(title = "网络议政-留言评论点赞")
@Table(name = "network_politics_comment_thumb")
public class CommentThumb implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Schema(title = "主键")
    private Long id;

    @Schema(title = "主题id")
    private Long dataId;

    @NotNull(message = "议政主题uuid[commentId]不可为空")
    @Schema(title = "评论ID")
    private Long commentId;

    @NotNull(message = "代表用户uuid[userUuid]不可为空")
    @Schema(title = "代表用户uuid")
    private String userUuid;

    @Schema(title = "是否点赞")
    private Boolean thumb;

    @CreatedDate
    @Schema(title = "创建时间")
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Schema(title = "修改时间")
    private LocalDateTime updatedTime;

}