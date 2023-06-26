package com.prodigal;

import com.prodigal.security.role.Role;
import com.prodigal.security.user.User;
import com.prodigal.security.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author jjh
 * @classname ProdigalApplication
 * @date 2021/9/29 create
 */
@SpringBootApplication
public class ProdigalApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProdigalApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 初始化角色以及角色权限
     *
     * @param userService 服务
     * @return 返回值不管
     */
    @Bean
    CommandLineRunner runner(UserService userService) {
        return args -> {
            //用户权限
            userService.saveRole(new Role(null, "ROLE_USER"));
            //游客权限
            userService.saveRole(new Role(null, "ROLE_TOURIST"));
            //会员权限
            userService.saveRole(new Role(null, "ROLE_MEMBER"));
            //管理员
            userService.saveRole(new Role(null, "ROLE_ADMIN"));
            //超级管理员
            userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

            userService.saveUser(new User(null, "靳家航", "jjh", "1234", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
            userService.saveUser(new User(null, "李太行", "lth", "1234", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
            //靳家航账号赋值权限
            userService.addRoleToUser("jjh", "ROLE_USER");
            userService.addRoleToUser("jjh", "ROLE_MEMBER");
            userService.addRoleToUser("jjh", "ROLE_ADMIN");
            userService.addRoleToUser("jjh", "ROLE_SUPER_ADMIN");
            //李太行账号赋值权限
            userService.addRoleToUser("lth", "ROLE_USER");
            userService.addRoleToUser("lth", "ROLE_MEMBER");
            userService.addRoleToUser("lth", "ROLE_ADMIN");
            userService.addRoleToUser("lth", "ROLE_SUPER_ADMIN");
        };
    }
}
