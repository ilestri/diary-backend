package org.diary.diarybackend.services.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diary.diarybackend.controllers.dtos.Member.SignUpReqDTO;
import org.diary.diarybackend.controllers.dtos.Member.SignUpResDTO;
import org.diary.diarybackend.entities.User;
import org.diary.diarybackend.entities.User_Profile;
import org.diary.diarybackend.repositories.UsersRepository;
import org.diary.diarybackend.repositories.UserProfileRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SignUpService {

    private final UsersRepository usersRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResDTO signUp(SignUpReqDTO signUpReqDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": "
                            + fieldError.getDefaultMessage()).collect(Collectors.joining(", "));
            log.error("회원가입 실패: {}", errorMessage);
            return new SignUpResDTO("회원가입 실패");
        }

        if (usersRepository.existsByEmail(signUpReqDto.getEmail())) {
            log.error("회원가입 실패: 이미 사용 중인 이메일 - {}", signUpReqDto.getEmail());
            return new SignUpResDTO("이미 사용 중인 이메일입니다.");
        }

        try {
            String encodedPassword = passwordEncoder.encode(signUpReqDto.getPassword());

            User user = User.builder()
                    .username(signUpReqDto.getUsername())
                    .phoneNumber(signUpReqDto.getPhone_number())
                    .email(signUpReqDto.getEmail())
                    .password(encodedPassword)
                    .birthdate(signUpReqDto.getBirthdate())
                    .build();

            User savedUser = usersRepository.save(user);

            User_Profile userProfile = User_Profile.builder()
                    .user(savedUser)
                    .nickname(signUpReqDto.getNickname())
                    .build();

            userProfileRepository.save(userProfile);

            log.info("회원가입 성공: 이메일 = {}", signUpReqDto.getEmail());
            return new SignUpResDTO("회원가입 성공");

        } catch (Exception e) {
            log.error("회원가입 중 예상치 못한 오류 발생: {}", e.getMessage());
            return new SignUpResDTO("회원가입 중 예상치 못한 오류가 발생했습니다.");
        }
    }
}
