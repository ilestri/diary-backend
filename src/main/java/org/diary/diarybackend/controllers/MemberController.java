package org.diary.diarybackend.controllers;

import javax.validation.Valid;
import org.diary.diarybackend.controllers.dtos.*;
import org.diary.diarybackend.services.SignInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diary.diarybackend.services.SignUpService;
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
    public ResponseEntity<SignInResDTO> signIn(@Valid @RequestBody SignInReqDTO signInReqDTO,
            BindingResult bindingResult) {
        SignInResDTO response = signInService.signIn(signInReqDTO, bindingResult);
        if (response.getResultCode() != 200) {
            return ResponseEntity.status(HttpStatus.valueOf(response.getResultCode()))
                    .body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/sign_up", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignUpResDTO> signUp(@Valid @RequestBody SignUpReqDto signUpReqDto,
            BindingResult bindingResult) {
        SignUpResDTO response = signUpService.signUp(signUpReqDto, bindingResult);
        if (response.getResultCode() != 200) {
            return ResponseEntity.status(HttpStatus.valueOf(response.getResultCode()))
                    .body(response);
        }
        return ResponseEntity.ok(response);
    }
}

