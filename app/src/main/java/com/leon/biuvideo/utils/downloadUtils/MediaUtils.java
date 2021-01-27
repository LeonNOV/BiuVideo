package com.leon.biuvideo.utils.downloadUtils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.net.Uri;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.videoBean.play.Play;
import com.leon.biuvideo.ui.views.GeneralNotification;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.MediaParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.MusicUrlParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Random;

/**
 * 下载媒体资源(视频/音频)
 */
public class MediaUtils {
    private int videoTrackIndex = 0;
    private int audioTrackIndex = 0;
    private MediaMuxer mediaMuxer;

    private final Context context;
    private final int tag;

    private GeneralNotification generalNotification;
    private MusicUrlParser musicUrlParser;

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
        // 设置downloadState状态为“正在下载”状态
        setDownloadState(fileName, false);
        setNotificationState(fileName, false, true);

        // 先对视频、音频进行缓存，两个文件均保存至`Temp`文件夹
        SaveMediaUtils saveMediaUtils = SaveMediaUtils.getInstance();
        saveMediaUtils.setOnDownloadMediaListener(new SaveMediaUtils.OnDownloadMediaListener() {
            @Override
            public void onDownloadFailed() {

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

    private void mergeVideo(String videoTempPath, String audioTempPath, String fileName) {
        MediaExtractor videoExtractor = null;
        MediaExtractor audioExtractor = null;

        try {
            String folderPath = FileUtils.createFolder(FileUtils.ResourcesFolder.VIDEOS);
            String outPath = folderPath + "/" + fileName + ".mp4";

            //设置输出配置
            mediaMuxer = new MediaMuxer(outPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

            //创建视频Extractor
            videoExtractor = getVideoExtractor(videoTempPath);

            //创建音频Extractor
            audioExtractor = getAudioExtractor(audioTempPath);

            /*
             * 设置缓冲区大小
             *
             * 出现：A/libc: Fatal signal 11 (SIGSEGV), code 1 (SEGV_MAPERR),
             *      fault addr 0xc in tid 2843 (Thread-12), pid 2430 (m.leon.biuvideo)
             * 这样的错误可能是缓冲区太小的缘故，设置sampleSize的大小即可
             * */
            int sampleSize = 1024 * 1000;
            ByteBuffer videoByteBuffer = ByteBuffer.allocate(sampleSize);
            ByteBuffer audioByteBuffer = ByteBuffer.allocate(sampleSize);

            MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo audioBufferInfo = new MediaCodec.BufferInfo();

            mediaMuxer.start();

            while (true) {
                int readVideoSampleSize = videoExtractor.readSampleData(videoByteBuffer, 0);
                if (readVideoSampleSize < 0) {
                    break;
                }
                videoBufferInfo.size = readVideoSampleSize;
                videoBufferInfo.presentationTimeUs = videoExtractor.getSampleTime();
                videoBufferInfo.offset = 0;
                videoBufferInfo.flags = videoExtractor.getSampleFlags();
                mediaMuxer.writeSampleData(videoTrackIndex, videoByteBuffer, videoBufferInfo);
                videoExtractor.advance();

//                size += videoBufferInfo.size;
//                currentSize = size;
//                currentProgress = (int) (size * 100 / this.size);
            }

            while (true) {
                int readAudioSampleSize = audioExtractor.readSampleData(audioByteBuffer, 0);
                if (readAudioSampleSize < 0) {
                    break;
                }
                audioBufferInfo.size = readAudioSampleSize;
                audioBufferInfo.presentationTimeUs = audioExtractor.getSampleTime();
                audioBufferInfo.offset = 0;
                audioBufferInfo.flags = audioExtractor.getSampleFlags();
                mediaMuxer.writeSampleData(audioTrackIndex, audioByteBuffer, audioBufferInfo);
                audioExtractor.advance();

//                size += videoBufferInfo.size;
//                currentSize = size;
//                currentProgress = (int) (size * 100 / this.size);
            }

            //通知刷新，进行显示
            ResourceUtils.sendBroadcast(context, new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(outPath)));

            // 设置downloadState状态为“已下载完成”状态
            setDownloadState(fileName, true);
            setNotificationState(fileName, true, true);

            deleteTemp(videoTempPath, audioTempPath);

        } catch (IOException e) {
            if (generalNotification == null) {
                generalNotification = new GeneralNotification(context, "缓存媒体资源", this.tag);
            }
            generalNotification.setNotificationOnSDK26("缓存视频", "缓存失败\t" + fileName, R.drawable.notification_biu_video);
            setDownloadFailState(fileName);

            e.printStackTrace();
        } finally {
            if (mediaMuxer != null) {
                mediaMuxer.stop();
                mediaMuxer.release();
            }

            if (audioExtractor != null) {
                audioExtractor.release();
            }

            if (videoExtractor != null) {
                videoExtractor.release();
            }
        }
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
     * 获取视频Extractor
     *
     * @param videoPath 视频路径
     * @return  返回videoExtractor
     */
    private MediaExtractor getVideoExtractor(String videoPath) throws IOException {
        MediaExtractor videoExtractor = new MediaExtractor();

        //设置视频源
        videoExtractor.setDataSource(videoPath);
        videoExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);

        // 找到视频所在轨道索引
        for (int i = 0; i < videoExtractor.getTrackCount(); i++) {
            MediaFormat trackFormat = videoExtractor.getTrackFormat(i);
            String mimeType = trackFormat.getString(MediaFormat.KEY_MIME);
            if (mimeType.startsWith("video/")) {
                videoExtractor.selectTrack(i);
                videoTrackIndex = mediaMuxer.addTrack(trackFormat);
                break;
            }
        }

        return videoExtractor;
    }

    /**
     * 获取音频Extractor
     *
     * @param audioPath 音频路径
     * @return  返回audioExtractor
     */
    private MediaExtractor getAudioExtractor(String audioPath) throws IOException {
        MediaExtractor audioExtractor = new MediaExtractor();

        //设置音频源
        audioExtractor.setDataSource(audioPath);
        audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);

        // 找到音频所在轨道索引
        for (int i = 0; i < audioExtractor.getTrackCount(); i++) {
            MediaFormat trackFormat = audioExtractor.getTrackFormat(i);
            String mimeType = trackFormat.getString(MediaFormat.KEY_MIME);
            if (mimeType.startsWith("audio/")) {
                audioExtractor.selectTrack(i);
                audioTrackIndex = mediaMuxer.addTrack(trackFormat);
                break;
            }
        }

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
            setDownloadFailState(fileName);

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

            MediaParser mediaParser = new MediaParser(context);
            Play play = mediaParser.parseMedia(mainId, subId, false);

            urls[0] = play.videos.get(qualityId).baseUrl;
            urls[1] = play.audios.get(0).baseUrl;

        } else {
            urls = new String[1];

            if (musicUrlParser == null) {
                musicUrlParser = new MusicUrlParser(context);
            }

            urls[0] = musicUrlParser.parseMusicUrl(mainId);;
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
