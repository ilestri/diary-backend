package org.diary.diarybackend.services;

import java.util.Optional;
import org.diary.diarybackend.entities.User;
import org.diary.diarybackend.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // 서비스 클래스임을 나타내는 어노테이션
public class UserService {

    private final UsersRepository usersRepository;

    // 생성자 주입
    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    // 폰번호 저장
    public User saveUser(String phoneNumber) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        return usersRepository.save(user);
    }

    // 폰번호로 사용자 조회
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return usersRepository.findByPhoneNumber(phoneNumber);
    }
}
