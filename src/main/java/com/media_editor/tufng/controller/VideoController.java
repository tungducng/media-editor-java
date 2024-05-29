package com.media_editor.tufng.controller;

// UserController.java
import com.media_editor.tufng.model.Video;
import com.media_editor.tufng.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/videos")
    public ResponseEntity<List<Video>> getVideos(@CookieValue("token") String token) {
        return videoService.getVideos(token);
    }
}
