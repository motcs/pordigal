package com.prodigal.business.politics.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
@Tag(name = "留言评论管理v1")
@RestController
@RequiredArgsConstructor
@RequestMapping("/politics/comment/manage/v1")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("page")
    @Operation(summary = "分页查询")
    public Page<CommentRequest> page(CommentRequest request, @PageableDefault(sort = "updatedTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return this.commentService.page(request, pageable);
    }

    @GetMapping("search")
    @Operation(summary = "超级查询")
    public List<CommentRequest> search(CommentRequest request, @PageableDefault(sort = "updatedTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return this.commentService.search(request, pageable);
    }

    @GetMapping("page/only")
    @Operation(summary = "多级分页查询")
    public Page<CommentRequest> pageOnly(CommentRequest request, @PageableDefault(sort = "updatedTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return this.commentService.pageOnly(request, pageable);
    }

    @GetMapping("number")
    @Operation(summary = "获取评论总数")
    public Long commentNumber(CommentRequest request) {
        return this.commentService.commentNumber(request);
    }

    @PostMapping
    @Operation(summary = "留言评论添加")
    public CommentRequest operation(@Valid @RequestBody CommentRequest request) {
        return this.commentService.operation(request);
    }

    @Operation(summary = "批量删除")
    @DeleteMapping("delete/batch")
    public void deleteBatch(@RequestBody List<Long> ids) {
        this.commentService.deleteBatch(ids);
    }

    @Operation(summary = "单个删除")
    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        this.commentService.delete(id);
    }

}
