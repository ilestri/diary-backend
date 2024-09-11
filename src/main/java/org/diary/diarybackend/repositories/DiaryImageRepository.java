package org.diary.diarybackend.repositories;

import org.diary.diarybackend.entities.Diary_Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryImageRepository extends JpaRepository<Diary_Image, Long> {
}
