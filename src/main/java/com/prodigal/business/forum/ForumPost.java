package com.prodigal.business.forum;

import com.prodigal.security.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-11 星期二
 */
@Data
@Entity
@Schema(title = "帖子")
@Table(name = "user_forum_post")
public class ForumPost implements Serializable, BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(title = "标题")
    private String title;

    @Schema(title = "内容")
    private String content;

    @Schema(title = "用户账户")
    private String username;

    @Schema(title = "用户姓名")
    private String usernames;

    @Schema(title = "创建时间")
    private LocalDateTime createdTime;

    @Schema(title = "修改时间")
    private LocalDateTime updatedTime;

}
