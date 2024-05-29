package com.media_editor.tufng.service;

import com.media_editor.tufng.model.Session;
import com.media_editor.tufng.model.Video;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoService {

    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

    private final DBService dbService;
    private final FFService ffService;

    private final Random random = new Random();

    public VideoService(DBService dbService, FFService ffService) {
        this.dbService = dbService;
        this.ffService = ffService;
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

    public ResponseEntity<?> getVideoAsset(String videoId, String type) {
        dbService.update();
        Video video = dbService.getVideos().stream()
                .filter(v -> v.getVideoId().equals(videoId))
                .findFirst()
                .orElse(null);

        if (video == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"Video not found!\"}");
        }

        String filePath = null;
        String mimeType = null;
        String filename = null;

        switch (type) {
            case "thumbnail":
                filePath = "../resources/storage/" + videoId + "/thumbnail.jpg";
                mimeType = "image/jpeg";
                break;
            case "audio":
                filePath = "../resources/storage/" + videoId + "/audio.aac";
                mimeType = "audio/aac";
                filename = video.getName() + "-audio.aac";
                break;
            case "resize":
                String dimensions = ""; // You need to get the dimensions somehow
                filePath = "../resources/storage/" + videoId + "/" + dimensions + "." + video.getExtension();
                mimeType = "video/mp4"; // Not a good practice, as videos are not always MP4
                filename = video.getName() + "-" + dimensions + "." + video.getExtension();
                break;
            case "original":
                filePath = "../resources/storage/" + videoId + "/original." + video.getExtension();
                mimeType = "video/mp4"; // Not a good practice, as videos are not always MP4
                filename = video.getName() + "." + video.getExtension();
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\": \"Invalid type!\"}");
        }

        try {
            Path path = Paths.get(filePath);
            InputStream inputStream = new FileInputStream(path.toFile());
            byte[] data = Files.readAllBytes(path);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
            headers.add(HttpHeaders.CONTENT_TYPE, mimeType);
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"File not found!\"}");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Internal server error!\"}");
        }
    }


    public ResponseEntity<?> uploadVideo(String filename, String token, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"Missing video file!\"}");
        }

        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        String name = FilenameUtils.getBaseName(filename);
        String videoId = new BigInteger(130, random).toString(32);

        List<String> formatsSupported = Arrays.asList("mov", "mp4");

        if (!formatsSupported.contains(extension)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"Only these formats are allowed: mov, mp4\"}");
        }

        try {
            Files.createDirectories(Paths.get("../resources/storage/" + videoId));
            String fullPath = "../resources/storage/" + videoId + "/original." + extension;
            try {
                file.transferTo(Paths.get(fullPath));
            } catch (IOException e) {
                logger.error("Failed to write file", e);
                throw e;
            }

            // Make a thumbnail for the video file
            ffService.makeThumbnail(fullPath, "../resources/storage/" + videoId + "/thumbnail.jpg");

            // Get the dimensions
            Video.Dimensions dimensions = ffService.getDimensions(fullPath);

            // Update the database
            dbService.update();
            int id = dbService.getVideos().size() + 1;
            Optional<Session> session = dbService.getSessions().stream()
                    .filter(s -> s.getToken().equals(token))
                    .findFirst();
            if (!session.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"Invalid token!\"}");
            }
            int userId = session.get().getUserId();
            // Instantiate Video with userId
            Video video = new Video(id, videoId, name, extension, dimensions, userId, false, new HashMap<>());
            dbService.getVideos().add(video);
            dbService.save();

            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"status\": \"success\", \"message\": \"The file was uploaded successfully!\"}");
        } catch (IOException e) {
            // Delete the folder
//            deleteFolder("../resources/storage/" + videoId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Internal server error!\"}");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void deleteFolder(String path) {
        try {
            Files.walk(Paths.get(path))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            logger.error("Failed to delete folder", e);
        }
    }
}