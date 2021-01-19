package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.PictureListAdapter;
import com.leon.biuvideo.beans.upMasterBean.Picture;
import com.leon.biuvideo.layoutManager.PictureGridLayoutManager;
import com.leon.biuvideo.ui.views.RoundPopupWindow;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.downloadUtils.ResourceUtils;
import com.leon.biuvideo.values.Paths;
import com.leon.biuvideo.utils.ValueFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 相簿界面Activity
 */
public class PictureActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView picture_more;

    private TextView
            picture_textView_time,
            picture_textView_view,
            picture_textView_like,
            picture_textView_desc;

    private RecyclerView picture_recyclerView;

    private Picture picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        init();
        initView();
        initValue();
    }

    private void init() {
        //获取数据
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        picture = (Picture) extras.getSerializable("picture");
    }

    private void initView() {
        ImageView picture_back = findViewById(R.id.picture_back);
        picture_back.setOnClickListener(this);

        picture_more = findViewById(R.id.picture_more);
        picture_more.setOnClickListener(this);

        picture_textView_time =findViewById(R.id.picture_textView_time);
        picture_textView_view =findViewById(R.id.picture_textView_view);
        picture_textView_like =findViewById(R.id.picture_textView_like);
        picture_textView_desc =findViewById(R.id.picture_textView_desc);

        picture_recyclerView = findViewById(R.id.picture_recyclerView);
    }

    private void initValue() {
        picture_textView_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
                .format(new Date(picture.ctime * 1000)));

        picture_textView_view.setText(ValueFormat.generateCN(picture.view));
        picture_textView_like.setText(ValueFormat.generateCN(picture.like));
        picture_textView_desc.setText(picture.description);

        int spanCount;

        //判断要显示的列数
        if (picture.pictures.size() % 3 == 0) {
            spanCount = 3;
        } else if (picture.pictures.size() % 2 == 0) {
            spanCount = 2;
        } else {
            spanCount = 1;
        }
        PictureGridLayoutManager pictureGridLayoutManager = new PictureGridLayoutManager(getApplicationContext(), spanCount);

        PictureListAdapter pictureListAdapter = new PictureListAdapter(picture.pictures, PictureActivity.this);
        picture_recyclerView.setLayoutManager(pictureGridLayoutManager);
        picture_recyclerView.setAdapter(pictureListAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.picture_back:
                this.finish();
                break;
            case R.id.picture_more:
                //创建popupWindow
                RoundPopupWindow roundPopupWindow = new RoundPopupWindow(getApplicationContext(), picture_more);
                roundPopupWindow
                        .setContentView(R.layout.picture_popup_window)
                        .setOnClickListener(R.id.picture_more_saveAll, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!InternetUtils.checkNetwork(getApplicationContext())) {
                                    Snackbar.make(v, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();
                                    return;
                                }

                                roundPopupWindow.dismiss();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int saveCounts = 0;

                                        for (String url : picture.pictures) {
                                            boolean b = ResourceUtils.savePicture(getApplicationContext(), url);

                                            if (b) saveCounts++;
                                        }

                                        Snackbar.make(v, "保存成功" + saveCounts + "张,失败" + (picture.pictures.size() - saveCounts) + "张", Snackbar.LENGTH_SHORT).show();
                                    }
                                }).start();
                            }
                        })
                        .setOnClickListener(R.id.picture_more_jumpToOrigin, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intentOriginUrl = new Intent();
                                intentOriginUrl.setAction("android.intent.action.VIEW");
                                Uri uri = Uri.parse(Paths.pictureWebPage + picture.docId + "?tab=1&type=2");
                                intentOriginUrl.setData(uri);
                                startActivity(intentOriginUrl);

                                roundPopupWindow.dismiss();
                            }
                        })
                        .setLocation(RoundPopupWindow.SHOW_AS_DROP_DOWN)
                        .create();
                break;
            default:
                break;
        }
    }
}