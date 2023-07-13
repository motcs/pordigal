package com.prodigal.business.politics.comment.follow;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
@Tag(name = "留言关注")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment/follow/manage/v1")
public class CommentFollowController {

    private final CommentFollowService commentFollowService;

    @PostMapping
    @Operation(summary = "关注|取消关注")
    public String save(@RequestBody CommentFollow commentFollow) {
        return this.commentFollowService.save(commentFollow);
    }

}
