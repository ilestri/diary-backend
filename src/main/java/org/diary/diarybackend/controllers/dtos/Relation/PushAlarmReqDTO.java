package org.diary.diarybackend.controllers.dtos.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushAlarmReqDTO {

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\d{10,15}", message = "Invalid phone number format")
    private String phone_number;
}
