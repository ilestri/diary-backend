package org.diary.diarybackend.repositories;

import org.diary.diarybackend.entities.Relationship_Status_History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipStatusHistoryRepository extends JpaRepository<Relationship_Status_History, Long> {
}
