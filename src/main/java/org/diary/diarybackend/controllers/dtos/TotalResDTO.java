package org.diary.diarybackend.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalResDTO {

    private int resultCode;
    private String resultMessage;
    private Object data;
}
