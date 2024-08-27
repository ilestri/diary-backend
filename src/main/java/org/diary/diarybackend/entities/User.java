package org.diary.diarybackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity // JPA 엔티티로 지정
@Data // Lombok: getter, setter, toString, equals, hashCode 메서드 자동 생성
@NoArgsConstructor // Lombok: 기본 생성자 자동 생성
@AllArgsConstructor // Lombok: 모든 필드를 포함한 생성자 자동 생성
@Builder // Lombok: 빌더 패턴으로 객체 생성
@Table(name = "users")
public class User implements UserDetails { // 클래스명도 일반적으로 단수형 사용

    @Id // 기본 키 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 전략 지정
    @Column(name = "id", unique = true, nullable = false) // 유니크, 널 불가 지정
    private Long id; // 유저 PK

    @Column(name = "username", length = 50, nullable = false) // 널 불가 지정
    private String username; // 실명

    @Column(name = "phone_number", length = 20, nullable = false, unique = true) // 널 불가, 유니크 지정
    private String phoneNumber; // 휴대폰 번호

    @Column(name = "email", length = 100, nullable = false, unique = true) // 널 불가, 유니크 지정
    private String email; // 이메일

    @Column(name = "password", nullable = false) // 널 불가 지정
    private String password; // 비밀번호

    @Column(name = "birthdate", nullable = false) // 널 불가 지정
    private LocalDate birthdate; // 생년월일

    @CreationTimestamp // 생성 시점 자동 기록
    @Column(name = "created_date", nullable = false, updatable = false) // 널 불가, 수정 불가 지정
    private LocalDateTime createdDate; // 계정 생성 시간

    @UpdateTimestamp // 수정 시점 자동 기록
    @Column(name = "updated_date", nullable = false) // 널 불가 지정
    private LocalDateTime updatedDate; // 마지막 수정 시간

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
