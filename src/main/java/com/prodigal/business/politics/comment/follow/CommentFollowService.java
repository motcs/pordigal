package com.prodigal.business.politics.comment.follow;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
@Service
@RequiredArgsConstructor
public class CommentFollowService {

    private final CommentFollowRepository commentFollowRepository;

    public String save(CommentFollow commentFollow) {
        commentFollow.setCreatedTime(LocalDateTime.now());
        commentFollow.setUpdatedTime(LocalDateTime.now());
        CommentFollow byUserIdAndFollowUserId = this.commentFollowRepository
                .findByUserIdAndFollowUserId(commentFollow.getUserId(), commentFollow.getFollowUserId());
        if (!ObjectUtils.isEmpty(byUserIdAndFollowUserId)) {
            this.commentFollowRepository.delete(byUserIdAndFollowUserId);
            return "取消关注成功";
        }
        this.commentFollowRepository.save(commentFollow);
        return "关注成功";
    }

}
