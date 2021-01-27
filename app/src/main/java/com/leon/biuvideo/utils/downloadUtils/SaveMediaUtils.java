package com.leon.biuvideo.utils.downloadUtils;

import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.HttpUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SaveMediaUtils {
    private String videoTempPath = null;
    private String audiTempPath = null;
    private OnDownloadMediaListener onDownloadMediaListener;

    private static SaveMediaUtils saveMediaUtils;
    private final OkHttpClient videoOkHttpClient, audioOkHttpClient;

    public static SaveMediaUtils getInstance() {
        if (saveMediaUtils == null) {
            saveMediaUtils = new SaveMediaUtils();
        }
        return saveMediaUtils;
    }

    private SaveMediaUtils() {
        videoOkHttpClient = new OkHttpClient();
        audioOkHttpClient = new OkHttpClient();
    }

    public interface OnDownloadMediaListener {
        void onDownloadFailed();

        void onDownloadSuccess(String videoTempPath, String audiTempPath);

        void onDownloading(int progress);
    }

    public void setOnDownloadMediaListener(OnDownloadMediaListener onDownloadMediaListener) {
        this.onDownloadMediaListener = onDownloadMediaListener;
    }

    public void saveMedia(String videoUrl, String audioUrl, String fileName) {
        Request videoRequest, audioRequest;
        Request.Builder requestBuilder = new Request.Builder();

        videoRequest = requestBuilder.url(videoUrl).headers(Headers.of(HttpUtils.getHeaders())).get().build();
        audioRequest = requestBuilder.url(audioUrl).headers(Headers.of(HttpUtils.getHeaders())).get().build();
        videoOkHttpClient.newCall(videoRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (onDownloadMediaListener != null) {
                    onDownloadMediaListener.onDownloadFailed();
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                //判断是否响应成功
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = null;

                    byte[] buf = new byte[2048];
                    int len;
                    FileOutputStream fileOutputStream = null;
                    try {
                        inputStream = response.body().byteStream();
                        long total = response.body().contentLength();

                        // 检查路径
                        String folderPath = FileUtils.createFolder(FileUtils.ResourcesFolder.TEMP);

                        File file = new File(folderPath, fileName + ".mp4");
                        fileOutputStream = new FileOutputStream(file);

                        long sum = 0;
                        while ((len = inputStream.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, len);
                            sum += len;

                            int progress = (int) (sum * 1.0f / total * 100);

                            // 下载中
                            onDownloadMediaListener.onDownloading(progress);
                        }
                        fileOutputStream.flush();

                        videoTempPath = file.getPath();
                        onDownloadMediaListener.onDownloadSuccess(videoTempPath, audiTempPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (inputStream != null)
                                inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (fileOutputStream != null)
                                fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        audioOkHttpClient.newCall(audioRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (onDownloadMediaListener != null) {
                    onDownloadMediaListener.onDownloadFailed();
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                //判断是否响应成功
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = null;

                    byte[] buf = new byte[2048];
                    int len;
                    FileOutputStream fileOutputStream = null;
                    try {
                        inputStream = response.body().byteStream();
                        long total = response.body().contentLength();

                        // 检查路径
                        String folderPath = FileUtils.createFolder(FileUtils.ResourcesFolder.TEMP);

                        File file = new File(folderPath, fileName + ".mp3");
                        fileOutputStream = new FileOutputStream(file);

                        long sum = 0;
                        while ((len = inputStream.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, len);
                            sum += len;

                            int progress = (int) (sum * 1.0f / total * 100);

                            // 下载中
                            onDownloadMediaListener.onDownloading(progress);
                        }
                        fileOutputStream.flush();

                        audiTempPath = file.getPath();
                        onDownloadMediaListener.onDownloadSuccess(videoTempPath, audiTempPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (inputStream != null)
                                inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (fileOutputStream != null)
                                fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}