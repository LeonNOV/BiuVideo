package com.leon.biuvideo.ui.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.PictureListAdapter;
import com.leon.biuvideo.beans.upMasterBean.Picture;
import com.leon.biuvideo.layoutManager.PictureGridLayoutManager;
import com.leon.biuvideo.ui.SimpleLoadDataThread;
import com.leon.biuvideo.ui.dialogs.LoadingDialog;
import com.leon.biuvideo.ui.views.RoundPopupWindow;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.downloadUtils.ResourceUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;
import com.leon.biuvideo.utils.ValueUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.FutureTask;

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
    private LoadingDialog loadingDialog;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        initView();
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

        loadingDialog = new LoadingDialog(PictureActivity.this);
        loadingDialog.show();

        loadData();
    }

    private void loadData() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                picture = (Picture) extras.getSerializable("picture");

                Message message = handler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();
                bundle.putBoolean("loadState", true);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean loadState = msg.getData().getBoolean("loadState");

                if (loadState) {
                    initValue();
                }

                loadingDialog.dismiss();

                return true;
            }
        });
    }

    private void initValue() {
        picture_textView_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
                .format(new Date(picture.ctime * 1000)));

        picture_textView_view.setText(ValueUtils.generateCN(picture.view));
        picture_textView_like.setText(ValueUtils.generateCN(picture.like));
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
                                    SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                                    return;
                                }

                                roundPopupWindow.dismiss();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int saveCounts = 0;

                                        for (String url : picture.pictures) {
                                            boolean b = ResourceUtils.savePicture(getApplicationContext(), url);

                                            if (b) {
                                                saveCounts++;
                                            }
                                        }

                                        SimpleSnackBar.make(v, "保存成功" + saveCounts + "张,失败" + (picture.pictures.size() - saveCounts) + "张", SimpleSnackBar.LENGTH_SHORT).show();
                                    }
                                }).start();
                            }
                        })
                        .setOnClickListener(R.id.picture_more_jumpToOrigin, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intentOriginUrl = new Intent();
                                intentOriginUrl.setAction("android.intent.action.VIEW");
                                Uri uri = Uri.parse(BiliBiliAPIs.pictureWebPage + picture.docId + "?tab=1&type=2");
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