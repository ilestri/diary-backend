package org.diary.diarybackend.repositories;

import org.diary.diarybackend.entities.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
