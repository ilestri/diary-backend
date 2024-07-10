package org.diary.diarybackend.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResDTO {
    private int resultCode;
    private String resultMessage;
    private String result;
}
