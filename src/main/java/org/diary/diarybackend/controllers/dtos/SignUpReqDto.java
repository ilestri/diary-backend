package org.diary.diarybackend.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.diary.diarybackend.entities.USERS;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReqDto {
    private String email;
    private String password;
    private String username;

    public USERS toEntity(String encodedPassword) {
        return USERS.builder()
                .email(email)
                .password(encodedPassword)
                .username(username)
                .build();
    }
}
