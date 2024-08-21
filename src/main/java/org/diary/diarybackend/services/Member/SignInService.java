package org.diary.diarybackend.services.Member;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diary.diarybackend.controllers.dtos.JwtToken;
import org.diary.diarybackend.controllers.dtos.Member.SignInReqDTO;
import org.diary.diarybackend.controllers.dtos.Member.SignInResDTO;
import org.diary.diarybackend.provider.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SignInService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignInResDTO signIn(SignInReqDTO signInReqDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": "
                            + fieldError.getDefaultMessage()).collect(Collectors.joining(", "));
            log.error("로그인 실패: {}", errorMessage);
            return new SignInResDTO(null, null);  // 실패 시 토큰 정보 없음
        }

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    signInReqDTO.getEmail(), signInReqDTO.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject()
                    .authenticate(authenticationToken);

            JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
            String responseAccessToken = jwtToken.getGrantType() + jwtToken.getAccessToken();
            String responseRefreshToken = jwtToken.getGrantType() + jwtToken.getRefreshToken();

            log.info("로그인 성공: 이메일 = {}, 액세스 토큰 = {}", signInReqDTO.getEmail(), responseAccessToken);
            return new SignInResDTO(responseAccessToken, responseRefreshToken);  // 성공 시 토큰 정보 포함

        } catch (Exception e) {
            log.error("로그인 중 예상치 못한 오류 발생: {}", e.getMessage());
            return new SignInResDTO(null, null);  // 실패 시 토큰 정보 없음
        }
    }
}
