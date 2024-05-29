package com.media_editor.tufng.controller;

// UserController.java
import com.media_editor.tufng.model.Video;
import com.media_editor.tufng.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/api/videos")
    public ResponseEntity<List<Video>> getVideos(@CookieValue("token") String token) {
        return videoService.getVideos(token);
    }

    @PostMapping("/api/upload-video")
    public ResponseEntity<?> uploadVideo(@RequestHeader("filename") String filename, @CookieValue("token") String token, @RequestParam("file") MultipartFile file) {
        return videoService.uploadVideo(filename, token, file);
    }

    @GetMapping("/get-video-asset")
    public ResponseEntity<?> getVideoAsset(@RequestParam String videoId, @RequestParam String type) {
        return videoService.getVideoAsset(videoId, type);
    }
}
