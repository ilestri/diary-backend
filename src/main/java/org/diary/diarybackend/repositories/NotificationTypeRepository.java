package org.diary.diarybackend.repositories;

import org.diary.diarybackend.entities.Notification_Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTypeRepository extends JpaRepository<Notification_Type, Long> {
    // 추가적인 사용자 정의 쿼리 메소드가 필요한 경우 여기에 작성합니다.
}
