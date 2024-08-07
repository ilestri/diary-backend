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
@Table(name = "relationship_status_history")
public class Relationship_Status_History {

    @Id // 기본 키 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 전략 지정
    @Column(name = "id", unique = true, nullable = false) // 유니크, 널 불가 지정
    private Long id; // 이력 PK

    @Column(name = "relationship_id", nullable = false) // 널 불가 지정
    private Long relationshipId; // 관계 FK

    @Column(name = "status", length = 50, nullable = false) // 널 불가 지정
    private String status; // 상태

    @CreationTimestamp // 생성 시점 자동 기록
    @Column(name = "changed_at", nullable = false, updatable = false) // 널 불가, 수정 불가 지정
    private LocalDateTime changedAt; // 상태 변경 시간

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정, 지연 로딩
    @JoinColumn(name = "relationship_id", insertable = false, updatable = false) // 조인 컬럼명 지정
    private Relationship relationship; // 관계 테이블과의 FK
}
