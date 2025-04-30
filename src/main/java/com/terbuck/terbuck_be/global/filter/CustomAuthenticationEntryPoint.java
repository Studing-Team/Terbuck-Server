package com.terbuck.terbuck_be.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terbuck.terbuck_be.common.dto.ErrorStatusResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json;charset=UTF-8");

        String message = "Token이 만료되었습니다.";

        ErrorStatusResponse errorResponse = ErrorStatusResponse.of(
                HttpServletResponse.SC_UNAUTHORIZED,
                message
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
