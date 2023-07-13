package com.prodigal.business.politics.comment.collection;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
@Service
@RequiredArgsConstructor
public class CommentCollectionService {

    private final CommentCollectionRepository commentCollectionRepository;

    public String save(CommentCollection commentCollection) {
        CommentCollection byUserIdAndCommentId = this.commentCollectionRepository
                .findByUserIdAndCommentId(commentCollection.getUserId(), commentCollection.getCommentId());
        if (!ObjectUtils.isEmpty(byUserIdAndCommentId)) {
            this.commentCollectionRepository.delete(byUserIdAndCommentId);
            return "取消收藏成功";
        }
        this.commentCollectionRepository.save(commentCollection);
        return "收藏成功";
    }

}
