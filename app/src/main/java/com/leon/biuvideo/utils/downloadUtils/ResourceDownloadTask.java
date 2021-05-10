package com.leon.biuvideo.utils.downloadUtils;

import android.content.Context;

import androidx.annotation.IntDef;

import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.HttpOption;
import com.arialyy.aria.core.common.RequestEnum;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiAnthology;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoInfo;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.dao.DownloadHistoryDao;
import com.leon.biuvideo.greendao.dao.DownloadLevelOne;
import com.leon.biuvideo.greendao.dao.DownloadLevelOneDao;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
import com.leon.biuvideo.greendao.daoutils.DownloadLevelOneUtils;

import java.io.File;
import java.io.Serializable;

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
    private final Object object;
    private final DownloadHistory downloadHistory;

    private String savePath;

    private DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils;

    private VideoInfo.VideoAnthology videoAnthology;
    private BangumiAnthology bangumiAnthology;
    private long taskId;

    public ResourceDownloadTask(Context context, Object object, DownloadHistory downloadHistory, Serializable serializable) {
        this.context = context;
        this.object = object;
        this.downloadHistory = downloadHistory;

        if (serializable instanceof VideoInfo.VideoAnthology) {
            videoAnthology = (VideoInfo.VideoAnthology) serializable;
        } else if (serializable instanceof BangumiAnthology) {
            bangumiAnthology = (BangumiAnthology) serializable;
        } else {
            throw new ClassCastException("类型必须是'VideoInfo.VideoAnthology'或'BangumiAnthology'");
        }

        this.savePath = checkSaveDirectory(context, downloadHistory.getResType(), downloadHistory.getMainTitle(), downloadHistory.getSubTitle()).getAbsolutePath();
    }

    /**
     * 创建任务并开始下载
     */
    public void startDownload() {
        DownloadHistoryUtils downloadHistoryUtils = new DownloadHistoryUtils(context);
        downloadHistoryDaoUtils = downloadHistoryUtils.getDownloadHistoryDaoUtils();

        HttpOption httpOption = new HttpOption();
        httpOption.addHeader("Referer", "https://www.bilibili.com/");
        httpOption.setRequestType(RequestEnum.GET);

        taskId = Aria.download(object)
                .load(downloadHistory.getResStreamUrl())
                .setFilePath(savePath)
                .option(httpOption)
                .create();

        // 如果存在多个选集，则需要获取其视频信息
        if (downloadHistory.getIsMultipleAnthology()) {
            String levelOneId = downloadHistory.getLevelOneId();

            // 查询是否已存在'LevelOne'数据
            DownloadLevelOneUtils downloadLevelOneUtils = new DownloadLevelOneUtils(context);
            DaoBaseUtils<DownloadLevelOne> downloadLevelOneDaoBaseUtils = downloadLevelOneUtils.getDownloadLevelOneDaoBaseUtils();
            if (!downloadLevelOneDaoBaseUtils.isExists(DownloadLevelOneDao.Properties.LevelOneId.eq(levelOneId))) {
                downloadLevelOneDaoBaseUtils.insert(new DownloadLevelOne(null, downloadHistory.getMainTitle(),
                        downloadHistory.getLevelOneId(), downloadHistory.getCoverUrl()));
            }
        }

        if (downloadHistory.getResType() != RES_TYPE_PICTURE) {
            downloadHistory.setTaskId(taskId);
            downloadHistory.setSavePath(savePath);
            downloadHistoryDaoUtils.insert(downloadHistory);
        }
    }

    /**
     * 检查/创建 资源保存路径
     */
    public static File checkSaveDirectory(Context context, @ResourcesType int resType, String mainTitle, String subTitle) {
        File resourcesPath = context.getExternalFilesDir(RESOURCE);

        File resSavePath;
        String resFileType;
        switch (resType) {
            case RES_TYPE_VIDEO:
                resSavePath = new File(resourcesPath, VIDEOS);
                resFileType = ".flv";
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
            resSavePath.mkdirs();
        }

        return new File(resSavePath, (mainTitle + "_" + subTitle + resFileType));
    }

    /**
     * 查询是否已下载
     *
     * @return 已存在返回true，不存在则返回false
     */
    public static boolean isExists(File savePath, Context context, String levelOnId, String levelTwoId) {
        boolean exists = savePath.exists();

        // 如果本地不存在，则查询数据库
        if (!exists) {
            DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils = new DownloadHistoryUtils(context).getDownloadHistoryDaoUtils();
            return downloadHistoryDaoUtils.isExists(DownloadHistoryDao.Properties.LevelOneId.eq(levelOnId),
                    DownloadHistoryDao.Properties.LevelTwoId.eq(levelTwoId));
        } else {
            return true;
        }
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    @IntDef({RES_TYPE_VIDEO, RES_TYPE_AUDIO, RES_TYPE_PICTURE})
    public @interface ResourcesType{}

    public VideoInfo.VideoAnthology getVideoAnthology() {
        return videoAnthology;
    }

    public BangumiAnthology getBangumiAnthology() {
        return bangumiAnthology;
    }

    public DownloadHistory getDownloadHistory() {
        return downloadHistory;
    }
}
