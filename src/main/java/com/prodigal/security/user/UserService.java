package com.prodigal.security.user;

import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-26 星期一
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
     * 注册用户
     *
     * @param user 用户信息
     * @return 返回用户信息
     */
    User register(User user);

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
