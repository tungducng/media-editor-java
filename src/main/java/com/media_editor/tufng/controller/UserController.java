package com.media_editor.tufng.controller;

// UserController.java
import com.media_editor.tufng.model.User;
import com.media_editor.tufng.service.UserService;
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

    @DeleteMapping("/logout")
        public ResponseEntity<?> logOut(@CookieValue("token") String token) {
            return userService.logout(token);
    }

    @GetMapping("/user")
    public ResponseEntity<User> sendUserInfo(@CookieValue("token") String token) {
        return userService.sendUserInfo(token);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUserInfo(@CookieValue("token") String token, @RequestBody User user) {
        return userService.updateUser(token, user);
    }
//
//    @PostMapping("/signup")
//    public void signUp(@RequestBody User user) {
//        // Implement the logic for siging up a user
//    }
}
