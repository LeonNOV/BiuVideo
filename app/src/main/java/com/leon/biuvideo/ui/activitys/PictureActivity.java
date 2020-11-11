package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.upMasterBean.UpPicture;
import com.leon.biuvideo.utils.ValueFormat;

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
        //https://space.bilibili.com/33915583
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

        picture_textView_desc.setText(picture.description);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.picture_back:
                this.finish();
                break;
            case R.id.picture_more:
                //创建popupWindow
                break;
            default:
                break;
        }
    }
}