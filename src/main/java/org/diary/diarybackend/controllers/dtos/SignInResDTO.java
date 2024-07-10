package org.diary.diarybackend.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResDTO {
    private int resultCode;
    private String resultMessage;
    private String accessToken;
    private String refreshToken;
}
