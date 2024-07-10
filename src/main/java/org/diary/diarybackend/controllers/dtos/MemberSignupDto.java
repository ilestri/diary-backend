package org.diary.diarybackend.controllers.dtos;

import org.diary.diarybackend.entities.USERS;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignupDto {
    private Long id;
    private String username;

    static public MemberSignupDto toDto(USERS user) {
        return MemberSignupDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
