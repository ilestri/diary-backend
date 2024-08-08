package org.diary.diarybackend.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.diary.diarybackend.entities.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReqDto {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Nickname cannot be blank")
    private String nickname;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\d{10,15}", message = "Invalid phone number format")
    private String phone_number;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotNull(message = "Birthdate cannot be null")
    private LocalDate birthdate;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .username(username)
                .build();
    }
}
