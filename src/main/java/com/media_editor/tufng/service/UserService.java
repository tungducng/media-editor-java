package com.media_editor.tufng.service;

// UserService.java
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.media_editor.tufng.dto.LoginResponse;
import com.media_editor.tufng.model.Session;
import com.media_editor.tufng.model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

// UserService.java
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


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

        Optional<User> user = dbService.getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            logger.info("User authenticated: {}", username);
            // Generate a random 10 digit token
            String token = String.format("%010d", random.nextInt(1000000000));

            // Save the generated token
            // dbService.getSessions().add(new Session(user.get().getId(), token));
            // dbService.save();

            // Set the token in the response header
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", "token=" + token + "; Path=/; HttpOnly; Secure");

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


    public User getUserById(int id) {
        return dbService.getUsers().stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }



    // Implement other user-related operations...
}