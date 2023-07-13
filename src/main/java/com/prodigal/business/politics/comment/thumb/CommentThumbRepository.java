package com.prodigal.business.politics.comment.thumb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
public interface CommentThumbRepository extends JpaRepository<CommentThumb, Long>, JpaSpecificationExecutor<CommentThumb> {

    /**
     * 根据留言ID以及代表ID查找点赞记录
     *
     * @param commentId 留言ID
     * @param userUuid  代表用户uuid
     * @return 返回点赞信息
     */
    CommentThumb findByCommentIdAndUserUuid(Long commentId, String userUuid);

    /**
     * 根据评论id查询所有点赞信息
     *
     * @param commentId 评论id
     * @return 点赞信息
     */
    List<CommentThumb> findAllByCommentId(Long commentId);

}
