package com.prodigal.business.politics.comment;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2022/4/8
 */
@Data
public class CommentRequest implements Serializable {

    @Schema(title = "主键")
    private Long id;

    @Schema(title = "回复的父级ID默认为0")
    private Long pid;

    /**
     * 1.议政评论
     * 2.代表会客厅
     * 3.代表专访
     * 4.理论研究
     * 5.远程协商
     * 100 时，为代表圈记录发布此时所属ID可以为空
     */
    @Schema(title = "评论类型")
    private String type;

    @Schema(title = "租户uuid")
    private String tenantUuid;

    @Schema(title = "所属id")
    private Long dataId;

    @Schema(title = "发起人用户uuid")
    private String creatorUserUuid;

    @Schema(title = "发起人用户姓名")
    private String creatorUserName;

    @Schema(title = "发起人用户头像")
    private String creatorImgUrl;

    @Schema(title = "发起人用户电话")
    private String creatorPhone;

    @Schema(title = "答复人用户uuid")
    private String replyUserUuid;

    @Schema(title = "答复人用户姓名")
    private String replyUserName;

    @Schema(title = "发起人用户头像")
    private String replyImgUrl;

    @Schema(title = "发起人用户头像")
    private String replyPhone;

    @Schema(title = "点赞情况用户uuid")
    private String thumbUserUuid;

    @Schema(title = "是否点赞")
    private Boolean thumb;

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

    @Schema(title = "二级评论数量")
    private Long commentNumber;

    @Schema(title = "是否关注")
    private Boolean isFollow;

    @Schema(title = "是否收藏")
    private Boolean isCollection;

    @Schema(title = "代表姓名")
    private String name;

    @Schema(title = "二级评论")
    private List<CommentRequest> subordinate = new ArrayList<>();

    public Comment toPoliticsComment() {
        Comment comment = new Comment();
        BeanUtils.copyProperties(this, comment);
        return comment;
    }

    public CommentRequest bind(CommentRequest request) {
        this.setPid(request.getId());
        this.setType(request.getType());
        this.setDataId(request.getDataId());
        this.setThumbUserUuid(request.getThumbUserUuid());
        return this;
    }

    public String whereSql(boolean isTrue) {
        StringBuilder sql = new StringBuilder("select * from (select *,(IF((select thumb from network_politics_comment_thumb B")
                .append(" where A.id = B.comment_id  and B.user_uuid = '").append(thumbUserUuid)
                .append("') = true, true, false)) as thumb,(IF((select comment_id from network_politics_comment_collection B")
                .append(" where A.id = B.comment_id  and B.user_id = '").append(thumbUserUuid)
                .append("') > 0, true, false)) as is_collection,(IF((select id from network_politics_comment_follow B")
                .append(" where A.creator_user_uuid = B.follow_user_id  and B.user_id = '").append(thumbUserUuid)
                .append("') > 0, true, false)) as is_follow from network_politics_comment A where id is not null");
        getWhereSql(sql);
        sql.append(")A where  A.id > 0");
        if (!ObjectUtils.isEmpty(this.isFollow) && isTrue) {
            sql.append(" and A.is_follow = ").append(this.isFollow);
        }
        if (!ObjectUtils.isEmpty(this.thumb) && isTrue) {
            sql.append(" and A.thumb = ").append(this.thumb);
        }
        if (!ObjectUtils.isEmpty(this.isCollection) && isTrue) {
            sql.append(" and A.is_collection = ").append(this.isCollection);
        }
        return sql.toString();
    }

    public String countSql(boolean isTrue) {
        StringBuilder sql = new StringBuilder("select count(*) from (select *,(IF((select thumb from network_politics_comment_thumb B")
                .append(" where A.id = B.comment_id  and B.user_uuid = '").append(thumbUserUuid)
                .append("') = true, true, false)) as thumb,(IF((select comment_id from network_politics_comment_collection B")
                .append(" where A.id = B.comment_id and B.user_id = '").append(thumbUserUuid)
                .append("') > 0, true, false)) as is_collection,(IF((select id from network_politics_comment_follow B")
                .append(" where A.creator_user_uuid = B.follow_user_id  and B.user_id = '").append(thumbUserUuid)
                .append("') > 0, true, false)) as is_follow from network_politics_comment A where id is not null");
        getWhereSql(sql);
        sql.append(")A where  A.id > 0");
        if (!ObjectUtils.isEmpty(this.isFollow) && isTrue) {
            sql.append(" and A.is_follow = ").append(this.isFollow);
        }
        if (!ObjectUtils.isEmpty(this.thumb) && isTrue) {
            sql.append(" and A.thumb = ").append(this.thumb);
        }
        if (!ObjectUtils.isEmpty(this.isCollection) && isTrue) {
            sql.append(" and A.is_collection = ").append(this.isCollection);
        }
        return sql.toString();
    }

    private void getWhereSql(StringBuilder sql) {
        if (StringUtils.hasLength(this.tenantUuid)) {
            sql.append(" and A.tenant_uuid = '").append(this.tenantUuid).append("'");
        }
        if (!ObjectUtils.isEmpty(this.pid)) {
            sql.append(" and A.pid = ").append(this.pid);
        }
        if (StringUtils.hasLength(this.creatorUserUuid)) {
            sql.append(" and A.creator_user_uuid = '").append(this.creatorUserUuid).append("'");
        }
        if (StringUtils.hasLength(this.replyUserUuid)) {
            sql.append(" and A.reply_user_uuid = '").append(this.replyUserUuid).append("'");
        }
        if (StringUtils.hasLength(this.type)) {
            sql.append(" and A.type = '").append(this.type).append("'");
        }
        if (!ObjectUtils.isEmpty(this.dataId)) {
            sql.append(" and A.data_id = ").append(this.dataId);
        }
        if (StringUtils.hasLength(this.name)) {
            sql.append("and (A.creator_user_uuid in (select uuid from sys_user where user_name like '%")
                    .append(this.name).append("%') or A.reply_user_uuid in (select uuid from sys_user where user_name like '%")
                    .append(this.name).append("%'))");
        }
    }
}
