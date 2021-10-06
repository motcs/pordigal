package com.prodigal.security.user;

import com.prodigal.security.role.Role;
import com.prodigal.security.role.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
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
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    /**
     * 过滤登录，查找用户
     *
     * @param username 登录账号
     * @return 返回用户信息及用户权限信息
     * @throws UsernameNotFoundException 没有找到用户的报错
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("没有该用户！");
            throw new UsernameNotFoundException("没有该用户!");
        } else {
            log.info("找到用户：{}", user);
        }
        //创建权限集合
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //循环将用户权限放入权限表
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        //返回赋加权限后的用户信息
        return new org.springframework
                .security.core.userdetails
                .User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
        log.info("开始保存用户{}", user);
        //加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
