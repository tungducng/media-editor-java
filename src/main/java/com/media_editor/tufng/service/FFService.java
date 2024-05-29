package com.media_editor.tufng.service;

import org.springframework.stereotype.Service;
import com.media_editor.tufng.model.Video.Dimensions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class FFService {

    public void makeThumbnail(String fullPath, String thumbnailPath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", fullPath, "-ss", "5", "-vframes", "1", thumbnailPath);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg exited with non-zero exit code: " + exitCode);
        }
    }

    public Dimensions getDimensions(String fullPath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("ffprobe", "-v", "error", "-select_streams", "v:0", "-show_entries", "stream=width,height", "-of", "csv=p=0", fullPath);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String dimensions = reader.readLine();
        int exitCode = process.waitFor();

        if (exitCode != 0 || dimensions == null) {
            throw new RuntimeException("FFprobe exited with non-zero exit code: " + exitCode);
        }

        String[] dimensionsArray = dimensions.split(",");
        if (dimensionsArray.length != 2) {
            throw new RuntimeException("Invalid dimensions format");
        }

        return new Dimensions(Integer.parseInt(dimensionsArray[0].trim()), Integer.parseInt(dimensionsArray[1].trim()));
    }
}