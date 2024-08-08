package org.diary.diarybackend.controllers;

import java.util.stream.Collectors;
import org.diary.diarybackend.controllers.dtos.*;
import org.diary.diarybackend.services.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/sign_in", produces = MediaType.APPLICATION_JSON_VALUE) // 모든 사용자에게 허용
    public ResponseEntity<SignInResDTO> signIn(@RequestBody SignInReqDTO signInReqDTO) {
        try {
            if (signInReqDTO.getEmail() == null || signInReqDTO.getPassword() == null) {
                return ResponseEntity.badRequest()
                        .body(new SignInResDTO(400, "Email or Password cannot be empty", "", ""));
            }

            String email = signInReqDTO.getEmail();
            String password = signInReqDTO.getPassword();
            JwtToken jwtToken = memberService.login(email, password); // token 부여
            String responseAccessToken = jwtToken.getGrantType() + jwtToken.getAccessToken();
            String responseRefreshToken = jwtToken.getGrantType() + jwtToken.getRefreshToken();
            log.info("request email = {}, password = {}", email, password); // 후에 비밀번호 로그 제거 예정
            log.info("jwtToken accessToken = {}, refreshToken = {}", responseAccessToken,
                    responseRefreshToken);
            return ResponseEntity.ok(
                    new SignInResDTO(200, "Success", responseAccessToken, responseRefreshToken));
        } catch (SignInException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new SignInResDTO(e.getStatus().value(), e.getMessage(), "", ""));
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new SignInResDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "An unexpected error occurred", null, null));
        }
    }

    // 예외처리 다시 해야함 일단 구현만 함
    @PostMapping(value = "/sign_up")
    public ResponseEntity<SignUpResDTO> signUp(@Validated @RequestBody SignUpReqDto signUpReqDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": "
                            + fieldError.getDefaultMessage()).collect(Collectors.joining(", "));
            log.error("회원가입 실패: {}", errorMessages);
            return ResponseEntity.badRequest()
                    .body(new SignUpResDTO(HttpStatus.BAD_REQUEST.value(), errorMessages,
                            "Failure"));
        }

        try {
            memberService.signUp(signUpReqDto);
            log.debug("회원가입 성공: {}", signUpReqDto.getEmail());
            return ResponseEntity.ok(new SignUpResDTO(200, "Success", "JWT 보내기 수정"));
        } catch (Exception e) {
            log.error("서버 오류: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new SignUpResDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(),
                            "Failure"));
        }
    }
}
