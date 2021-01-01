package com.leon.biuvideo.utils;

import android.content.Context;
import android.util.Log;

import com.leon.biuvideo.utils.downloadUtils.MediaUtils;

import java.util.concurrent.Callable;

public class SimpleDownloadThread implements Callable<String> {
    private Context context;
    private String videoPath;
    private String audioPath;
    private String fileName;

    private MediaUtils mediaUtils;

    public SimpleDownloadThread(Context context, String videoPath, String audioPath, String fileName) {
        this.context = context;
        this.videoPath = videoPath;
        this.audioPath = audioPath;
        this.fileName = fileName;
    }

    public SimpleDownloadThread(Context context, String audioPath, String fileName) {
        this.context = context;
        this.audioPath = audioPath;
        this.fileName = fileName;
    }

    @Override
    public String call() {
        if (mediaUtils == null) {
            mediaUtils = new MediaUtils(context);
        }
        Log.d("SimpleThreadPool", "call:" + fileName);
        if (videoPath != null) {
            mediaUtils.saveVideo(videoPath, audioPath, fileName);
        } else {
            mediaUtils.saveMusic(audioPath, fileName);
        }

        return null;
    }
}