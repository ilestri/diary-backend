package org.diary.diarybackend.entities;

import org.diary.diarybackend.commons.constants.Role;
import org.diary.diarybackend.commons.constants.Status;
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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

/**
 * 사용자 계정 데이터를 관리하는 엔티티 클래스.
 * 이 클래스는 데이터베이스의 'USERS' 테이블과 매핑되어 사용자 관련 데이터를 표현합니다.
 * <p>
 * 속성:
 * - id: 사용자의 고유 식별자(ID)입니다. 유니크 값으로 설정되어 있습니다.
 * - password: 사용자의 비밀번호입니다. 보안을 위해 길이 제한과 암호화 적용이 필요합니다.
 * - username: 사용자의 이름입니다. 이 필드는 인증 과정에서 username으로 사용됩니다.
 * - role: 사용자의 역할을 나타내며, 'Role' 열거형에서 정의된 값 중 하나를 가집니다.
 * - createdDate: 계정이 생성된 날짜와 시간입니다. 자동으로 현재 시간이 기록됩니다.
 * - modifiedDate: 계정 정보가 마지막으로 수정된 날짜와 시간입니다. 자동으로 현재 시간이 기록됩니다.
 * - status: 사용자의 계정 상태를 나타내며, 'Status' 열거형에서 정의된 값 중 하나를 가집니다.
 * <p>
 * 주요 기능 및 특징:
 * - @Entity: 이 클래스가 JPA 엔티티임을 나타냅니다.
 * - @Table: 이 클래스가 매핑될 데이터베이스 테이블의 이름을 지정합니다.
 * - @Data: Lombok 라이브러리의 어노테이션으로, getter, setter, toString, equals, hashCode 메소드를 자동 생성합니다.
 * - @NoArgsConstructor, @AllArgsConstructor: 각각 파라미터가 없는 생성자와 모든 필드를 포함한 생성자를 자동 생성합니다.
 * - @Builder: 빌더 패턴을 사용하여 객체를 생성할 수 있는 메커니즘을 제공합니다.
 * - @CreationTimestamp, @UpdateTimestamp: 각각 생성 및 수정 시점의 타임스탬프를 자동으로 설정합니다.
 * - UserDetails 인터페이스 구현: Spring Security에서 사용자 정보를 관리하기 위한 메소드를 구현합니다.
 * <p>
 * UserDetails 인터페이스 메소드:
 * - getAuthorities(): 사용자의 권한을 반환합니다. 'ROLE_' 접두어를 사용하여 Spring Security의 권한 명세에 맞게 반환합니다.
 * - getUsername(): 인증 시 사용되는 사용자의 ID를 반환합니다.
 * - getPassword(): 사용자의 비밀번호를 반환합니다.
 * - isAccountNonExpired(), isAccountNonLocked(), isCredentialsNonExpired():
 * 각각 계정의 만료, 잠김, 자격 증명 만료 여부를 반환합니다. 이 예제에서는 항상 true를 반환합니다.
 * - isEnabled(): 사용자의 활성화 상태를 반환합니다. 'Status'가 'Active'일 경우 true를 반환합니다.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "USERS")
public class USERS implements UserDetails {
    @Id
    @Column(name = "userId", unique = true, nullable = false)
    private String id;

    @Column(length = 50, nullable = false)
    private String password;

    @Column(name = "userName", nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(name = "createdDate", nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "modifiedDate", nullable = false)
    private LocalDateTime modifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == Status.Active;
    }
}
