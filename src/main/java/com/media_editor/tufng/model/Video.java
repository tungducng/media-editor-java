package com.media_editor.tufng.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Video {
    private int id;
    private String videoId;
    private String name;
    private String extension;
    private Dimensions dimensions;
    private int userId;
    private Map<String, Resize> resizes;
    private boolean extractedAudio;

    public Video(int id, String videoId, String name, String extension, Dimensions dimensions, int userId, boolean extractedAudio, Map<String, Resize> resizes) {
        this.id = id;
        this.videoId = videoId;
        this.name = name;
        this.extension = extension;
        this.dimensions = dimensions;
        this.userId = userId;
        this.extractedAudio = extractedAudio;
        this.resizes = resizes;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isExtractedAudio() {
        return extractedAudio;
    }

    public void setExtractedAudio(boolean extractedAudio) {
        this.extractedAudio = extractedAudio;
    }

    public Map<String, Resize> getResizes() {
        return resizes;
    }

    public void setResizes(Map<String, Resize> resizes) {
        this.resizes = resizes;
    }


    public static class Resize {
        private boolean processing;

        @JsonCreator
        public Resize(@JsonProperty("processing") boolean processing) {
            this.processing = processing;
        }

        public boolean isProcessing() {
            return processing;
        }

        public void setProcessing(boolean processing) {
            this.processing = processing;
        }
    }

    public static class Dimensions {
        private int width;
        private int height;

        public Dimensions() {
        }

        public Dimensions(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}





