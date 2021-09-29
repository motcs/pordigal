package com.prodigal.home.security.user;

import com.prodigal.home.security.role.Role;

import java.util.List;

/**
 * @author jjh
 * @classname UserService
 * @date 2021/9/29 create
 */
public interface UserService {
    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return 返回用户信息
     */
    User saveUser(User user);

    /**
     * 保存权限
     *
     * @param role 权限信息
     * @return 返回添加的权限
     */
    Role saveRole(Role role);

    /**
     * 根据权限名给用户赋权限
     *
     * @param username 用户名
     * @param roleName 权限名
     */
    void addRoleToUser(String username, String roleName);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 返回用户信息
     */
    User getUser(String username);

    /**
     * 获取全部用户信息
     *
     * @return 返回信息
     */
    List<User> getUsers();
}
