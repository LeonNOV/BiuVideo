package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.views.SimpleSnackBar;

/**
 * 关于本App的信息
 */
public class AboutBiuVideoDialog extends AlertDialog implements View.OnClickListener {
    private final Context context;

    public AboutBiuVideoDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_biu_video_dialog);

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initView();
    }

    private void initView() {
        ImageView about_biu_video_imageView_jumpToGitee = findViewById(R.id.about_biu_video_imageView_jumpToGitee);
        about_biu_video_imageView_jumpToGitee.setOnClickListener(this);

//        ImageView about_biu_video_imageView_jumpToGithub = findViewById(R.id.about_biu_video_imageView_jumpToGithub);
//        about_biu_video_imageView_jumpToGithub.setOnClickListener(this);

        ImageView about_biu_video_join_group = findViewById(R.id.about_biu_video_join_group);
        about_biu_video_join_group.setOnClickListener(this);

        TextView about_biu_video_textView_close = findViewById(R.id.about_biu_video_textView_close);
        about_biu_video_textView_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_biu_video_imageView_jumpToGitee:
                //跳转到Gitee
                Intent intentGitee = new Intent();
                intentGitee.setAction("android.intent.action.VIEW");
                Uri uriGitee = Uri.parse("https://gitee.com/leon_xf/biu-video");
                intentGitee.setData(uriGitee);
                context.startActivity(intentGitee);

                break;
            /*case R.id.about_biu_video_imageView_jumpToGithub:
                //跳转到GitHub
                Intent intentGithub = new Intent();
                intentGithub.setAction("android.intent.action.VIEW");
                Uri uriGithub = Uri.parse("adwadawdwadaw");
                intentGithub.setData(uriGithub);
                context.startActivity(intentGithub);

                break;*/
            case R.id.about_biu_video_join_group:

                Intent intent = new Intent();
                intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + "f7iJCgrDQnvPELv8QrJizOtBYTymrh5c"));
                // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    SimpleSnackBar.make(v, "调起QQ失败，请检查QQ是否为最新版或是否已安装QQ", SimpleSnackBar.LENGTH_SHORT).show();
                }

                break;
            default:
                dismiss();
                break;

        }
    }
}
