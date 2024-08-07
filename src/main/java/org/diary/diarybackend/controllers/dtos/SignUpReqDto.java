package org.diary.diarybackend.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.diary.diarybackend.entities.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@Getter
@Builder
@NoArgsConstructor
public class SignUpReqDto {

    private String username;
    private String nickname;
    private String phone_number;
    private String email;
    private String password;
    private String birthdate;

    public SignUpReqDto(String username, String nickname, String phone_number, String email,
            String password, String birthdate) {
        if (username == null || nickname == null || password == null) {
            throw new IllegalArgumentException("Username, nickname and password cannot be null");
        }
        this.username = username;
        this.nickname = nickname;
        this.phone_number = formatPhoneNumber(phone_number);
        this.email = formatEmail(email);
        this.password = password;
        this.birthdate = formatBirthdate(birthdate);
    }

    private String formatPhoneNumber(String phone_number) {
        if (phone_number == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }
        // 휴대폰 번호 포맷팅 로직
        return phone_number.replaceAll("\\D", ""); // 숫자만 남기기
    }

    private String formatEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        // 이메일 포맷팅 및 유효성 검사 로직
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (Pattern.matches(emailPattern, email)) {
            return email.toLowerCase();
        } else {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private String formatBirthdate(String birthdate) {
        if (birthdate == null) {
            throw new IllegalArgumentException("Birthdate cannot be null");
        }
        // 생년월일 포맷팅 로직
        LocalDate date = LocalDate.parse(birthdate, DateTimeFormatter.ISO_DATE);
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .username(username)
                .build();
    }
}
