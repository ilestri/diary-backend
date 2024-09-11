package org.diary.diarybackend.services.Alarm;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diary.diarybackend.controllers.dtos.Alarm.GetAlarmResDTO;
import org.diary.diarybackend.entities.Notification;
import org.diary.diarybackend.repositories.NotificationRepository;
import org.diary.diarybackend.utils.UseToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GetAlameService {

    private final UseToken useToken;
    private final NotificationRepository notificationRepository;

    @Transactional
    public List<GetAlarmResDTO> GetAlame(String token) {
        Long userId;
        try {
            userId = Long.parseLong(useToken.getUserId(token));
        } catch (Exception e) {
            log.error("유효하지 않은 토큰: {}", e.getMessage());
            return null;
        }

        // 여러 알람을 가져옴
        List<Notification> notifications = notificationRepository.findByUserId(userId);

        if (notifications.isEmpty()) {
            log.error("알람이 없습니다.");
            return null;
        }

        // Notification 리스트를 GetAlarmResDTO 리스트로 변환
        return notifications.stream()
                .map(noti -> new GetAlarmResDTO(noti.getTitle(), noti.getMessage(),
                        noti.getIsRead(), noti.getTypeId()))
                .collect(Collectors.toList());
    }
}