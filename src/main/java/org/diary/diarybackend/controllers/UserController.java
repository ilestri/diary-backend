package org.diary.diarybackend.controllers;

import java.util.Optional;
import org.diary.diarybackend.entities.User;
import org.diary.diarybackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // 컨트롤러 클래스임을 나타내는 어노테이션
public class UserController {

    private final UserService userService;

    // 생성자 주입
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User phoneNumber) {
        User user = userService.saveUser(phoneNumber.getPhoneNumber());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/find")
    public ResponseEntity<User> getUserByPhoneNumber(@RequestParam String phoneNumber) {
        Optional<User> userOptional = userService.findByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}