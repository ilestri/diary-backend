package org.diary.diarybackend.services;

import org.diary.diarybackend.controllers.dtos.JwtToken;
import org.diary.diarybackend.controllers.dtos.SignInReqDTO;
import org.diary.diarybackend.controllers.dtos.SignInResDTO;
import org.diary.diarybackend.provider.JwtTokenProvider;
import org.diary.diarybackend.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SignInService {

    private final UsersRepository usersRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignInResDTO signIn(SignInReqDTO signInReqDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": "
                            + fieldError.getDefaultMessage()).collect(Collectors.joining(", "));
            log.error("로그인 실패: {}", errorMessage);
            return new SignInResDTO(400, errorMessage, "", "");
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
            return new SignInResDTO(200, "성공", responseAccessToken, responseRefreshToken);

        } catch (Exception e) {
            log.error("로그인 중 예상치 못한 오류 발생: {}", e.getMessage());
            return new SignInResDTO(500, "로그인 중 예상치 못한 오류가 발생했습니다.", "", "");
        }
    }
}
