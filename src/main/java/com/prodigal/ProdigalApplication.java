package com.prodigal;

import com.prodigal.security.role.Role;
import com.prodigal.security.role.RoleService;
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
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-26 星期一
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
//    @Bean
    CommandLineRunner runner(UserService userService, RoleService roleService) {
        return args -> {
            //用户权限
            roleService.operation(new Role(null, "ROLE_USER", "普通用户"));
            //会员权限
            roleService.operation(new Role(null, "ROLE_MEMBER", "会员"));
            //管理员
            roleService.operation(new Role(null, "ROLE_ADMIN", "管理员"));
            //超级管理员
            roleService.operation(new Role(null, "ROLE_SUPER_ADMIN", "超级管理员"));

            userService.saveUser(new User(null, "靳家航", "jjh",
                    "15738374392", "http://123.249.96.217:8001/photo/feifei.png",
                    "1234", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
            //靳家航账号赋值权限
            userService.addRoleToUser("jjh", "ROLE_USER");
            userService.addRoleToUser("jjh", "ROLE_MEMBER");
            userService.addRoleToUser("jjh", "ROLE_ADMIN");
            userService.addRoleToUser("jjh", "ROLE_SUPER_ADMIN");
        };
    }

}
