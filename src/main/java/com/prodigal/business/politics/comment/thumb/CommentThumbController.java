package com.prodigal.business.politics.comment.thumb;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
@Tag(name = "留言评论点赞管理v1")
@RestController
@RequiredArgsConstructor
@RequestMapping("/politics/comment/thumb/manage/v1")
public class CommentThumbController {
    private final CommentThumbService commentThumbService;

    @PostMapping("save")
    @Operation(summary = "点赞-取消点赞")
    public CommentThumb save(@Valid @RequestBody CommentThumb commentThumb) {
        return this.commentThumbService.save(commentThumb);
    }
}
