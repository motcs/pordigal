package com.prodigal.home.security.user;

import com.prodigal.home.security.role.Role;
import com.prodigal.home.security.role.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author jjh
 * @classname UserServiceImpl
 * @date 2021/9/29 create
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public User saveUser(User user) {
        log.info("开始保存用户{}", user);
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("开始保存权限{}", role);
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("开始为用户赋值权限username:{},roleName:{}", username, roleName);
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String username) {
        log.info("查询用户：{}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("查询所有用户");
        return userRepo.findAll();
    }
}
