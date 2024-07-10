package org.diary.diarybackend.services;

import org.diary.diarybackend.controllers.dtos.JwtToken;
import org.diary.diarybackend.controllers.dtos.MemberSignupDto;
import org.diary.diarybackend.controllers.dtos.SignUpReqDto;
import org.diary.diarybackend.entities.USERS;
import org.diary.diarybackend.provider.JwtTokenProvider;
import org.diary.diarybackend.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService { // 서비스 클래스 - 로그인 메서드 구현
    private final UsersRepository usersRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional // 메서드가 포함하고 있는 작업 중에 하나라도 실패할 경우 전체 작업을 취소
    public JwtToken login(String email, String password) {
        // Login Email/PW 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        // 실제 검증 부분 - 사용자 비밀번호 체크
        // authenticate() 메서드를 통해 요청된 Member에 대한 검증이 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성 -> 검증 정상 통과
        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional
    public MemberSignupDto signUp(SignUpReqDto signUpReqDto) {
        log.info("회원가입 시도 Email: {}", signUpReqDto.getEmail());

        if (usersRepository.existsByEmail(signUpReqDto.getEmail())) {
            log.warn("회원가입 실패 : Email 중복 : ", signUpReqDto.getEmail());
            throw new IllegalArgumentException("이미 사용중인 이메일 입니다.");
        }
        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpReqDto.getPassword());

        // 회원가입 시, USER 역할 부여
        USERS user = USERS.builder()
                .username(signUpReqDto.getUsername())
                .email(signUpReqDto.getEmail())
                .password(encodedPassword)
                .build();

        usersRepository.save(user);

        log.info("회원가입 성공 Email: {}", signUpReqDto.getEmail());

        return MemberSignupDto.toDto(user);
    }
}
