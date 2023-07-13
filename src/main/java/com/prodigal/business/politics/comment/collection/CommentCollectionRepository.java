package com.prodigal.business.politics.comment.collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
public interface CommentCollectionRepository extends JpaRepository<CommentCollection, Long>, JpaSpecificationExecutor<CommentCollection> {

    CommentCollection findByUserIdAndCommentId(String userId, Long commentId);

    void deleteAllByCommentId(Long commentId);

}