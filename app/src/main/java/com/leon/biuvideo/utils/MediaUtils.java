package com.leon.biuvideo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MediaUtils {
    /**
     * 获取、合并视频音频
     *
     * @param videoPath 视频路径
     * @param audioPath 音频路径
     * @param fileName   文件名称
     * @return  返回获取状态
     */
    public static boolean saveVideo(Context context, String videoPath, String audioPath, String fileName) {

        String folderPath = FileUtils.createFolder(FileUtils.ResourcesFolder.VIDEOS);

        String outPath = folderPath + "/" + fileName + ".mp4";

        long begin = System.currentTimeMillis();

        try {
            //设置头信息
            HashMap<String, String> headers = HttpUtils.getHeaders();

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
             * 这样的错误可能是缓冲区太小的缘故，设置sampleSize的大小即可
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

            Log.d(Fuck.blue, "--------------------------------------------");
            Log.d(Fuck.blue, "Video:" + videoPath.split("\\?")[0]);
            Log.d(Fuck.blue, "Audio:" + audioPath.split("\\?")[0]);
            Log.d(Fuck.blue, "isExists:" + new File(outPath).exists());
            Log.d(Fuck.blue, "耗时：" + (end - begin) + "ms");
            Log.d(Fuck.blue, "--------------------------------------------");

            //通知刷新，进行显示
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(outPath)));

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
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
     * 保存音乐
     *
     * @param resourceUrl  资源链接
     * @param fileName  文件名称
     * @return  返回保存状态
     */
    public static boolean saveMusic(Context context, String resourceUrl, String fileName) {
        try {
            URL url = new URL(resourceUrl);

            URLConnection urlConnection = url.openConnection();

            //设置头信息
            HashMap<String, String> headers = HttpUtils.getHeaders();
            urlConnection.setRequestProperty("User-Agent", headers.get("User-Agent"));
            urlConnection.setRequestProperty("Referer", headers.get("Referer"));

            urlConnection.connect();

            int length = urlConnection.getContentLength();

            BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());

            //创建musicFile对象
            File musicFile = new File(FileUtils.createFolder(FileUtils.ResourcesFolder.MUSIC), fileName + ".mp3");

            FileOutputStream fileOutputStream = new FileOutputStream(musicFile);

            byte[] bytes = new byte[1024 * 10];

            long total = 0;
            int len;
            while ((len = bufferedInputStream.read(bytes)) != -1) {
                total += len;

                Log.d(Fuck.blue, "保存进度: " + total * 100 / length);

                fileOutputStream.write(bytes, 0, len);
            }

            fileOutputStream.close();
            bufferedInputStream.close();

            //发送广播，通知刷新显示
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(musicFile)));

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 保存图片资源至系统相册
     *
     * @param context   上下文对象
     * @param picUrl    图片链接
     * @return  返回保存成功状态
     */
    public static boolean savePicture(Context context, String picUrl) {
        try {
            URL url = new URL(picUrl);

            HttpURLConnection connection = (HttpURLConnection)  url.openConnection();

            //获取&设置请求头
            HashMap<String, String> headers = HttpUtils.getHeaders();
            connection.setRequestProperty("User-Agent", headers.get("User-Agent"));
            connection.setRequestProperty("Referer", headers.get("Referer"));

            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            //获取bitmap对象
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            //创建保存picFile类
            //图片文件名称使用UUID进行命名
            File picFile = new File(FileUtils.createFolder(FileUtils.ResourcesFolder.PICTURES), UUID.randomUUID().toString() + ".jpeg");

            //获取输出流
            FileOutputStream fileOutputStream = new FileOutputStream(picFile);

            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

            fileOutputStream.close();
            inputStream.close();
            connection.disconnect();

            //保存图片后发送广播，通知刷新图库的显示
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(picFile)));

            return isSuccess;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 将webView中的内容转换为图片
     *
     * @param webView   webView对象
     * @param context   context
     * @return 返回截图保存状态
     */
    public static boolean saveArticle(WebView webView, Context context) {
        Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), webView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);

        try {
            File articlePic = new File(FileUtils.createFolder(FileUtils.ResourcesFolder.PICTURES), FileUtils.generateFileName("article") + ".jpeg");

            FileOutputStream fos = new FileOutputStream(articlePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(articlePic)));

            fos.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
