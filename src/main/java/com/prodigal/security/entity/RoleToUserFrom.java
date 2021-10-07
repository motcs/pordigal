package com.prodigal.security.entity;

import lombok.Data;

/**
 * 为用户赋值权限接参
 *
 * @author jjh
 * @classname RoleToUserFrom
 * @date 2021/9/29 create
 */
@Data
public class RoleToUserFrom {
    private String username;
    private String roleName;
}
