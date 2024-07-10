package org.diary.diarybackend.provider;

import org.diary.diarybackend.controllers.dtos.JwtToken;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * JSON Web Token (JWT) 공급자 클래스.
 * 이 클래스는 사용자 인증 후 JWT를 생성하고, 들어오는 요청의 JWT를 검증하며, 인증된 사용자의 정보를 추출하는 기능을 제공합니다.
 * JWT는 클라이언트와 서버 간의 상태를 유지하지 않는 환경에서 보안이 유지된 상태로 사용자 정보를 주고받기 위해 사용됩니다.
 * <p>
 * 주요 기능:
 * - JWT 생성: 인증된 사용자의 권한 정보를 바탕으로 액세스 토큰과 리프레시 토큰을 생성합니다.
 * - JWT 검증: 들어오는 요청에서 토큰을 추출하고 그 유효성을 검증합니다.
 * - 사용자 인증 정보 추출: 토큰에서 사용자의 세부 권한 정보를 추출하여 Spring Security의 인증 객체로 생성합니다.
 * <p>
 * 메소드 설명:
 * - generateToken(Authentication authentication): 인증 객체를 받아 사용자의 이름과 권한을 포함한 JWT를 생성합니다.
 * - getAuthentication(String accessToken): 액세스 토큰을 파싱하여 Spring Security의 인증 객체를 생성합니다.
 * - getUserIdFromToken(String token): 토큰에서 사용자의 ID를 추출합니다.
 * - validateToken(String token): 토큰의 유효성을 검사합니다. 토큰이 유효하지 않은 경우 로그를 기록하고 false를 반환합니다.
 * - parseClaims(String accessToken): 액세스 토큰에서 클레임을 추출합니다. 토큰이 만료된 경우 만료된 클레임을 반환합니다.
 * <p>
 * 구성 요소:
 * - JwtTokenProvider(@Value("${jwt.secret}") String secretKey): 생성자에서는 시크릿 키를 Base64로 디코딩하고 HMAC SHA-256 알고리즘을 사용해 키를 생성합니다.
 * - Key key: JWT 서명에 사용되는 비밀 키입니다.
 * <p>
 * 예외 처리:
 * - JWT 관련 예외(ExpiredJwtException, UnsupportedJwtException 등)를 적절히 처리하고, 로그를 통해 문제를 기록합니다.
 * <p>
 * 보안 주의:
 * - JWT는 중요한 보안 요소를 포함하므로 키 관리와 토큰의 안전한 전송 및 저장에 주의해야 합니다.
 */

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public JwtToken generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + 86400000);
        String accessToken = Jwts.builder().setSubject(authentication.getName()).claim("auth", authorities).setExpiration(accessTokenExpiresIn).signWith(key, SignatureAlgorithm.HS256).compact();

        String refreshToken = Jwts.builder().setExpiration(new Date(now + 86400000)).signWith(key, SignatureAlgorithm.HS256).compact();

        return JwtToken.builder().grantType("Bearer").accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public String getUserIdFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = parseClaims(token);
        String userId = claims.getSubject();
        if (userId == null) {
            throw new RuntimeException("토큰에서 userId를 찾을 수 없습니다.");
        }
        return userId;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
        }
        return false;
    }
}
