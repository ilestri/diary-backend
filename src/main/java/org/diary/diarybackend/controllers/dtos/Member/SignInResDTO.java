package org.diary.diarybackend.controllers.dtos.Member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResDTO {

    private String accessToken;
    private String refreshToken;
}
