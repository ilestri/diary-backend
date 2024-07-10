package org.diary.diarybackend.repositories;

import org.diary.diarybackend.entities.USERS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<USERS, Long> {
    Optional<USERS> findByEmail(String email);

}
