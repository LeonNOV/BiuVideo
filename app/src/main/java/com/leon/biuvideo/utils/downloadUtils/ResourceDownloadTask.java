package com.leon.biuvideo.utils.downloadUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.HttpOption;
import com.arialyy.aria.core.common.RequestEnum;
import com.arialyy.aria.core.task.DownloadTask;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.Bangumi;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoInfo;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.dao.DownloadLevelOne;
import com.leon.biuvideo.greendao.dao.DownloadLevelOneDao;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
import com.leon.biuvideo.greendao.daoutils.DownloadLevelOneUtils;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.BangumiDetailParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoInfoParser;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/30
 * @Desc 单个下载任务
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
    private final Map<String, String> headers = HttpUtils.getAPIRequestHeader();
    private DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils;
    private Handler handler;

    public ResourceDownloadTask(Context context, DownloadHistory downloadHistory) {
        this.context = context;
        this.downloadHistory = downloadHistory;

        checkSaveDirectory();
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
    public void startDownload() {
        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Object obj = msg.obj;

                DownloadLevelOne downloadLevelOne = new DownloadLevelOne();
                downloadLevelOne.setLevelOneId(downloadHistory.getLevelOneId());
                if (obj != null) {
                    if (obj instanceof VideoInfo) {
                        VideoInfo videoInfo = (VideoInfo) obj;
                        downloadLevelOne.setTitle(videoInfo.title);
                        downloadLevelOne.setCoverUrl(videoInfo.cover);
                    } else {
                        Bangumi bangumi = (Bangumi) obj;
                        downloadLevelOne.setTitle(bangumi.title);
                        downloadLevelOne.setCoverUrl(bangumi.cover);
                    }

                    new DownloadLevelOneUtils(context).getDownloadLevelOneDaoBaseUtils().insert(downloadLevelOne);
                }


                return true;
            }
        });

        DownloadHistoryUtils downloadHistoryUtils = new DownloadHistoryUtils(context);
        downloadHistoryDaoUtils = downloadHistoryUtils.getDownloadHistoryDaoUtils();

        HttpOption httpOption = new HttpOption();

        for (Map.Entry<String, String> headersEntry : headers.entrySet()) {
            httpOption.addHeader(headersEntry.getKey(), headersEntry.getValue());
        }

        httpOption.setRequestType(RequestEnum.GET);

        long taskId = Aria.download(context)
                .load(downloadHistory.getResStreamUrl())
                .setFilePath(savePath)
                .option(httpOption)
                .create();

        // 如果存在多个选集，则需要获取其视频信息
        if (downloadHistory.getIsMultipleAnthology()) {
            String levelOneId = downloadHistory.getLevelOneId();

            // 查询是否已存在'LevelOne'数据
            List<DownloadHistory> downloadHistories = downloadHistoryDaoUtils.queryByQueryBuilder(DownloadLevelOneDao.Properties.LevelOneId.eq(levelOneId));
            if (downloadHistories == null || downloadHistories.size() == 0) {
                getVideoInfo(downloadHistory.getLevelOneId());
            }
        }

        if (downloadHistory.getResType() != RES_TYPE_PICTURE) {
            downloadHistory.setTaskId(taskId);
            downloadHistoryDaoUtils.insert(downloadHistory);
        }
    }

    @IntDef({RES_TYPE_VIDEO, RES_TYPE_AUDIO, RES_TYPE_PICTURE})
    public @interface ResourcesType{}

    /**
     * 取消下载
     */
    @Download.onTaskCancel
    void onCancel (DownloadTask downloadTask) {
        if (downloadTask.getEntity().getId() == downloadHistory.getTaskId()) {
            downloadHistoryDaoUtils.delete(downloadHistory);
        }
    }

    /**
     * 下载失败
     */
    @Download.onTaskFail
    void onFail (DownloadTask downloadTask) {
        if (downloadTask.getEntity().getId() == downloadHistory.getTaskId()) {
            downloadHistory.setIsFailed(true);
            downloadHistoryDaoUtils.update(downloadHistory);
        }
    }

    /**
     * 下载完成
     */
    @Download.onTaskComplete
    void complete (DownloadTask downloadTask) {
        if (downloadTask.getEntity().getId() == downloadHistory.getTaskId()) {
            downloadHistory.setIsCompleted(true);
            downloadHistoryDaoUtils.update(downloadHistory);
        }
    }

    /**
     * 获取'LevelOne'数据
     */
    private void getVideoInfo (String levelOneId) {
        Message message = handler.obtainMessage();
        // 是否为投稿视频
        if (levelOneId.startsWith("BV")) {
            message.obj = VideoInfoParser.parseData(downloadHistory.getLevelOneId());
        } else {
            message.obj = new BangumiDetailParser(levelOneId).parseData();
        }

        handler.sendMessage(message);
    }
}
