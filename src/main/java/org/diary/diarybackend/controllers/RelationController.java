package org.diary.diarybackend.controllers;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diary.diarybackend.controllers.dtos.Relation.GetNicknmResDTO;
import org.diary.diarybackend.controllers.dtos.Relation.PushAlarmReqDTO;
import org.diary.diarybackend.controllers.dtos.TotalResDTO;
import org.diary.diarybackend.services.Relation.GetNicknmService;
import org.diary.diarybackend.services.Relation.PushAlameService;
import org.diary.diarybackend.utils.UseToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/relation")
@RequiredArgsConstructor
public class RelationController {

    private final GetNicknmService getNicknmService;
    private final PushAlameService pushAlameService;
    private final UseToken useToken;

    @GetMapping(value = "/get-nickname", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TotalResDTO> getUserInfo(
            @RequestParam("phone_number") String phone_number,
            @RequestHeader("Authorization") String token) {

        // 토큰 유효성 검증 및 처리
        ResponseEntity<TotalResDTO> validationResult = useToken.validateToken(token);
        if (validationResult != null) {
            return validationResult; // 토큰이 유효하지 않다면, 해당 응답을 반환합니다.
        }

        // phone_number로 닉네임 및 프로필 정보 조회
        GetNicknmResDTO getNicknmResponse = getNicknmService.getNicknm(phone_number);

        TotalResDTO response = TotalResDTO.builder()
                .resultCode(HttpStatus.OK.value())
                .resultMessage("사용자 정보를 성공적으로 추출했습니다.")
                .data(getNicknmResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/push_alarm", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TotalResDTO> getUserInfo(@Valid @RequestBody PushAlarmReqDTO request,
            @RequestHeader("Authorization") String token) {

        String result = pushAlameService.PushAlame(request, token);

        TotalResDTO response = TotalResDTO.builder()
                .resultCode(HttpStatus.OK.value())
                .resultMessage("사용자 요청이 정상적으로 완료되었습니다.")
                .data(result)
                .build();

        return ResponseEntity.ok(response);
    }
}




