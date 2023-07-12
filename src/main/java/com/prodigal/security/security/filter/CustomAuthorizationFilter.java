package com.prodigal.security.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 授权管理器
 *
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-26 星期一
 */
@Log4j2
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    public static final List<String> SERVLET_PATH_ = List.of("/api/login", "/api/refresh/token", "/api/register");

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        //权限验证
        if (SERVLET_PATH_.contains(request.getServletPath())) {
            //如果是登录接口，则什么都不做放行
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader("X-Auth-Token");
            //验证权限是否开头带了Prodigal 以及一个空格,如果由，则直接放行，单词及空则由前端拼接后面加上token
            log.info("开始验证权限：{}", authorizationHeader);
            if (StringUtils.hasLength(authorizationHeader)) {
                try {
                    log.info("权限验证通过，开始删除自定义权限头");
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJwt = verifier.verify(authorizationHeader);
                    String username = decodedJwt.getSubject();
                    String[] roles = decodedJwt.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    log.info("验证权限通过！");
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    log.error("您没有权限：{}", e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>(4);
                    error.put("error_message", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                log.info("权限验证失败，放行退出");
                filterChain.doFilter(request, response);
            }
        }
    }

}
