package org.diary.diarybackend.services.Relation;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diary.diarybackend.controllers.dtos.Relation.PushAlarmReqDTO;
import org.diary.diarybackend.entities.Notification;
import org.diary.diarybackend.entities.Relationship;
import org.diary.diarybackend.entities.User;
import org.diary.diarybackend.repositories.NotificationRepository;
import org.diary.diarybackend.repositories.RelationshipRepository;
import org.diary.diarybackend.repositories.UsersRepository;
import org.diary.diarybackend.utils.UseToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PushAlameService {

    private final UseToken useToken;
    private final UsersRepository usersRepository;
    private final RelationshipRepository relationshipRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public String PushAlame(PushAlarmReqDTO request, String token) {

        // 추후 예외 처리 할 때 다시 추가
        Long my_id;
        try {
            my_id = Long.parseLong(useToken.getUserId(token));
        } catch (Exception e) {
            return "유효하지 않은 토큰: " + e.getMessage();
        }

        Optional<User> myOpt = usersRepository.findById(my_id);

        String myName;
        if (myOpt.isPresent()) {
            myName = myOpt.get().getUsername();
        } else {
            log.error("사용자를 찾을 수 없습니다. phone_number: {}", request.getPhone_number());
            return "사용자를 찾을 수 없습니다. phone_number: " + request.getPhone_number();
        }

        Optional<User> partnerOpt = usersRepository.findByPhoneNumber(request.getPhone_number());

        Long partnerId;
        if (partnerOpt.isPresent()) {
            partnerId = partnerOpt.get().getId();
        } else {
            log.error("사용자를 찾을 수 없습니다. phone_number: {}", request.getPhone_number());
            return "사용자를 찾을 수 없습니다. phone_number: " + request.getPhone_number();
        }

        try {
            // 트랜잭션 내의 로직을 모두 포함
            Relationship relation = Relationship.builder()
                    .myId(my_id)
                    .partnerId(partnerId)
                    .relation("couple") // 일단 커플로 지정(추후 수정 예정)
                    .status("request") // 요청 상태로 지정
                    .build();

            relationshipRepository.save(relation);

            // 상대의 알람에 내용을 추가해야함 그 이후는 이후에 처리
            Notification noti = Notification.builder()
                    .userId(partnerId)
                    .typeId(1L)
                    .title("연인 추가 요청")
                    .message(myName + "님이 연인 추가 요청을 하였습니다.")
                    .isRead(false)
                    .build();

            notificationRepository.save(noti);
        } catch (Exception e) {
            // 예외가 발생하면 트랜잭션을 롤백하고, 에러 메시지 반환
            log.error("예외가 발생했습니다.", e);
            // 트랜잭션 롤백을 명시적으로 설정 (선택적, 기본적으로 @Transactional에 의해 처리됨)
            throw new RuntimeException("트랜잭션 오류: " + e.getMessage(), e);
        }

        return "SUCCESS";
    }
}
