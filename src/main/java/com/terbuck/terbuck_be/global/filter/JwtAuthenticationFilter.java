package com.terbuck.terbuck_be.global.filter;

import com.terbuck.terbuck_be.global.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JwtAuthenticationFilter");

        String token = getJwtFromRequest(request);  // 요청에서 JWT 토큰 추출
        if (token != null) {
            try {
                tokenProvider.validateToken(token);
                Long userId = tokenProvider.getUserIdFromToken(token); // 사용자 ID 추출

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                authenticationEntryPoint.commence(request, response, new BadCredentialsException("토큰이 만료되었습니다."));
            } catch (JwtException | IllegalArgumentException e) {
                authenticationEntryPoint.commence(request, response, new BadCredentialsException("유효하지 않은 토큰입니다."));
            }
        }
        filterChain.doFilter(request, response);  // 필터 체인 계속 실행
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
