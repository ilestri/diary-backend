package org.diary.diarybackend.controllers.dtos;

import org.diary.diarybackend.entities.USERS;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignupDto {
    private String id;
    private String username;

    static public MemberSignupDto toDto(USERS USERS) {
        return MemberSignupDto.builder().id(USERS.getId()).username(USERS.getUsername()).build();
    }
}
