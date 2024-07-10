package org.diary.diarybackend.services;

import org.diary.diarybackend.entities.USERS;
import org.diary.diarybackend.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Spring Security의 인증 과정에서 사용자의 상세 정보를 로드하는 서비스 클래스.
 * 이 클래스는 UserDetailsService 인터페이스를 구현하여, 데이터베이스에서 사용자 정보를 조회하고 Spring Security가 사용할 수 있는 형태로 제공합니다.
 * <p>
 * 주요 기능:
 * - 사용자 이름(ID)을 바탕으로 사용자 정보를 조회하고, 해당 정보를 UserDetails 객체로 변환합니다.
 * - 사용자가 존재하지 않을 경우 UsernameNotFoundException을 발생시킵니다.
 * <p>
 * 구성 요소:
 * - UsersRepository: 사용자 정보를 데이터베이스에서 조회하기 위한 JPA 리포지토리.
 * - PasswordEncoder: 비밀번호를 안전하게 인코딩하기 위한 Spring Security의 컴포넌트.
 * <p>
 * 메소드 설명:
 * - loadUserByUsername(String id): 제공된 사용자 ID로 데이터베이스에서 사용자를 조회합니다.
 * 조회된 사용자 정보는 createUserDetails 메서드를 통해 Spring Security에서 사용하는 UserDetails 객체로 매핑됩니다.
 * - createUserDetails(USERS user): USERS 엔티티 객체를 받아 UserDetails 객체를 생성합니다.
 * 이 때, 사용자의 권한과 비밀번호가 처리됩니다.
 * <p>
 * 예외 처리:
 * - UsernameNotFoundException: 사용자 ID로 사용자 정보를 찾을 수 없을 때 발생합니다.
 * 이 예외는 Spring Security의 인증 프로세스에서 사용자가 존재하지 않음을 알리는 데 사용됩니다.
 * <p>
 * 사용 예:
 * - 이 서비스는 Spring Security의 인증 과정에서 자동으로 사용됩니다. 특정 사용자 ID로 로그인을 시도할 때,
 * loadUserByUsername 메서드가 호출되어 사용자의 존재 유무를 확인하고, UserDetails를 생성하여 인증을 수행합니다.
 */

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return usersRepository.findById(id).map(this::createUserDetails).orElseThrow(() -> new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(USERS USERS) {
        return User.builder().username(USERS.getUsername()).password(passwordEncoder.encode(USERS.getPassword())).authorities(USERS.getAuthorities()).build();
    }
}
