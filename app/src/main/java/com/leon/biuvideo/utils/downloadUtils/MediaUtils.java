package com.leon.biuvideo.utils.downloadUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.views.GeneralNotification;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Random;

import io.microshow.rxffmpeg.RxFFmpegInvoke;

/**
 * 下载媒体资源(视频/音频)
 */
public class MediaUtils {
    private final Context context;
    private final int tag;
    private String mainId;
    private long subId = 0;
    private int qualityId = 0;

    private GeneralNotification generalNotification;

    public MediaUtils(Context context, String mainId, long subId, int qualityId) {
        this.context = context;
        this.mainId = mainId;
        this.subId = subId;
        this.qualityId = qualityId;
        this.tag = new Random().nextInt(1001);
    }

    public MediaUtils(Context context, String mainId) {
        this.context = context;
        this.mainId = mainId;
        this.tag = new Random().nextInt(1001);
    }

    /**
     * 获取、合并视频音频
     *
     * @param videoPath 视频路径
     * @param audioPath 音频路径
     * @param fileName   文件名称
     */
    public void saveVideo(String videoPath, String audioPath, String fileName) {
        // 设置downloadState状态为“正在下载”状态
        setDownloadState(fileName, false);
        setNotificationState(fileName, false, true);

        // 先对视频、音频进行缓存，两个文件均保存至`Temp`文件夹
        SaveMediaUtils saveMediaUtils = new SaveMediaUtils();
        saveMediaUtils.setOnDownloadMediaListener(new SaveMediaUtils.OnDownloadMediaListener() {
            @Override
            public void onDownloadFailed() {
                generalNotification.setNotificationOnSDK26("缓存视频", "缓存失败，正在重试中...\t" + fileName, R.drawable.notification_biu_video);
                String[] strings = HttpUtils.reacquireMediaUrl(context, mainId, subId, qualityId, !videoPath.startsWith("BV"));
                setNotificationState(fileName, false, true);
                saveMediaUtils.saveMedia(strings[0], strings[1], fileName);
            }

            @Override
            public void onDownloadSuccess(String videoTempPath, String audiTempPath) {
                // 如果视频、音频均下载完成就进行合成
                if (videoTempPath != null && audiTempPath != null) {
                    mergeVideo(videoTempPath, audiTempPath, fileName);
                }
            }

            @Override
            public void onDownloading(int progress) {

            }
        });
        saveMediaUtils.saveMedia(videoPath, audioPath, fileName);
    }

    /**
     * 合并音视频
     *
     * @param videoTempPath     视频临时路径
     * @param audioTempPath     音频临时路径
     * @param fileName      文件名称
     */
    private void mergeVideo(String videoTempPath, String audioTempPath, String fileName) {
        String folderPath = FileUtils.createFolder(FileUtils.ResourcesFolder.VIDEOS);
        String outPath = folderPath + "/" + fileName + ".mp4";

        // 生成ffmpeg命令
        String command = "ffmpeg -i " + videoTempPath + " -i " + audioTempPath + " -vcodec copy -acodec copy " + outPath;
        RxFFmpegInvoke.getInstance().runCommand(command.split(" "), new RxFFmpegInvoke.IFFmpegListener() {
            @Override
            public void onFinish() {
                Fuck.blue("Done.");
                //通知刷新，进行显示
                ResourceUtils.sendBroadcast(context, new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(outPath)));

                // 设置downloadState状态为“已下载完成”状态
                setDownloadState(fileName, true);
                setNotificationState(fileName, true, true);

                deleteTemp(videoTempPath, audioTempPath);
            }

            @Override
            public void onProgress(int progress, long progressTime) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String message) {
                Fuck.blue("淦，出错了！\n" + message);
                deleteTemp(videoTempPath, audioTempPath);
                generalNotification.setNotificationOnSDK26("缓存视频", "缓存失败\t" + fileName, R.drawable.notification_biu_video);
                setDownloadFailState(fileName);
            }
        });
    }

    /**
     * 删除音视频的临时文件
     *
     * @param videoTempPath     临时视频文件路径
     * @param audioTempPath     临时音频文件路径
     */
    private void deleteTemp(String videoTempPath, String audioTempPath) {
        File videoTempFile = new File(videoTempPath);
        if (videoTempFile.exists()) {
            videoTempFile.delete();
        }

        File audioTempFile = new File(audioTempPath);
        if (audioTempFile.exists()) {
            audioTempFile.delete();
        }
    }

    /**
     * 保存音乐
     *
     * @param resourceUrl  资源链接
     * @param fileName  文件名称
     * @return  返回保存状态
     */
    public boolean saveMusic(String resourceUrl, String fileName) {
        try {
            URL url = new URL(resourceUrl);

            URLConnection urlConnection = url.openConnection();

            //设置头信息
            HashMap<String, String> headers = HttpUtils.getHeaders();
            urlConnection.setRequestProperty("User-Agent", headers.get("User-Agent"));
            urlConnection.setRequestProperty("Referer", headers.get("Referer"));

            urlConnection.connect();

            // 设置downloadState状态为“正在下载”状态
            setDownloadState(fileName, false);
            setNotificationState(fileName, false, false);

//            size = urlConnection.getContentLength();

            BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());

            //创建musicFile对象
            File musicFile = new File(FileUtils.createFolder(FileUtils.ResourcesFolder.MUSIC), fileName + ".mp3");

            FileOutputStream fileOutputStream = new FileOutputStream(musicFile);

            byte[] bytes = new byte[1024 * 10];

//            currentSize = 0;
            int len;
            while ((len = bufferedInputStream.read(bytes)) != -1) {
//                currentSize += len;
//                currentProgress = (int) (size * 100 / this.size);

                fileOutputStream.write(bytes, 0, len);
            }

            fileOutputStream.close();
            bufferedInputStream.close();

            //发送广播，通知刷新显示
            ResourceUtils.sendBroadcast(context, new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(musicFile)));

            // 设置downloadState状态为“已完成下载”状态
            setDownloadState(fileName, true);
            setNotificationState(fileName, true, false);

            return true;
        } catch (IOException e) {
            if (generalNotification == null) {
                generalNotification = new GeneralNotification(context, "缓存媒体资源", this.tag);
            }
            generalNotification.setNotificationOnSDK26("缓存音频", "缓存失败\t" + fileName, R.drawable.notification_biu_video);
            setDownloadFailState(fileName);

            e.printStackTrace();
        }

        return false;
    }

    /**
     * 设置下载条目的状态
     *
     * @param fileName  文件名称
     * @param isComplete    是否已下载完毕
     */
    private void setDownloadState(String fileName, boolean isComplete) {
        DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = new DownloadRecordsDatabaseUtils(context);

        // 将已存在的条目的isDelete设置为0
        downloadRecordsDatabaseUtils.setDelete(fileName, false);

        if (isComplete) {
            downloadRecordsDatabaseUtils.setComplete(fileName);
        } else {
            downloadRecordsDatabaseUtils.setDownloading(fileName);
        }

        downloadRecordsDatabaseUtils.close();
    }

    /**
     * 设置下载条目的状态为失败状态
     *
     * @param fileName  文件名称
     */
    private void setDownloadFailState(String fileName) {
        DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = new DownloadRecordsDatabaseUtils(context);

        downloadRecordsDatabaseUtils.setFailed(fileName);

        downloadRecordsDatabaseUtils.close();
    }

    /**
     * 创建通知栏
     *
     * @param fileName  文件名称
     * @param isComplete    是否已下载完毕
     * @param isVideo   是否为video
     */
    private void setNotificationState(String fileName, boolean isComplete, boolean isVideo) {
        if (!isComplete) {
            generalNotification = new GeneralNotification(context, "缓存媒体资源", this.tag);
            generalNotification.setNotificationOnSDK26(isVideo ? "缓存视频" : "缓存音频", "正在缓存\t" + fileName, R.drawable.notification_biu_video);
        } else {
            GeneralNotification.cancel(tag);

            // 发送本地广播
            sendLocalBroadcast(fileName, isVideo);
        }
    }

    /**
     * 下载完毕后发送本地广播
     *
     * @param fileName  文件名称
     */
    private void sendLocalBroadcast(String fileName, boolean isVideo) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);

        Intent mediaIntent;
        if (isVideo) {
            mediaIntent = new Intent("DownloadVideo");
        } else {
            mediaIntent = new Intent("DownloadAudio");
        }

        mediaIntent.putExtra("fileName", fileName);
        localBroadcastManager.sendBroadcast(mediaIntent);
    }
}
