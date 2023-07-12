package com.prodigal.security.user;

import com.prodigal.annotation.RestServerException;
import com.prodigal.security.role.Role;
import com.prodigal.security.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-26 星期一
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
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
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("没有该用户！");
            throw new UsernameNotFoundException("没有该用户!");
        } else {
            log.info("找到用户：{}", user);
        }
        //创建权限集合
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //循环将用户权限放入权限表
        user.getRole().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        //返回赋加权限后的用户信息
        return new org.springframework
                .security.core.userdetails
                .User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
        if (user.isNew()) {
            log.info("开始保存用户{}", user);
            //加密密码
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User register(User user) {
        if (user.isNew()) {
            log.info("开始保存用户{}", user);
            User byUsername = this.userRepository.findByUsername(user.getUsername());
            if (ObjectUtils.isEmpty(byUsername)) {
                //加密密码
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                return userRepository.save(user);
            }
            throw RestServerException.withMsg("当前用户名已被注册");
        }
        throw RestServerException.withMsg("注册接口，不允许修改用户信息");
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("开始为用户赋值权限username:{},roleName:{}", username, roleName);
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRole().add(role);
    }

    @Override
    public User getUser(String username) {
        log.info("查询用户：{}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("查询所有用户");
        return userRepository.findAll();
    }


}
