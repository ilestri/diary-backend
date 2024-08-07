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
@Table(name = "notification_types")
public class Notification_Type {

    @Id // 기본 키 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 전략 지정
    @Column(name = "id", unique = true, nullable = false) // 유니크, 널 불가 지정
    private Long id; // 타입 PK

    @Column(name = "type_name", length = 50, nullable = false) // 길이, 널 불가 지정
    private String typeName; // 타입 이름
}
