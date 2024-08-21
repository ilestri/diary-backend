package org.diary.diarybackend.utils;

import org.diary.diarybackend.controllers.dtos.TotalResDTO;
import org.diary.diarybackend.entities.User;
import org.diary.diarybackend.provider.JwtTokenProvider;
import org.diary.diarybackend.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UseToken {

    private final JwtTokenProvider jwtTokenProvider;
    private final UsersRepository usersRepository;

    @Autowired
    public UseToken(JwtTokenProvider jwtTokenProvider, UsersRepository usersRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.usersRepository = usersRepository;
    }

    // 사용자 ID 가져오기
    public String getUserId(String token) throws Exception {
        try {
            // 토큰에서 이메일 추출
            String email = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", "").trim());

            // 이메일로 사용자 id조회
            Optional<User> userOpt = usersRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                return String.valueOf(userOpt.get().getId());
            } else {
                throw new IllegalArgumentException("User not found for the provided email");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid token or user not found");
        }
    }

    // 토큰 유효성 검증 및 결과 반환
    public ResponseEntity<TotalResDTO> validateToken(String token) {
        try {
            if (!jwtTokenProvider.validateToken(token.replace("Bearer ", "").trim())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(TotalResDTO.builder()
                                .resultCode(HttpStatus.UNAUTHORIZED.value())
                                .resultMessage("Invalid token")
                                .data(null)
                                .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(TotalResDTO.builder()
                            .resultCode(HttpStatus.UNAUTHORIZED.value())
                            .resultMessage("Token validation failed: " + e.getMessage())
                            .data(null)
                            .build());
        }
        return null; // 토큰이 유효한 경우 null을 반환하여 컨트롤러에서 계속 진행하도록 합니다.
    }
}
