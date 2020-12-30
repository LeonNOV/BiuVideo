package com.leon.biuvideo.utils.downloadUtils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.net.Uri;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.videoBean.play.Play;
import com.leon.biuvideo.ui.views.GeneralNotification;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.MediaParseUtils;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.MusicUrlParseUtils;
import com.leon.biuvideo.values.Tables;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MediaUtils {
    private final Context context;
    private final int tag;

    private GeneralNotification generalNotification;

    public MediaUtils(Context context) {
        this.context = context;
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
        String folderPath = FileUtils.createFolder(FileUtils.ResourcesFolder.VIDEOS);

        String outPath = folderPath + "/" + fileName + ".mp4";

        Fuck.blue("VideoUrl:" + videoPath);
        Fuck.blue("AudioUrl:" + audioPath);

        long begin = System.currentTimeMillis();

        // 设置downloadState状态为“正在下载”状态
        setDownloadState(fileName, false);
        setNotificationState(fileName, false, true);

        MediaExtractor videoExtractor = null;
        MediaExtractor audioExtractor = null;

        try {
            //设置头信息
            HashMap<String, String> headers = HttpUtils.getHeaders();

            //创建视频Extractor
            videoExtractor = getVideoExtractor(videoPath, headers);
            MediaFormat videoFormat = videoExtractor.getTrackFormat(0);
            videoExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);

            //创建音频Extractor
            audioExtractor = getAudioExtractor(audioPath, headers);
            MediaFormat audioFormat = audioExtractor.getTrackFormat(0);
            audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);

            //设置输出配置
            MediaMuxer mediaMuxer = new MediaMuxer(outPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int videoTrack = mediaMuxer.addTrack(videoFormat);
            int audioTrack = mediaMuxer.addTrack(audioFormat);

            /*
             * 设置缓冲区大小
             *
             * 出现：A/libc: Fatal signal 11 (SIGSEGV), code 1 (SEGV_MAPERR),
             *      fault addr 0xc in tid 2843 (Thread-12), pid 2430 (m.leon.biuvideo)
             * 这样的错误可能是缓冲区太小的缘故，设置sampleSize的大小即可
             * */
            int sampleSize = 1024 * 1000;
            ByteBuffer videoBuffer = ByteBuffer.allocate(sampleSize);
            ByteBuffer audioBuffer = ByteBuffer.allocate(sampleSize);

            MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo audioBufferInfo = new MediaCodec.BufferInfo();

            mediaMuxer.start();

            boolean sawEOS_video = false;
            int frameCount_video = 0;
            int offset = 0;
            while (!sawEOS_video) {
                videoBufferInfo.offset = offset;
                videoBufferInfo.size = videoExtractor.readSampleData(videoBuffer, offset);

                if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
                    sawEOS_video = true;
                    videoBufferInfo.size = 0;
                } else {
                    videoBufferInfo.presentationTimeUs = videoExtractor.getSampleTime();
                    videoBufferInfo.flags = videoExtractor.getSampleFlags();
                    mediaMuxer.writeSampleData(videoTrack, videoBuffer, videoBufferInfo);
                    videoExtractor.advance();
                    frameCount_video++;
                }

//                size += videoBufferInfo.size;
//                currentSize = size;
//                currentProgress = (int) (size * 100 / this.size);
            }

            boolean sawEOS_audio = false;
            int frameCount_audio = 0;
            while (!sawEOS_audio) {
                audioBufferInfo.offset = offset;
                audioBufferInfo.size = audioExtractor.readSampleData(audioBuffer, offset);

                if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
                    sawEOS_audio = true;
                    audioBufferInfo.size = 0;
                } else {
                    audioBufferInfo.presentationTimeUs = audioExtractor.getSampleTime();
                    audioBufferInfo.flags = audioExtractor.getSampleFlags();
                    mediaMuxer.writeSampleData(audioTrack, audioBuffer, audioBufferInfo);
                    audioExtractor.advance();
                    frameCount_audio++;
                }

//                size += videoBufferInfo.size;
//                currentSize = size;
//                currentProgress = (int) (size * 100 / this.size);
            }

            mediaMuxer.stop();
            mediaMuxer.release();
            videoExtractor.release();
            audioExtractor.release();

            long end = System.currentTimeMillis();

            Log.d(Fuck.blue, "--------------------------------------------");
            Log.d(Fuck.blue, "Video:" + videoPath.split("\\?")[0]);
            Log.d(Fuck.blue, "Audio:" + audioPath.split("\\?")[0]);
            Log.d(Fuck.blue, "isExists:" + new File(outPath).exists());
//            Log.d(Fuck.blue, "size:" + this.size);
            Log.d(Fuck.blue, "耗时：" + (end - begin) + "ms");
            Log.d(Fuck.blue, "--------------------------------------------");

            //通知刷新，进行显示
            ResourceUtils.sendBroadcast(context, new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(outPath)));

            // 设置downloadState状态为“已下载完成”状态
            setDownloadState(fileName, true);
            setNotificationState(fileName, true, true);
        } catch (IOException e) {
            if (generalNotification == null) {
                generalNotification = new GeneralNotification(context, "缓存媒体资源", this.tag);
            }
            generalNotification.setNotificationOnSDK26("缓存视频", "缓存失败\t" + fileName, R.drawable.notification_biu_video);

            e.printStackTrace();
        } finally {
            if (videoExtractor != null) {
                videoExtractor.release();
            }
            if (audioExtractor != null) {
                audioExtractor.release();
            }
        }
    }

    /**
     * 获取视频Extractor
     *
     * @param videoPath 视频路径
     * @param headers   头信息
     * @return  返回videoExtractor
     */
    private MediaExtractor getVideoExtractor(String videoPath, Map<String, String> headers) throws IOException {
        MediaExtractor videoExtractor = new MediaExtractor();

        //设置视频源
        videoExtractor.setDataSource(videoPath, headers);
        videoExtractor.selectTrack(0);

        return videoExtractor;
    }

    /**
     * 获取音频Extractor
     *
     * @param audioPath 音频路径
     * @param headers   头信息
     * @return  返回audioExtractor
     */
    private MediaExtractor getAudioExtractor(String audioPath, Map<String, String> headers) throws IOException {
        MediaExtractor audioExtractor = new MediaExtractor();

        //设置音频源
        audioExtractor.setDataSource(audioPath, headers);
        audioExtractor.selectTrack(0);

        return audioExtractor;
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

            e.printStackTrace();
        }

        return false;
    }

    /**
     * 获取媒体资源链接
     *
     * @param mainId    主ID（bvid， sid）
     * @param subId 子ID（cid）
     * @return  返回资源链接
     */
    public String[] reacquireMediaUrl(String mainId, long subId, int qualityId) {
        String[] urls;
        if (subId != 0) {
            urls = new String[2];
            Play play = MediaParseUtils.parseMedia(mainId, 0, subId);
            urls[0] = play.videos.get(qualityId).baseUrl;
            urls[1] = play.audios.get(0).baseUrl;

        } else {
            urls = new String[1];
            urls[0] = MusicUrlParseUtils.parseMusicUrl(mainId);;
        }

        return urls;
    }

    /**
     * 设置下载条目的状态
     *
     * @param fileName  文件名称
     * @param isComplete    是否已下载完毕
     */
    private void setDownloadState(String fileName, boolean isComplete) {
        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.DownloadDetailsForVideo);
        DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();

        if (isComplete) {
            downloadRecordsDatabaseUtils.setComplete(fileName);
        } else {
            downloadRecordsDatabaseUtils.setDownloading(fileName);
        }

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
