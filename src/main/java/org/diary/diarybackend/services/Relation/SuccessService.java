package org.diary.diarybackend.services.Relation;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diary.diarybackend.controllers.dtos.Relation.PushAlarmReqDTO;
import org.diary.diarybackend.entities.User;
import org.diary.diarybackend.repositories.UserProfileRepository;
import org.diary.diarybackend.repositories.UsersRepository;
import org.diary.diarybackend.utils.UseToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SuccessService {

    private final UseToken useToken;
    private final UsersRepository usersRepository;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public String PushAlame(PushAlarmReqDTO request, String token) {

        // 요청자의 아이디
        String my_id;
        try {
            my_id = useToken.getUserId(token);
        } catch (Exception e) {
            return "유효하지 않은 토큰: " + e.getMessage();
        }

        Optional<User> userOpt = usersRepository.findByPhoneNumber(request.getPhone_number());

        if (userOpt.isPresent()) {
            Long user = userOpt.get().getId();
        } else {
            log.error("사용자를 찾을 수 없습니다. phone_number: {}", request.getPhone_number());
            return "사용자를 찾을 수 없습니다. phone_number: " + request.getPhone_number();
        }

//        Relationship Relation = User.builder()
//                .myId(signUpReqDto.getUsername())
//                .partnerId(signUpReqDto.getPhone_number())
//                .relation(signUpReqDto.getEmail())
//                .password(encodedPassword)
//                .birthdate(signUpReqDto.getBirthdate())
//                .build();

        return "SUCCESS";
    }
}
