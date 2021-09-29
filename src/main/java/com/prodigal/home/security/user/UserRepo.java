package com.prodigal.home.security.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jjh
 * @classname UserRepo
 * @date 2021/9/29 create
 */
public interface UserRepo extends JpaRepository<User, Long> {
    /**
     * 根据登录名查询信息
     *
     * @param username 登录名
     * @return 返回用户信息
     */
    User findByUsername(String username);
}
