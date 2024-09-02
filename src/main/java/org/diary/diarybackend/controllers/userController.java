package org.diary.diarybackend.controllers;

import org.diary.diarybackend.entities.User;
import org.diary.diarybackend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public class userController {
    private UserService userService;
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

