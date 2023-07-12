package com.prodigal.security.authority;

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
@Schema(title = "角色与接口对应关系管理")
@RequiredArgsConstructor
@RequestMapping("/api/authority/v1")
public class AuthorityController {

    private final AuthorityService authorityService;

    @GetMapping("page")
    @Operation(summary = "分页查询")
    public Page<Authority> page(AuthorityRequest request, Pageable pageable) {
        return this.authorityService.page(request, pageable);
    }

    @GetMapping("search")
    @Operation(summary = "查询")
    public List<Authority> search(AuthorityRequest request) {
        return this.authorityService.search(request);
    }

    @PostMapping
    @Operation(summary = "提交|修改")
    public Authority operation(@RequestBody AuthorityRequest request) {
        return this.authorityService.operation(request);
    }

    @DeleteMapping
    @Operation(summary = "批量删除")
    public void delete(@RequestBody List<Long> ids) {
        this.authorityService.delete(ids);
    }

}
