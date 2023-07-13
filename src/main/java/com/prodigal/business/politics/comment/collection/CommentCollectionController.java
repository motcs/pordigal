package com.prodigal.business.politics.comment.collection;

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
@Tag(name = "留言收藏")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment/collection/manage/v1")
public class CommentCollectionController {

    private final CommentCollectionService commentCollectionService;

    @PostMapping
    public String save(@RequestBody CommentCollection commentCollection) {
        return this.commentCollectionService.save(commentCollection);
    }

}
