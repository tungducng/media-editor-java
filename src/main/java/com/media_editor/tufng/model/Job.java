package com.media_editor.tufng.model;

public class Job {
    private String type;
    private String videoId;
    private int width;
    private int height;

    public Job(String type, String videoId, int width, int height) {
        this.type = type;
        this.videoId = videoId;
        this.width = width;
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
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