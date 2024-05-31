package com.media_editor.tufng.service;

import com.media_editor.tufng.model.Job;
import com.media_editor.tufng.model.Video;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class JobQueueService {
    private static final String UPLOADED_FOLDER = "src/main/resources/uploaded_videos";
    private final DBService dbService;
    private final FFService ffService;
    private final UtilService utilService;
    private final Queue<Job> jobs;
    private Job currentJob;

    public JobQueueService(DBService dbService, FFService ffService, UtilService utilService) {
        this.jobs = new LinkedList<>();
        this.currentJob = null;
        this.dbService = dbService;
        this.ffService = ffService;
        this.utilService = utilService;

        // Loop through the videos and find all the processing true items, and
        // add them to the queue (enqueue)
        dbService.update();
        for (Video video : dbService.getVideos()) {
            for (String key : video.getResizes().keySet()) {
                Video.Resize resize = video.getResizes().get(key);
                if (resize.isProcessing()) {
                    String[] dimensions = key.split("x");
                    int width = Integer.parseInt(dimensions[0]);
                    int height = Integer.parseInt(dimensions[1]);
                    this.enqueue(new Job("resize", video.getVideoId(), width, height));
                }
            }
        }
    }

    public void enqueue(Job job) {
        this.jobs.add(job);
        this.executeNext();
    }

    public Job dequeue() {
        return this.jobs.poll();
    }

    public void executeNext() {
        if (this.currentJob != null) return;
        this.currentJob = this.dequeue();
        if (this.currentJob == null) return;
        this.execute(this.currentJob);
    }

    public void execute(Job job) {
        if (job.getType().equals("resize")) {
            String videoId = job.getVideoId();
            int width = job.getWidth();
            int height = job.getHeight();

            dbService.update();
            Video video = dbService.getVideos().stream()
                    .filter(v -> v.getVideoId().equals(videoId))
                    .findFirst()
                    .orElse(null);

            String originalVideoPath = UPLOADED_FOLDER + "/" + video.getVideoId() + "/original." + video.getExtension();
            String targetVideoPath = UPLOADED_FOLDER + "/" + video.getVideoId() + "/" + width + "x" + height + "." + video.getExtension();

            try {
                ffService.resize(originalVideoPath, targetVideoPath, width, height);

                dbService.update();
                video = dbService.getVideos().stream()
                        .filter(v -> v.getVideoId().equals(videoId))
                        .findFirst()
                        .orElse(null);
                video.getResizes().get(width + "x" + height).setProcessing(false);
                dbService.save();

                System.out.println("Done resizing! Number of jobs remaining: " + this.jobs.size());
            } catch (Exception e) {
                utilService.deleteFile(targetVideoPath);
            }
        }

        this.currentJob = null;
        this.executeNext();
    }
}