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
@Table(name = "diary_images")
public class Diary_Image {

    @Id // 기본 키 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 전략 지정
    @Column(name = "id", unique = true, nullable = false) // 유니크, 널 불가 지정
    private Long id; // 이미지 PK

    @Column(name = "diary_id", nullable = false) // 널 불가 지정
    private Long diaryId; // 일기 FK

    @Column(name = "image_url", nullable = false) // 널 불가 지정
    private String imageUrl; // 이미지 URL

    @CreationTimestamp // 생성 시점 자동 기록
    @Column(name = "created_date", nullable = false, updatable = false) // 널 불가, 수정 불가 지정
    private LocalDateTime createdDate; // 이미지 업로드 시간

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정, 지연 로딩
    @JoinColumn(name = "diary_id", insertable = false, updatable = false) // 조인 컬럼명 지정
    private Diary diary; // 일기 테이블과의 FK 관계
}
