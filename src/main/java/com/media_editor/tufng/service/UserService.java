package com.media_editor.tufng.service;

import com.media_editor.tufng.model.Session;
import com.media_editor.tufng.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final DBService dbService;
    private final Random random = new Random();

    public UserService(DBService dbService) {
        this.dbService = dbService;
    }

    public ResponseEntity<?> login(String username, String password) {
        logger.info("Attempting to log in user: {}", username);

        dbService.update();
        Optional<User> user = dbService.getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            logger.info("User authenticated: {}", username);
            // Generate a random 10 digit token
            String token = String.format("%010d", random.nextInt(1000000000));

//             Save the generated token
            dbService.getSessions().add(new Session(user.get().getId(), token));
            dbService.save();

            // Set the token in the response header
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", "token=" + token + "; Path=/;");

            // Return a JSON response with a success message
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .body("{\"message\": \"Logged in successfully!\"}");
        } else {
            logger.warn("Invalid username or password for user: {}", username);
            // Return a JSON response with an error message and 401 Unauthorized status
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"Invalid username or password\"}");
        }
    }

    public ResponseEntity<User> sendUserInfo(String token) {
        dbService.update();
        Optional<Session> session = dbService.getSessions().stream()
                .filter(s -> s.getToken().equals(token))
                .findFirst();

        if (session.isPresent()) {
            Optional<User> user = dbService.getUsers().stream()
                    .filter(u -> u.getId() == session.get().getUserId())
                    .findFirst();

            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public ResponseEntity<?> logout(String token) {
        dbService.update();
        Optional<Session> session = dbService.getSessions().stream()
                .filter(s -> s.getToken().equals(token))
                .findFirst();

        if (session.isPresent()) {
            dbService.getSessions().remove(session.get());
            dbService.save();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", "token=deleted; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body("{\"message\": \"Logged out successfully!\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"Invalid session token\"}");
        }
    }

    public ResponseEntity<?> updateUser(String token, User updatedUser) {
        dbService.update();
        Optional<Session> session = dbService.getSessions().stream()
                .filter(s -> s.getToken().equals(token))
                .findFirst();

        if (session.isPresent()) {
            Optional<User> user = dbService.getUsers().stream()
                    .filter(u -> u.getId() == session.get().getUserId())
                    .findFirst();

            if (user.isPresent()) {
                user.get().setUsername(updatedUser.getUsername());
                user.get().setName(updatedUser.getName());

                // Only update the password if it is provided
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    user.get().setPassword(updatedUser.getPassword());
                }

                dbService.save();

                return ResponseEntity.ok()
                        .body("{\"username\": \"" + user.get().getUsername() + "\", \"name\": \"" + user.get().getName() + "\", \"password_status\": \"" + (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty() ? "updated" : "not updated") + "\"}");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public User getUserById(int id) {
        return dbService.getUsers().stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }
}