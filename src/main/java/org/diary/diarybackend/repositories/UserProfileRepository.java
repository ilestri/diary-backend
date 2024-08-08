package org.diary.diarybackend.repositories;

import org.diary.diarybackend.entities.User_Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<User_Profile, Long> {
}
