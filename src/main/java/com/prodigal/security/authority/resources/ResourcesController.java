package com.prodigal.security.authority.resources;

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
 * @since 2023-06-28 星期三
 */
@Log4j2
@RestController
@Schema(title = "请求接口资源管理")
@RequiredArgsConstructor
@RequestMapping("/api/resources/v1")
public class ResourcesController {

    private final ResourcesService resourcesService;

    @GetMapping("page")
    @Operation(summary = "分页查询")
    public Page<Resources> page(ResourcesRequest request, Pageable pageable) {
        return this.resourcesService.page(request, pageable);
    }

    @GetMapping("search")
    @Operation(summary = "查询")
    public List<Resources> search(ResourcesRequest request) {
        return this.resourcesService.search(request);
    }

    @PostMapping
    @Operation(summary = "提交|修改")
    public Resources operation(@RequestBody ResourcesRequest request) {
        return this.resourcesService.operation(request);
    }

    @DeleteMapping
    @Operation(summary = "批量删除")
    public void delete(@RequestBody List<Long> ids) {
        this.resourcesService.delete(ids);
    }

}
