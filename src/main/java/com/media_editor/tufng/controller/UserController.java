package com.media_editor.tufng.controller;

// UserController.java
import com.media_editor.tufng.dto.LoginResponse;
import com.media_editor.tufng.model.User;
import com.media_editor.tufng.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> logIn(@RequestBody User user) {
        return userService.login(user.getUsername(), user.getPassword());
    }
//
//    @PostMapping("/logout")
//    public void logOut() {
//        // Implement the logic for logging out a user
//    }
//
//    @GetMapping("/user")
//    public void getUserInfo() {
//        // Implement the logic for getting user info
//    }
//
//    @PutMapping("/user")
//    public void updateUserInfo(@RequestBody User user) {
//        // Implement the logic for updating user info
//    }
//
//    @PostMapping("/signup")
//    public void signUp(@RequestBody User user) {
//        // Implement the logic for siging up a user
//    }
}
