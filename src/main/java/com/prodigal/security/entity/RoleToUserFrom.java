package com.prodigal.security.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 为用户赋值权限接参
 *
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-26 星期一
 */
@Data
@Schema(title = "用户关联角色")
public class RoleToUserFrom {

    @Schema(title = "用户名")
    private String username;

    @Schema(title = "角色名")
    private String roleName;

}
