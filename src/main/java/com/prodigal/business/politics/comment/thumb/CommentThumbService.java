package com.prodigal.business.politics.comment.thumb;

import com.prodigal.business.politics.comment.Comment;
import com.prodigal.business.politics.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
@Service
@RequiredArgsConstructor
public class CommentThumbService {

    private final CommentRepository commentRepository;
    private final CommentThumbRepository commentThumbRepository;

    public CommentThumb save(CommentThumb commentThumb) {
        commentThumb.setUpdatedTime(LocalDateTime.now());
        // 根据留言ID以及代表ID查找点赞记录
        CommentThumb old = this.commentThumbRepository
                .findByCommentIdAndUserUuid(commentThumb.getCommentId(), commentThumb.getUserUuid());
        if (ObjectUtils.isEmpty(old)) {
            commentThumb.setThumb(true);
            commentThumb.setCreatedTime(LocalDateTime.now());
        } else {
            commentThumb.setId(old.getId());
            commentThumb.setThumb(!old.getThumb());
            commentThumb.setCreatedTime(old.getCreatedTime());
        }
        CommentThumb save = this.commentThumbRepository.save(commentThumb);
        //根据评论的ID查询父级评论
        Optional<Comment> byId = commentRepository.findById(commentThumb.getCommentId());
        if (byId.isPresent()) {
            Comment comment = byId.get();
            //判断是点赞还是取消点赞
            if (save.getThumb()) {
                //点赞则评论的点赞数量+1
                comment.setThumbNumber(comment.getThumbNumber() + 1);
            } else {
                //取消点赞则评论的点赞数量-1
                comment.setThumbNumber(comment.getThumbNumber() - 1);
            }
            this.commentRepository.save(comment);
        }
        return save;
    }

    @Transactional(rollbackOn = {Exception.class})
    public void delete(Long commentId) {
        List<CommentThumb> allByCommentId = this.commentThumbRepository.findAllByCommentId(commentId);
        this.commentThumbRepository.deleteAll(allByCommentId);
    }
}
