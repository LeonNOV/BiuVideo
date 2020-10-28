package com.leon.biuvideo.utils;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

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
     * 保存音乐/图片
     *
     * @param resourceUrl  资源链接
     * @param path  保存路径
     * @param fileName  文件名称
     * @return  返回保存状态
     */
    public static boolean saveMusic(String resourceUrl, String path, String fileName) {
        try {
            URL url = new URL(resourceUrl);

            URLConnection urlConnection = url.openConnection();

            //设置头信息
            HashMap<String, String> headers = getHeaders();
            urlConnection.setRequestProperty("User-Agent", headers.get("User-Agent"));
            urlConnection.setRequestProperty("Referer", headers.get("Referer"));

            urlConnection.connect();

            int length = urlConnection.getContentLength();

            BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(path + "/" + fileName);

            byte[] bytes = new byte[1024 * 10];

            long total = 0;
            int len;
            while ((len = bufferedInputStream.read(bytes)) != -1) {
                total += len;

                Log.d(LogTip.blue, "保存进度: " + total * 100 / length);

                fileOutputStream.write(bytes, 0, len);
            }

            fileOutputStream.close();
            bufferedInputStream.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
