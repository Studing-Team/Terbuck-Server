package com.terbuck.terbuck_be.global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // 모든 요청 허용
                )
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 비활성화
                .formLogin(AbstractHttpConfigurer::disable)  // 로그인 폼 비활성화
                .httpBasic(AbstractHttpConfigurer::disable); // HTTP Basic 인증도 비활성화

        return http.build();
    }
}


