package org.diary.diarybackend.controllers.dtos.Relation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetNicknmResDTO {

    private Long user_id;
    private String nickname;
    private String profileImageUrl;
}
