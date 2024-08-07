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
@Table(name = "relationships")
public class Relationship {

    @Id // 기본 키 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 전략 지정
    @Column(name = "id", unique = true, nullable = false) // 유니크, 널 불가 지정
    private Long id; // 관계 PK

    @Column(name = "my_id", nullable = false) // 널 불가 지정
    private Long myId; // 신청자 FK

    @Column(name = "partner_id", nullable = false) // 널 불가 지정
    private Long partnerId; // 파트너 FK

    @Column(name = "relation", length = 50, nullable = false) // 널 불가 지정
    private String relation; // 관계유형 (연인, 친구, 가족 등...)

    @CreationTimestamp // 생성 시점 자동 기록
    @Column(name = "start_date", nullable = false, updatable = false) // 널 불가, 수정 불가 지정
    private LocalDateTime startDate; // 관계가 시작된 날짜

    @Column(name = "diary_start_date") // 컬럼명 지정
    private LocalDateTime diaryStartDate; // 일기를 쓰기 시작한 날짜

    @Column(name = "diary_view_date") // 컬럼명 지정
    private LocalDateTime diaryViewDate; // 일기를 보기로 한 날짜

    @Column(name = "status", length = 50, nullable = false) // 널 불가 지정
    private String status; // 관계 상태 (PENDING-보류, ACCEPTED-허가, DECLINED-거절, ENDED-종료)

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정, 지연 로딩
    @JoinColumn(name = "my_id", insertable = false, updatable = false) // 조인 컬럼명 지정
    private User myUser; // 신청자 FK 관계

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정, 지연 로딩
    @JoinColumn(name = "partner_id", insertable = false, updatable = false) // 조인 컬럼명 지정
    private User partnerUser; // 파트너 FK 관계
}
