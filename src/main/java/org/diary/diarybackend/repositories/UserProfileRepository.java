package org.diary.diarybackend.repositories;

import org.diary.diarybackend.entities.User_Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<User_Profile, Long> {

    // user_id로 프로필 찾기
    Optional<User_Profile> findByUserId(Long userId);
}
