package com.sparta.board.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.board.dto.MessageResponseDto;
import com.sparta.board.entity.enumSet.ErrorType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request 에 담긴 토큰을 가져온다.
        String token = jwtUtil.resolveToken(request);

        // 토큰이 null 이면 다음 필터로 넘어간다.
        if (token == null) {
//            jwtExceptionHandler(response, ErrorType.NOT_VALID_TOKEN);
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰이 유효하지 않으면 예외처리
        if (!jwtUtil.validateToken(token)) {
//            throw new JwtException(ErrorType.NOT_VALID_TOKEN);
            jwtExceptionHandler(response, ErrorType.NOT_VALID_TOKEN);
            return;
        }

        // 유효한 토큰이라면, 토큰으로부터 사용자 정보를 가져온다.
        Claims info = jwtUtil.getUserInfoFromToken(token);
        setAuthentication(info.getSubject());   // 사용자 정보로 인증 객체 만들기

        // 다음 필터로 넘어간다.
        filterChain.doFilter(request, response);

    }

    private void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(username); // 인증 객체 만들기
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 토큰에 대한 오류가 발생했을 때, 커스터마이징해서 Exception 처리 값을 클라이언트에게 알려준다.
    public void jwtExceptionHandler(HttpServletResponse response, ErrorType error) {
        response.setStatus(error.getCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(new MessageResponseDto(error.getMessage(), error.getCode()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
