package com.fubao.project.global.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fubao.project.global.common.exception.ErrorResponse;
import com.fubao.project.global.common.exception.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ResponseCode responseCode = (ResponseCode) request.getAttribute("exception");
        setResponse(response, responseCode);
    }

    private void setResponse(HttpServletResponse response, ResponseCode responseCode) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        if(responseCode==null)
            responseCode = ResponseCode.UNAUTHORIZED;
        objectMapper.writeValue(response.getWriter(), ResponseEntity.status(responseCode.getStatus()).body(ErrorResponse.of(responseCode)));
    }
}