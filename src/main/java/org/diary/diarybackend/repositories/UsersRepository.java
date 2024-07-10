package org.diary.diarybackend.repositories;

import org.diary.diarybackend.entities.USERS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<USERS, Long> {
    Optional<USERS> findById(Long id);
    Optional<USERS> findByEmail(String email);
    boolean existsByEmail(String email);
}
