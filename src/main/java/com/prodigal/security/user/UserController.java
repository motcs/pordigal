package com.prodigal.security.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prodigal.security.entity.RoleToUserFrom;
import com.prodigal.security.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author jjh
 * @classname UserController
 * @date 2021/9/29 create
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @GetMapping("/user/all")
    public ResponseEntity<List<User>> getUser() {
        return ResponseEntity.ok().body(this.userService.getUsers());
    }

    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return 返回添加后的数据
     */
    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(this.userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(this.userService.saveRole(role));
    }

    @PostMapping("/role/to/user")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserFrom from) {
        for (Role role : userService.getUser(from.getUsername()).getRoles()) {
            if (role.getName().equals(from.getRoleName())) {
                throw new RuntimeException("很抱歉该用户已有此权限，权限不可以重复添加！");
            }
        }
        this.userService.addRoleToUser(from.getUsername(), from.getRoleName());
        return ResponseEntity.ok().build();
    }


    /**
     * 刷新token接口
     *
     * @param request  请求域
     * @param response 返回域
     */
    @GetMapping("/refresh/token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        log.info("开始验证权限是否符合书写规则");
        if (authorizationHeader != null && authorizationHeader.startsWith("Prodigal ")) {
            try {
                log.info("权限验证通过，开始删除自定义权限头");
                //将token自定义前缀删除，只留下权限信息
                String refreshToken = authorizationHeader.substring("Prodigal ".length());
                log.info("已删除自定义权限头：{}", refreshToken);
                //定义算法
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
                //加载jwt算法
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJwt = verifier.verify(refreshToken);
                //获取username
                String username = decodedJwt.getSubject();
                User user = userService.getUser(username);
                //定义一个token
                String accessToken = JWT.create()
                        //传入主题，使用用户的登录名
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURI())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>(4);
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
                log.info("验证权限通过！");

            } catch (Exception e) {
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>(4);
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);

            }
        } else {
            throw new RuntimeException("刷新token失败了");
        }


    }
}
