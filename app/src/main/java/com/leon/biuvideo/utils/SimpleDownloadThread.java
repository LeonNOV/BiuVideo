package com.leon.biuvideo.utils;

import android.content.Context;
import android.util.Log;

import com.leon.biuvideo.utils.downloadUtils.MediaUtils;

import java.util.concurrent.Callable;

public class SimpleDownloadThread implements Callable<String> {
    private final Context context;
    private final String mainId;
    private long subId = 0;
    private int qualityId = 0;
    private final String audioPath;
    private final String fileName;

    private String videoPath;

    public SimpleDownloadThread(Context context, String mainId, long subId, int qualityId, String videoPath, String audioPath, String fileName) {
        this.context = context;
        this.mainId = mainId;
        this.subId = subId;
        this.qualityId = qualityId;
        this.videoPath = videoPath;
        this.audioPath = audioPath;
        this.fileName = fileName;
    }

    public SimpleDownloadThread(Context context, String mainId, String audioPath, String fileName) {
        this.context = context;
        this.mainId = mainId;
        this.audioPath = audioPath;
        this.fileName = fileName;
    }

    @Override
    public String call() {
        MediaUtils mediaUtils;

        Log.d("SimpleThreadPool", "call:" + fileName);

        if (videoPath != null) {
            mediaUtils = new MediaUtils(context, mainId, subId, qualityId);
            mediaUtils.saveVideo(videoPath, audioPath, fileName);
        } else {
            mediaUtils = new MediaUtils(context, mainId);
            mediaUtils.saveMusic(audioPath, fileName);
        }

        return null;
    }
}