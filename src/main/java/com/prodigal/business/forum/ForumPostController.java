package com.prodigal.business.forum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-11 星期二
 */
@Log4j2
@RestController
@Schema(title = "发布帖子")
@RequiredArgsConstructor
@RequestMapping("/forum/post/v1")
public class ForumPostController {

    private final ForumPostService forumPostService;

    @GetMapping("page")
    @Operation(summary = "分页查询")
    public Page<ForumPost> page(ForumPostRequest request, Pageable pageable) {
        return this.forumPostService.page(request, pageable);
    }

    @GetMapping("search")
    @Operation(summary = "查询")
    public List<ForumPost> search(ForumPostRequest request) {
        return this.forumPostService.search(request);
    }

    @PostMapping
    @Operation(summary = "提交|修改")
    public ForumPost operation(@RequestBody ForumPostRequest request) {
        return this.forumPostService.operation(request);
    }

    @DeleteMapping
    @Operation(summary = "批量删除")
    public void delete(@RequestBody List<Long> ids) {
        this.forumPostService.delete(ids);
    }

}
