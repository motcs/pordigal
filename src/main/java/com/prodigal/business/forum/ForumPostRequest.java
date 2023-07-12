package com.prodigal.business.forum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-11 星期二
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ForumPostRequest extends ForumPost {

    public ForumPost toForumPost() {
        ForumPost forumPost = new ForumPost();
        BeanUtils.copyProperties(this, forumPost);
        return forumPost;
    }

    public Specification<ForumPost> specification() {
        return (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (!ObjectUtils.isEmpty(this.getId())) {
                list.add(cb.equal(root.get("id").as(String.class), this.getId()));
            }
            if (StringUtils.hasLength(this.getTitle())) {
                list.add(cb.like(root.get("title").as(String.class), "%" + this.getTitle() + "%"));
            }
            if (StringUtils.hasLength(this.getUsername())) {
                list.add(cb.equal(root.get("username").as(String.class), this.getUsername()));
            }
            if (StringUtils.hasLength(this.getUsernames())) {
                list.add(cb.like(root.get("usernames").as(String.class), "%" + this.getUsernames() + "%"));
            }
            query.where(list.toArray(new Predicate[0]));
            return query.getRestriction();
        };
    }

}
