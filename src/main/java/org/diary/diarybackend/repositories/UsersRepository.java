package org.diary.diarybackend.repositories;

import org.diary.diarybackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id); // ID로 유저 찾기

    Optional<User> findByEmail(String email); // 이메일로 유저 찾기

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email); // 이메일 존재 여부 확인
}
