package org.diary.diarybackend.provider;

import org.diary.diarybackend.controllers.dtos.JwtToken;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

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
        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + 86400000);
        String accessToken = Jwts.builder().setSubject(authentication.getName()).setExpiration(accessTokenExpiresIn).signWith(key, SignatureAlgorithm.HS256).compact();

        String refreshToken = Jwts.builder().setExpiration(new Date(now + 86400000)).signWith(key, SignatureAlgorithm.HS256).compact();

        return JwtToken.builder().grantType("Bearer ").accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        UserDetails principal = new User(claims.getSubject(), "", null);
        return new UsernamePasswordAuthenticationToken(principal, "", null);
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
