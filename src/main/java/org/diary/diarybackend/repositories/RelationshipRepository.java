package org.diary.diarybackend.repositories;

import org.diary.diarybackend.entities.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {

}
