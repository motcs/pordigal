package com.prodigal.core.sensitive;

import com.prodigal.commons.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2022-06-28 11:40:31 星期二
 */
@Tag(name = "敏感词汇检测v1")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sensitive/vocabulary/v1")
public class SensitiveController {

    private final SensitiveService sensitiveService;

    @GetMapping("page")
    @Operation(summary = "分页查询")
    public Page<Sensitive> page(SensitiveRequest request, Pageable pageable) {
        return this.sensitiveService.page(request, pageable);
    }

    @GetMapping("search")
    @Operation(summary = "不分页查询")
    public List<Sensitive> search(SensitiveRequest request) {
        return this.sensitiveService.search(request);
    }

    @PostMapping
    @Operation(summary = "添加修改")
    public Sensitive operation(@RequestBody SensitiveRequest request) {
        return this.sensitiveService.operation(request);
    }

    @PostMapping("copy")
    @Operation(summary = "复制敏感词模板")
    public Result copy(@RequestBody SensitiveRequest request) {
        if (ObjectUtils.isEmpty(request.getTenantUuid())) {
            return Result.of(false, null, "被复制的租户编码不可为空");
        }
        if (ObjectUtils.isEmpty(request.getTenantCode())) {
            return Result.of(false, null, "需要复制的租户编码不可为空");
        }
        return this.sensitiveService.copy(request);
    }

    @PostMapping("detection")
    @Operation(summary = "敏感词汇检测")
    public Result vocabularyDetection(@RequestBody VocabularyDetection detection) {
        return this.sensitiveService.vocabularyDetection(detection);
    }

    @DeleteMapping
    @Operation(summary = "批量删除")
    public void deleteBatch(@RequestBody List<Long> ids) {
        this.sensitiveService.deleteBatch(ids);
    }


}
