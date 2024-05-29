package com.media_editor.tufng.service;

import com.media_editor.tufng.model.Session;
import com.media_editor.tufng.model.User;
import com.media_editor.tufng.model.Video;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;

@Service
public class VideoService {

    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);
    private final DBService dbService;
    private final Random random = new Random();

    public VideoService(DBService dbService) {
        this.dbService = dbService;
    }


    public ResponseEntity<List<Video>> getVideos(String token) {
        dbService.update();
        Optional<Session> session = dbService.getSessions().stream()
                .filter(s -> s.getToken().equals(token))
                .findFirst();

        if (session.isPresent()) {
            List<Video> videos = dbService.getVideos().stream()
                    .filter(v -> v.getUserId() == session.get().getUserId())
                    .collect(Collectors.toList());
            if (!videos.isEmpty()) {
                return ResponseEntity.ok(videos);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}