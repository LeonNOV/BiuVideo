package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Picture;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.PictureListAdapter;
import com.leon.biuvideo.beans.upMasterBean.UpPicture;
import com.leon.biuvideo.layoutManager.PictureLayoutManager;
import com.leon.biuvideo.ui.views.PictureViewer;
import com.leon.biuvideo.utils.MediaUtils;
import com.leon.biuvideo.utils.Paths;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.WebpSizes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PictureActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView
            picture_back,
            picture_more;

    private TextView
            picture_textView_title,
            picture_textView_time,
            picture_textView_view,
            picture_textView_like,
            picture_textView_desc;

    private RecyclerView picture_recyclerView;

    private UpPicture picture;

    private PopupWindow popupWindow;
    private Button picture_more_saveAll, picture_more_jumpToOrigin;

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
        picture = (UpPicture) extras.getSerializable("picture");
    }

    private void initView() {

        picture_back = findViewById(R.id.picture_back);
        picture_back.setOnClickListener(this);

        picture_more = findViewById(R.id.picture_more);
        picture_more.setOnClickListener(this);

        picture_textView_title =findViewById(R.id.picture_textView_title);
        picture_textView_time =findViewById(R.id.picture_textView_time);
        picture_textView_view =findViewById(R.id.picture_textView_view);
        picture_textView_like =findViewById(R.id.picture_textView_like);
        picture_textView_desc =findViewById(R.id.picture_textView_desc);

        picture_recyclerView = findViewById(R.id.picture_recyclerView);
    }

    private void initValue() {
        picture_textView_title.setText(picture.title);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

        Date time = new Date(picture.ctime * 1000);
        picture_textView_time.setText(sdf.format(time));

        picture_textView_view.setText(ValueFormat.generateCN(picture.view));

        picture_textView_like.setText(ValueFormat.generateCN(picture.like));

        //如果标题为空，则将picture_textView_title控件进行‘删除’
        String title = picture.title;
        if (title.equals("")) {
            picture_textView_title.setVisibility(View.GONE);
        } else {
            picture_textView_title.setText(title);
        }

        picture_textView_desc.setText(picture.description);

        int spanCount;
        WebpSizes.PicturePixelSize picturePixelSize;

        //判断要显示的列数
        if (picture.pictures.size() % 3 == 0) {
            picturePixelSize = WebpSizes.PicturePixelSize.MORE;
            spanCount = 3;
        } else if (picture.pictures.size() % 2 == 0) {
            picturePixelSize = WebpSizes.PicturePixelSize.DOUBLE;
            spanCount = 2;
        } else {
            picturePixelSize = WebpSizes.PicturePixelSize.SINGLE;
            spanCount = 1;
        }

        PictureListAdapter pictureListAdapter = new PictureListAdapter(getApplicationContext(), picture.pictures, picturePixelSize.value);

        pictureListAdapter.setOnPictureItemClickListener(new PictureListAdapter.OnPictureItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(PictureActivity.this, "点击了第" + (position + 1) + "张图片", Toast.LENGTH_SHORT).show();
                PictureViewer pictureViewer = new PictureViewer(PictureActivity.this, position, picture.pictures);
                pictureViewer.showAtLocation(PictureActivity.this.findViewById(R.id.picture_toolBar),
                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        PictureLayoutManager pictureLayoutManager = new PictureLayoutManager(getApplicationContext(), spanCount);

        picture_recyclerView.setLayoutManager(pictureLayoutManager);
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
                createPopupWindow(v);
                break;
            case R.id.picture_more_saveAll:
                //保存所有图片
                Toast.makeText(this, "正在保存图片", Toast.LENGTH_SHORT).show();

                popupWindow.dismiss();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int saveCounts = 0;

                        for (String url : picture.pictures) {
                            boolean b = MediaUtils.savePicture(getApplicationContext(), url);

                            if (b) saveCounts++;
                        }

                        Looper.prepare();
                        Toast.makeText(getApplicationContext(),
                                "保存成功" + saveCounts + "张,失败" + (picture.pictures.size() - saveCounts) + "张",
                                Toast.LENGTH_SHORT).show();

                        Looper.loop();
                    }
                }).start();

                break;
            case R.id.picture_more_jumpToOrigin:

                Intent intentOriginUrl = new Intent();
                intentOriginUrl.setAction("android.intent.action.VIEW");
                Uri uri = Uri.parse(Paths.pictureWebPage + picture.docId + "?tab=1&type=2");
                intentOriginUrl.setData(uri);
                startActivity(intentOriginUrl);

                popupWindow.dismiss();

                break;
            default:
                break;
        }
    }

    private void createPopupWindow(View view) {
        popupWindow = new PopupWindow(getApplicationContext());

        View popupView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.picture_popup_window, null);
        picture_more_saveAll = popupView.findViewById(R.id.picture_more_saveAll);
        picture_more_saveAll.setOnClickListener(this);

        picture_more_jumpToOrigin = popupView.findViewById(R.id.picture_more_jumpToOrigin);
        picture_more_jumpToOrigin.setOnClickListener(this);

        popupWindow = new PopupWindow(popupView, ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

        popupWindow.showAsDropDown(view, -10, 0);
    }
}