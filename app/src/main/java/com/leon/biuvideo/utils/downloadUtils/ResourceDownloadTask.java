package com.leon.biuvideo.utils.downloadUtils;

import android.content.Context;

import androidx.annotation.IntDef;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.HttpOption;
import com.arialyy.aria.core.common.RequestEnum;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
import com.leon.biuvideo.utils.HttpUtils;

import java.io.File;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/30
 * @Desc
 */
public class ResourceDownloadTask {
    public static final int RES_TYPE_VIDEO = 0;
    public static final int RES_TYPE_PICTURE = 1;
    public static final int RES_TYPE_AUDIO = 2;

    private static final String RESOURCE = "resources";

    private static final String PICTURES = "pictures";
    private static final String VIDEOS = "videos";
    private static final String AUDIOS = "audios";

    private final Context context;
    private final DownloadHistory downloadHistory;

    private String savePath;

    /**
     * 请求头信息
     */
    private Map<String, String> headers = HttpUtils.getAPIRequestHeader();
    private long taskId;

    public ResourceDownloadTask(Context context, DownloadHistory downloadHistory) {
        this.context = context;
        this.downloadHistory = downloadHistory;

        checkSaveDirectory();
    }

    public ResourceDownloadTask setHeaders (String key, String value) {
        if (headers.containsKey(key)) {
            headers.replace(key, value);
        } else {
            headers.put(key, value);
        }

        return this;
    }

    /**
     * 检查/创建 资源保存路径
     */
    private void checkSaveDirectory() {
        File resourcesPath = new File(context.getDataDir(), RESOURCE);
        if (!resourcesPath.exists()) {
            resourcesPath.mkdirs();
        }

        File resSavePath;
        String resFileType;
        switch (downloadHistory.getResType()) {
            case RES_TYPE_VIDEO:
                resSavePath = new File(resourcesPath, VIDEOS);
                resFileType = ".mp4";
                break;
            case RES_TYPE_AUDIO:
                resSavePath = new File(resourcesPath, AUDIOS);
                resFileType = ".mp3";
                break;
            case RES_TYPE_PICTURE:
                resSavePath = new File(resourcesPath, PICTURES);
                resFileType = ".jpg";
                break;
            default:
                throw new RuntimeException("资源类型错误");
        }

        if (!resSavePath.exists()) {
            if (resSavePath.mkdirs()) {
                this.savePath = new File(resSavePath, downloadHistory.getTitle() + "_" + downloadHistory.getResKey() + resFileType).getAbsolutePath();
            }
        }
    }

    /**
     * 创建任务并开始下载
     */
    private void startDownload() {
        HttpOption httpOption = new HttpOption();

        for (Map.Entry<String, String> headersEntry : headers.entrySet()) {
            httpOption.addHeader(headersEntry.getKey(), headersEntry.getValue());
        }

        httpOption.setRequestType(RequestEnum.GET);

        taskId = Aria.download(context)
                .load(downloadHistory.getResStreamUrl())
                .setFilePath(savePath)
                .option(httpOption)
                .create();
    }

    /**
     * 下载完成后将本条记录写入数据库
     */
    @Download.onTaskComplete
    void complete () {
        DownloadHistoryUtils downloadHistoryUtils = new DownloadHistoryUtils(context);
        DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils = downloadHistoryUtils.getDownloadHistoryDaoUtils();

        downloadHistoryDaoUtils.insert(downloadHistory);
    }

    @IntDef({RES_TYPE_VIDEO, RES_TYPE_AUDIO, RES_TYPE_PICTURE})
    public @interface ResourcesType{}
}
