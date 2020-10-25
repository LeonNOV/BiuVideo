package com.leon.biuvideo.utils;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class MediaUtils {
    /**
     * 获取、合并视频音频
     *
     * @param videoPath 视频路径
     * @param audioPath 音频路径
     * @param outPath   存放路径
     * @return  返回获取状态
     */
    public static boolean ComposeTrack(String videoPath, String audioPath, String outPath) {
        long begin = System.currentTimeMillis();

        try {
            //设置头信息
            HashMap<String, String> headers = getHeaders();

            //创建视频Extractor
            MediaExtractor videoExtractor = getVideoExtractor(videoPath, headers);
            MediaFormat videoFormat = videoExtractor.getTrackFormat(0);

            //创建音频Extractor
            MediaExtractor audioExtractor = getAudioExtractor(audioPath, headers);
            MediaFormat audioFormat = audioExtractor.getTrackFormat(0);

            //设置输出配置
            MediaMuxer mediaMuxer = new MediaMuxer(outPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int videoTrack = mediaMuxer.addTrack(videoFormat);
            int audioTrack = mediaMuxer.addTrack(audioFormat);

            //设置缓冲区大小
            /**
             * 出现：A/libc: Fatal signal 11 (SIGSEGV), code 1 (SEGV_MAPERR),
             *      fault addr 0xc in tid 2843 (Thread-12), pid 2430 (m.leon.biuvideo)
             * 这样的错误可能是缓冲区太小的缘故
             */
            int sampleSize = 1024 * 1000;
            ByteBuffer videoBuffer = ByteBuffer.allocate(sampleSize);
            ByteBuffer audioBuffer = ByteBuffer.allocate(sampleSize);

            MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo audioBufferInfo = new MediaCodec.BufferInfo();

            videoExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
            audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);

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
            }

            boolean sawEOS_audio = false;
            int frameCount_audio = 0;
            while (!sawEOS_audio) {
                frameCount_audio++;
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
                }
            }

            mediaMuxer.stop();
            mediaMuxer.release();
            videoExtractor.release();
            audioExtractor.release();

            long end = System.currentTimeMillis();

            Log.d(LogTip.blue, "--------------------------------------------");
            Log.d(LogTip.blue, "Video:" + videoPath.split("\\?")[0]);
            Log.d(LogTip.blue, "Audio:" + audioPath.split("\\?")[0]);
            Log.d(LogTip.blue, "isExists:" + new File(outPath).exists());
            Log.d(LogTip.blue, "耗时：" + (end - begin) + "ms");
            Log.d(LogTip.blue, "--------------------------------------------");

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 设置头信息
     *
     * @return  返回头信息
     */
    private static HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36 Edg/86.0.622.51");
        headers.put("Connection", "keep-alive");
        headers.put("Referer", "https://www.bilibili.com/");

        return headers;
    }

    /**
     * 获取视频Extractor
     *
     * @param videoPath 视频路径
     * @param headers   头信息
     * @return  返回videoExtractor
     */
    private static MediaExtractor getVideoExtractor(String videoPath, Map<String, String> headers) throws IOException {
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
    private static MediaExtractor getAudioExtractor(String audioPath, Map<String, String> headers) throws IOException {
        MediaExtractor audioExtractor = new MediaExtractor();

        //设置音频源
        audioExtractor.setDataSource(audioPath, headers);
        audioExtractor.selectTrack(0);

        return audioExtractor;
    }

    /**
     * 写入文件前，检查文件夹状态的方法
     *
     * @return
     */
    public static String folderState(ResourcesFolder resourcesFolder ) {
        //获取根路径
        File rootDirectory = Environment.getExternalStorageDirectory();

        //在根目录创建名为BiuVideo的文件夹
        File biuVideoFolder = new File(rootDirectory, "BiuVideo");
        if (!biuVideoFolder.exists()) {
            biuVideoFolder.mkdirs();
        }

        String biuVideoDirectory = biuVideoFolder.getAbsolutePath();

        //创建对应文件夹并返回其绝对路径
        return createFolder(biuVideoDirectory, resourcesFolder);
    }

    /**
     * 创建文件夹
     *
     * @param parent    父路径
     * @param folderName    子文件夹名称
     */
    private static String createFolder(String parent, ResourcesFolder folderName) {
        File file = new File(parent, folderName.value);
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }

    /**
     * 生成一个随机的文件名
     *
     * @param baseName  基本名称
     * @return  返回一个文件名
     */
    public static String generateFileName(String baseName) {
        SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA);
        String format = ymd.format(new Date(System.currentTimeMillis()));

        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 5);

        return baseName + "-" + uuid + "-" + format;
    }

    public enum ResourcesFolder {
        VIDEOS("Videos"),
        PICTURES("Pictures"),
        SONGS("Songs");

        public String value;

        ResourcesFolder(String value) {
            this.value = value;
        }
    }
}
