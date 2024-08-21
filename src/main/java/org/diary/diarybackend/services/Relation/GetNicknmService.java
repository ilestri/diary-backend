package org.diary.diarybackend.services.Relation;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diary.diarybackend.controllers.dtos.Relation.GetNicknmResDTO;
import org.diary.diarybackend.entities.User;
import org.diary.diarybackend.entities.User_Profile;
import org.diary.diarybackend.repositories.UserProfileRepository;
import org.diary.diarybackend.repositories.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GetNicknmService {

    private final UsersRepository usersRepository;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public GetNicknmResDTO getNicknm(String phoneNumber) {

        // phone_number로 User 찾기
        Optional<User> userOpt = usersRepository.findByPhoneNumber(phoneNumber);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // User ID로 User_Profile 찾기
            Optional<User_Profile> userProfileOpt = userProfileRepository.findByUserId(user.getId());

            if (userProfileOpt.isPresent()) {
                User_Profile userProfile = userProfileOpt.get();

                // 찾은 정보를 DTO에 담아 반환
                return new GetNicknmResDTO(user.getId(), userProfile.getNickname(), userProfile.getProfilePicture());
            } else {
                log.error("프로필을 찾을 수 없습니다. user_id: {}", user.getId());
            }
        } else {
            log.error("사용자를 찾을 수 없습니다. phone_number: {}", phoneNumber);
        }

        return new GetNicknmResDTO(null, null, null);  // 유저 또는 프로필이 없을 경우 빈 객체 반환
    }
}
