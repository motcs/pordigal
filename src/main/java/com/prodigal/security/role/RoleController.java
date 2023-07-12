package com.prodigal.security.role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@RequestMapping("/api/role/v1")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @Operation(summary = "查询")
    public List<Role> search() {
        return this.roleService.search();
    }

    @PostMapping
    @Operation(summary = "提交|修改")
    public Role operation(@RequestBody Role role) {
        return this.roleService.operation(role);
    }

    @DeleteMapping
    @Operation(summary = "批量删除")
    public void delete(@RequestBody List<Long> ids) {
        this.roleService.delete(ids);
    }

}
