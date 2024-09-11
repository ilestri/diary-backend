package org.diary.diarybackend.controllers.dtos.Alarm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAlarmResDTO {

    private String title;
    private String message;
    private Boolean is_read;
    private Long type;
}
