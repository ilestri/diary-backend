package org.diary.diarybackend.controllers.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class SignInReqDTO {
    private String id;
    private String password;
}
