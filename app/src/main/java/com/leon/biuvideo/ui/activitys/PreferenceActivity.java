package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.leon.biuvideo.R;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
    }

    /**
     *                 //设置Dialog显示内容
     *                 ArrayList<AboutBean> aboutBeans = new ArrayList<>();
     *                 for (int i = 1; i <= 10; i++) {
     *                     AboutBean aboutBean = new AboutBean();
     *
     *                     aboutBean.title = "标题" + i;
     *                     aboutBean.license = "许可许可许可" + i;
     *                     aboutBean.desc = "内容内容内容内容内容内容内容内容" + i;
     *
     *                     aboutBeans.add(aboutBean);
     *                 }
     *
     *                 //显示Dialog
     *                 AboutDialog aboutDialog = new AboutDialog(MainActivity.this, aboutBeans);
     *                 aboutDialog.setOnClickBottomListener(new AboutDialog.OnClickBottomListener() {
     *                     @Override
     *                     public void onCloseClick() {
     *                         aboutDialog.dismiss();
     *                     }
     *                 });
     *
     *                 aboutDialog.show();
     *
     *                 popupWindow.dismiss();
     */
}