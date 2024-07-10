package org.diary.diarybackend.controllers;

import org.diary.diarybackend.controllers.dtos.*;
import org.diary.diarybackend.services.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 관련 요청을 처리하는 컨트롤러 클래스.
 * 이 클래스는 회원가입, 로그인 등의 회원 관련 기능을 제공하는 REST API 엔드포인트를 정의합니다.
 * 클라이언트로부터의 요청을 받아 서비스 계층으로 전달하고, 처리 결과를 응답으로 반환합니다.
 * <p>
 * 주요 기능:
 * 1. **로그인**: 사용자 아이디와 비밀번호를 받아 로그인을 처리하고, JWT 토큰을 발급합니다.
 * 2. **회원가입**: 사용자 아이디, 비밀번호, 닉네임을 받아 회원가입을 처리합니다.
 * <p>
 * 각 메서드 설명:
 * - signIn: 클라이언트로부터 아이디와 비밀번호를 받아 로그인을 시도합니다. 로그인 성공 시 JWT 토큰을 반환합니다.
 * 요청 본문의 아이디나 비밀번호가 비어있는 경우, 400 상태 코드와 함께 오류 메시지를 반환합니다.
 * - signUp: 클라이언트로부터 아이디, 비밀번호, 닉네임을 받아 회원가입을 시도합니다.
 * 필요한 모든 필드가 제공되지 않은 경우, 400 상태 코드와 함께 오류 메시지를 반환합니다.
 * 회원가입 로직이 성공하면 200 상태 코드와 성공 메시지를 반환합니다.
 * <p>
 * 예외 처리:
 * - 각 메서드는 입력 값의 유효성 검사를 수행하고, 유효하지 않은 경우 적절한 HTTP 상태 코드와 메시지를 반환합니다.
 * - 서비스 계층에서 발생하는 예외를 적절히 처리하고, 이를 통해 발생할 수 있는 상황에 대해 예외 처리 로직을 구현합니다.
 * - 내부 서버 오류가 발생하는 경우, 500 상태 코드와 함께 오류 메시지를 반환합니다.
 * <p>
 * 모든 회원 관련 요청은 이 경로를 통해 이 클래스로 라우팅됩니다.
 */

@Slf4j
@RestController
@RequestMapping(value = "/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping(value = "/sign_in", produces = MediaType.APPLICATION_JSON_VALUE) // 모든 사용자에게 허용
    public ResponseEntity<SignInResDTO> signIn(@RequestBody SignInReqDTO signInReqDTO) {
        try {
            if (signInReqDTO.getId() == null || signInReqDTO.getPassword() == null) {
                return ResponseEntity.badRequest().body(new SignInResDTO(400, "Id or Password not be empty", "", ""));
            }

            String id = signInReqDTO.getId();
            String password = signInReqDTO.getPassword();
            JwtToken jwtToken = memberService.login(id, password); // token 부여
            String responseAccessToken = jwtToken.getAccessToken();
            String responseRefreshToken = jwtToken.getRefreshToken();
            log.info("request id = {}, password = {}", id, password); // 후에 비밀번호 로그 제거 예정
            log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
            return ResponseEntity.ok(new SignInResDTO(200, "Success", responseAccessToken, responseRefreshToken));
        } catch (SignInException e) {
            return ResponseEntity.status(e.getStatus()).body(new SignInResDTO(e.getStatus().value(), e.getMessage(), null, null));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().body(new SignInResDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null, null));
        }
    }

    @PostMapping(value = "/sign_up")
    public ResponseEntity<SignUpResDTO> signUp(@RequestBody SignUpReqDto signUpReqDto) {
        log.debug("Received signUp request with ID: {}", signUpReqDto.getUserid());
        if (signUpReqDto.getUserid() == null || signUpReqDto.getPassword() == null || signUpReqDto.getNickname() == null) {
            log.debug("Bad Request: Missign required fields");
            return ResponseEntity.badRequest().body(new SignUpResDTO(400, "Invalid request", "Failure"));
        }
        try {
            memberService.signUp(signUpReqDto);
            log.debug("SignUp Success: {}", signUpReqDto.getUserid());
            return ResponseEntity.ok(new SignUpResDTO(200, "Success", "Success"));
        } catch (IllegalArgumentException e) {
            log.error("SignUp Failure: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new SignUpResDTO(400, e.getMessage(), "Failure"));
        } catch (Exception e) {
            log.error("Internal Server Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new SignUpResDTO(500, "Internal Server Error", "Failure"));
        }
    }

}
