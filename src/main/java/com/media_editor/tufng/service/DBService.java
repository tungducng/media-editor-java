package com.media_editor.tufng.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.media_editor.tufng.model.Session;
import com.media_editor.tufng.model.User;
import com.media_editor.tufng.model.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DBService {

    private static final Logger logger = LoggerFactory.getLogger(DBService.class);
    private static final String USERS_PATH = "src/main/resources/data/users.json";
    private static final String SESSIONS_PATH = "src/main/resources/data/sessions.json";
    private static final String VIDEOS_PATH = "src/main/resources/data/videos.json";

    private List<User> users;
    private List<Session> sessions;
    private List<Video> videos;

    private final ObjectMapper objectMapper;

    public DBService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        update();
    }

    public void update() {
        this.users = readFromFile(USERS_PATH, User.class);
        this.sessions = readFromFile(SESSIONS_PATH, Session.class);
        this.videos = readFromFile(VIDEOS_PATH, Video.class);
    }

    public void save() {
        writeToFile(USERS_PATH, users);
        writeToFile(SESSIONS_PATH, sessions);
        writeToFile(VIDEOS_PATH, videos);
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public List<Video> getVideos() {
        return videos;
    }

    private <T> List<T> readFromFile(String path, Class<T> clazz) {
        try {
            return objectMapper.readValue(Files.readAllBytes(Paths.get(path)), objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            logger.error("Failed to read from file: " + path, e);
            return List.of();  // Return an empty list instead of null
        }
    }

    private <T> void writeToFile(String path, List<T> data) {
        try {
            Files.write(Paths.get(path), objectMapper.writeValueAsBytes(data));
        } catch (Exception e) {
            logger.error("Failed to write to file: " + path, e);
        }
    }
}
