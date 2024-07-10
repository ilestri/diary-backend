package org.diary.diarybackend.controllers.dtos;

import org.diary.diarybackend.entities.USERS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReqDto {
    private String email;
    private String password;
    private String username;
    private LocalDateTime createdDate;

    public USERS toEntity(String encodedPassword) {
        return USERS.builder()
                .email(email)
                .password(encodedPassword)
                .username(username)
                .createdAt(createdDate == null ? LocalDateTime.now() : createdDate)
                .build();
    }
}
