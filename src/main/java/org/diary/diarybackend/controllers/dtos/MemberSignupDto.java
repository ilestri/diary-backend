package org.diary.diarybackend.controllers.dtos;

import org.diary.diarybackend.entities.User;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignupDto {
    private Long id;
    private String username;

    static public MemberSignupDto toDto(User user) {
        return MemberSignupDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
