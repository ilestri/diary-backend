package org.diary.diarybackend.controllers;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diary.diarybackend.controllers.dtos.Member.SignInReqDTO;
import org.diary.diarybackend.controllers.dtos.Member.SignInResDTO;
import org.diary.diarybackend.controllers.dtos.Member.SignUpReqDTO;
import org.diary.diarybackend.controllers.dtos.Member.SignUpResDTO;
import org.diary.diarybackend.controllers.dtos.TotalResDTO;
import org.diary.diarybackend.services.Member.SignInService;
import org.diary.diarybackend.services.Member.SignUpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class MemberController {

    private final SignInService signInService;
    private final SignUpService signUpService;

    @PostMapping(value = "/sign_in", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TotalResDTO> signIn(@Valid @RequestBody SignInReqDTO request, BindingResult bindingResult) {
        SignInResDTO signInResponse = signInService.signIn(request, bindingResult);

        int resultCode = (signInResponse.getAccessToken() != null) ? 200 : 401;
        String resultMessage = (signInResponse.getAccessToken() != null) ? "로그인 성공" : "로그인 실패";

        TotalResDTO response = TotalResDTO.builder()
                .resultCode(resultCode)
                .resultMessage(resultMessage)
                .data(signInResponse)
                .build();

        return ResponseEntity.status(HttpStatus.valueOf(resultCode)).body(response);
    }

    @PostMapping(value = "/sign_up", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TotalResDTO> signUp(@Valid @RequestBody SignUpReqDTO request, BindingResult bindingResult) {
        SignUpResDTO signUpResponse = signUpService.signUp(request, bindingResult);

        int resultCode = (signUpResponse != null && "회원가입 성공".equals(signUpResponse.getResult())) ? 200 : 400;
        String resultMessage = (resultCode == 200) ? "회원가입 성공" : "회원가입 실패";

        TotalResDTO response = TotalResDTO.builder()
                .resultCode(resultCode)
                .resultMessage(resultMessage)
                .data(signUpResponse)
                .build();

        return ResponseEntity.status(HttpStatus.valueOf(resultCode)).body(response);
    }
}
