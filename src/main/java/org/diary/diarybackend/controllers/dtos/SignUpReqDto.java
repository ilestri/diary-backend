package org.diary.diarybackend.controllers.dtos;

import org.diary.diarybackend.commons.constants.Role;
import org.diary.diarybackend.commons.constants.Status;
import org.diary.diarybackend.entities.USERS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * SignUpDTO
 * 이 클래스는 회원가입 요청을 처리하는 데 사용되는 DTO 이다.
 * 회원가입 시 필요한 데이터를 수집하는 데 사용된다.
 * 이 데이터는 서버로 전송되어 회원가입 로직을 처리하는 데 필요하다.
 * <p>
 * 주요 목적 :
 * 클라이언트로 받은 원시 데이터를 안전하게 서버로 전달하는 것
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReqDto {
    private String userid;
    private String password;
    private String nickname;
    private Role role;
    private LocalDateTime createdDate;

    public USERS toEntity(String encodedPassword, Role role) {
        return USERS.builder().id(userid).password(encodedPassword).username(nickname).role(role).status(Status.Active).createdDate(createdDate == null ? LocalDateTime.now() : createdDate).build();
    }
}
