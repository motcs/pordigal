package com.prodigal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-06-27 星期二
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // 设置允许的源，这里使用通配符 * 表示允许所有源
        configuration.addAllowedMethod("*"); // 设置允许的 HTTP 方法，这里使用通配符 * 表示允许所有方法
        configuration.addAllowedHeader("*"); // 设置允许的请求头，这里使用通配符 * 表示允许所有头部

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 对所有请求路径应用 CORS 配置
        return source;
    }
}
