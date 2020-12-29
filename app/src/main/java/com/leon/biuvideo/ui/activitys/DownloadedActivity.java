package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.Tables;

import java.io.File;
import java.util.List;

public class DownloadedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded);

        init();
    }

    private void init() {
        String videoPath = FileUtils.createFolder(FileUtils.ResourcesFolder.VIDEOS);
        String audioPath = FileUtils.createFolder(FileUtils.ResourcesFolder.MUSIC);

        // 检查已删除的媒体文件
        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(getApplicationContext(), Tables.DownloadDetailsForVideo);
        DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();

        List<DownloadedDetailMedia> downloadedDetailMedia = downloadRecordsDatabaseUtils.queryAllSubVideo(null);

        File mediaFile;
        String path;
        for (DownloadedDetailMedia detailMedia : downloadedDetailMedia) {
            if (detailMedia.isVideo) {
                path = videoPath + "/" + detailMedia.fileName + ".mp4";
            } else {
                path = audioPath + "/" + detailMedia.fileName + ".mp3";
            }

            mediaFile = new File(path);
            if (!mediaFile.exists()) {
                downloadRecordsDatabaseUtils.setDelete(detailMedia.fileName);
            }
        }

        downloadRecordsDatabaseUtils.close();
    }
}