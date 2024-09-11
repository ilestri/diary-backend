package org.diary.diarybackend.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diary.diarybackend.controllers.dtos.Alarm.GetAlarmResDTO;
import org.diary.diarybackend.controllers.dtos.TotalResDTO;
import org.diary.diarybackend.services.Alarm.GetAlameService;
import org.diary.diarybackend.utils.UseToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/Notification")
@RequiredArgsConstructor
public class AlarmController {

    private final GetAlameService getAlameService;
    private final UseToken useToken;

    @GetMapping(value = "/get-Alarm", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TotalResDTO> getUserAlarm(
            @RequestHeader("Authorization") String token) {

        // 토큰 유효성 검증 및 처리
        ResponseEntity<TotalResDTO> validationResult = useToken.validateToken(token);
        if (validationResult != null) {
            return validationResult; // 토큰이 유효하지 않다면, 해당 응답을 반환합니다.
        }

        // 여러 개의 알람을 가져오는 로직
        List<GetAlarmResDTO> alarmResponses = getAlameService.GetAlame(token);

        if (alarmResponses == null || alarmResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(TotalResDTO.builder()
                            .resultCode(HttpStatus.NOT_FOUND.value())
                            .resultMessage("알람이 없습니다.")
                            .build());
        }

        // 알람 리스트를 TotalResDTO에 포함하여 반환
        TotalResDTO response = TotalResDTO.builder()
                .resultCode(HttpStatus.OK.value())
                .resultMessage("알람을 성공적으로 가져왔습니다.")
                .data(alarmResponses) // 리스트 데이터를 포함
                .build();

        return ResponseEntity.ok(response);
    }
}