package com.prodigal.security.role;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jjh
 * @classname RoleRepo
 * @date 2021/9/29 create
 */
public interface RoleRepo extends JpaRepository<Role, Long> {
    /**
     * 根据权限名查询权限
     *
     * @param name 权限名
     * @return 返回权限信息
     */
    Role findByName(String name);
}
