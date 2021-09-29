package com.prodigal.home.security.user;

import com.prodigal.home.security.entity.RoleToUserFrom;
import com.prodigal.home.security.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * @author jjh
 * @classname UserController
 * @date 2021/9/29 create
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getUser() {
        return ResponseEntity.ok().body(this.userService.getUsers());
    }

    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return 返回添加后的数据
     */
    @PostMapping("user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(this.userService.saveUser(user));
    }

    @PostMapping("role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(this.userService.saveRole(role));
    }

    @PostMapping("/role/to/user")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserFrom from) {
        this.userService.addRoleToUser(from.getUsername(), from.getRoleName());
        return ResponseEntity.ok().build();
    }
}
