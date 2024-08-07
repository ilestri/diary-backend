package org.diary.diarybackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // JPA 엔티티로 지정
@Data // Lombok: getter, setter, toString, equals, hashCode 메서드 자동 생성
@NoArgsConstructor // Lombok: 기본 생성자 자동 생성
@AllArgsConstructor // Lombok: 모든 필드를 포함한 생성자 자동 생성
@Builder // Lombok: 빌더 패턴으로 객체 생성
@Table(name = "user_profiles")
public class User_Profile {

    @Id // 기본 키 지정
    @Column(name = "user_id")
    private Long userId; // 유저 FK

    @Column(name = "nickname", length = 50, nullable = false) // 널 불가 지정
    private String nickname; // 닉네임

    @Column(name = "bio")
    private String bio; // 사용자 소개

    @Column(name = "profile_picture")
    private String profilePicture; // 프로필 사진 URL

    @OneToOne(fetch = FetchType.LAZY) // 일대일 관계 설정, 지연 로딩
    @MapsId // User 엔티티의 기본 키를 공유
    @JoinColumn(name = "user_id") // 조인 컬럼명 지정
    private User user; // 유저 테이블과의 FK
}
