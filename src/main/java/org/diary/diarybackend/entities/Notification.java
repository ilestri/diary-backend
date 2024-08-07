package org.diary.diarybackend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity // JPA 엔티티로 지정
@Data // Lombok: getter, setter, toString, equals, hashCode 메서드 자동 생성
@NoArgsConstructor // Lombok: 기본 생성자 자동 생성
@AllArgsConstructor // Lombok: 모든 필드를 포함한 생성자 자동 생성
@Builder // Lombok: 빌더 패턴으로 객체 생성
@Table(name = "notifications")
public class Notification {

    @Id // 기본 키 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 전략 지정
    @Column(name = "id", unique = true, nullable = false) // 유니크, 널 불가 지정
    private Long id; // 알람 PK

    @Column(name = "user_id", nullable = false) // 널 불가 지정
    private Long userId; // 유저 FK

    @Column(name = "type_id", nullable = false) // 널 불가 지정
    private Long typeId; // 알람 타입 FK

    @Column(name = "title", length = 255, nullable = false) // 널 불가 지정
    private String title; // 알람 제목

    @Column(name = "message", nullable = false) // 널 불가 지정
    private String message; // 알람 내용

    @Column(name = "is_read", nullable = false) // 널 불가 지정
    private Boolean isRead = false; // 확인 유무

    @CreationTimestamp // 생성 시점 자동 기록
    @Column(name = "created_at", nullable = false, updatable = false) // 널 불가, 수정 불가 지정
    private LocalDateTime createdAt; // 생성 시간

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정, 지연 로딩
    @JoinColumn(name = "user_id", insertable = false, updatable = false) // 조인 컬럼명 지정
    private User user; // 유저 테이블과의 FK 관계

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정, 지연 로딩
    @JoinColumn(name = "type_id", insertable = false, updatable = false) // 조인 컬럼명 지정
    private Notification_Type notificationType; // 알람 타입 테이블과의 FK 관계
}
