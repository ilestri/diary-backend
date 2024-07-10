package org.diary.diarybackend.filters;

import org.diary.diarybackend.provider.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * JWT 인증 토큰을 처리하기 위한 커스텀 필터입니다.
 * 이 필터는 들어오는 모든 HTTP 요청의 헤더에서 JWT 토큰을 추출하고 유효성을 검증합니다.
 * 토큰이 유효한 경우, Spring Security의 SecurityContext에 인증 정보를 설정하여
 * 시스템 내 다른 부분에서 현재 사용자의 인증 상태를 접근할 수 있도록 합니다.
 *
 * 주요 기능:
 * - 토큰 추출: HTTP 요청의 'Authorization' 헤더에서 'Bearer' 접두사를 가진 토큰을 추출합니다.
 * - 토큰 검증: 추출된 토큰의 유효성을 검사합니다. 토큰이 만료되었거나 문제가 있는 경우 JSON 형태로 적절한 HTTP 응답을 반환합니다.
 * - 인증 설정: 유효한 토큰의 경우, 해당 토큰에 대한 인증 정보를 생성하고, Spring Security의 SecurityContext에 설정합니다.
 *
 * 예외 처리:
 * - ExpiredJwtException: 토큰이 만료된 경우, 클라이언트에게 JSON 형식으로 401 Unauthorized와 "Expired JWT Token" 메시지를 반환합니다.
 * - Exception: 그 외 토큰 검증 실패 시 JSON 형식으로 "Invalid JWT Token" 메시지와 함께 401 Unauthorized를 반환합니다.
 *
 * 필터 구성 및 사용:
 * - 이 필터는 Spring Security 설정에 등록되어야 하며, 보통 SecurityConfig 클래스 내에서 HttpSecurity 객체를 통해 필터 체인에 추가됩니다.
 */

@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        try {
            String token = resolveToken(req);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"resultCode\": 401, \"resultMessage\": \"Expired JWT Token: " + e.getMessage() + "\", \"content\": \"\"}");
            return;
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"resultCode\": 401, \"resultMessage\": \"Invalid JWT Token: " + e.getMessage() + "\", \"content\": \"\"}");
            return;
        }

        chain.doFilter(req, res);
    }
}
