package org.diary.diarybackend.services.Relation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diary.diarybackend.controllers.dtos.Relation.PushAlarmReqDTO;
import org.diary.diarybackend.repositories.UserProfileRepository;
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
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public String PushAlame(PushAlarmReqDTO request, String token) {

        // 요청자의 아이디
        String my_id;
        try {
            my_id = useToken.getUserId(token);
        } catch (Exception e) {
            return "Invalid token: " + e.getMessage();
        }

        return "SUCCESS";
    }
}
