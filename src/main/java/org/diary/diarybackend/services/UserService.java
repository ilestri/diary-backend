package org.diary.diarybackend.services;

import org.diary.diarybackend.entities.User;
import org.diary.diarybackend.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    //폰번호저장
    public User saveUser(String phoneNumber) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        return usersRepository.save(user);
    }
    //폰 번호 조회
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return usersRepository.findByPhoneNumber(phoneNumber);
    }
    //뭘 더 추가해야하는가?
}
