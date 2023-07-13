package com.prodigal.business.politics;

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
@Tag(name = "网络议政主题管理v1")
@RestController
@RequiredArgsConstructor
@RequestMapping("/politics/manage/v1")
public class PoliticsController {
    private final PoliticsService politicsService;

    @GetMapping("page")
    @Operation(summary = "分页查询")
    public Page<Politics> page(PoliticsRequest request, @PageableDefault(sort = "updatedTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return this.politicsService.page(request, pageable);
    }

    @GetMapping("search")
    @Operation(summary = "超级查询")
    public List<Politics> search(PoliticsRequest request) {
        return this.politicsService.search(request);
    }

    @GetMapping("comment")
    @Operation(summary = "根据代表uuid查询评论过的网络议政信息")
    public List<Politics> comment(String repUuid) {
        return this.politicsService.comment(repUuid);
    }

    @PostMapping
    @Operation(summary = "议政添加")
    public Politics operation(@Valid @RequestBody PoliticsRequest request) {
        return this.politicsService.operation(request);
    }

    @Operation(summary = "批量删除")
    @DeleteMapping("delete/batch")
    public void deleteBatch(@RequestBody List<Long> ids) {
        this.politicsService.deleteBatch(ids);
    }

    @Operation(summary = "单个删除")
    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        this.politicsService.delete(id);
    }
}
