package org.diary.diarybackend.controllers.dtos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.diary.diarybackend.entities.User;

import java.util.regex.Pattern;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReqDto {

    private String username;
    private String nickname;
    private String phone_number;
    private String email;
    private String password;
    private LocalDate birthdate;

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

        System.out.println("생성자 사용");
        System.out.println("username: " + this.username);
        System.out.println("nickname: " + this.nickname);
        System.out.println("phone_number: " + this.phone_number);
        System.out.println("email: " + this.email);
        System.out.println("password: " + this.password);
        System.out.println("birthdate: " + this.birthdate);
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

    private LocalDate formatBirthdate(String birthdate) {
        if (birthdate == null) {
            throw new IllegalArgumentException("Birthdate cannot be null");
        }
        // 생년월일 포맷팅 로직
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(birthdate, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid birthdate format. Expected format: yyyy-MM-dd");
        }
    }

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .username(username)
                .build();
    }
}
