package com.prodigal.security.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-26 星期一
 */
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    /**
     * 根据权限名查询权限
     *
     * @param name 权限名
     * @return 返回权限信息
     */
    Role findByName(String name);

}
