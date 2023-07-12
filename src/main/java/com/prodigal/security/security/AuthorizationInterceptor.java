package com.prodigal.security.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prodigal.commons.HttpCommonsUtils;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.prodigal.security.authority.AuthorityService.authorities;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-28 星期三
 */
@Log4j2
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws IOException {
        // 获取当前请求URL,去除?以后的内容，容易匹配
        String requestUrl = HttpCommonsUtils.substringDoubt(request.getRequestURI(), "?");
        log.info("请求路径：{}", requestUrl);
        boolean hasPermission = authorities.keySet().stream()
                // 将内容中的*换成.+正则表达式，来判断路径是否存在
                .map(res -> res.replace("*", "[^/]+"))
                // 判断接口路径是否包含
                .filter(pattern -> Pattern.matches(pattern, requestUrl))
                .flatMap(interfacePath -> authorities.get(interfacePath).stream())
                .anyMatch(role -> Objects.equals(role, "ROLE_IGNORE") || request.isUserInRole(role));
        if (hasPermission) {
            // 用户具有所请求接口所需的权限
            return true;
        }

        // 用户没有所请求接口所需的权限
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Map<String, String> error = new HashMap<>(4);
        error.put("message", "您的权限不足，无法访问");
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
        return false;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                           @NotNull Object handler, ModelAndView modelAndView) {
        // Implementation if needed
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                @NotNull Object handler, Exception ex) {
        // Implementation if needed
    }

}
