package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.views.TagView;

import org.jetbrains.annotations.NotNull;

/**
 * @Author Leon
 * @Time 2021/5/16
 * @Desc 关于弹窗
 */
public class AboutAppDialog extends AlertDialog implements View.OnClickListener {
    private final static String GITEE = "https://gitee.com/leon_xf/biu-video";
    private final static String GITHUB = "https://github.com/LeonNOV/BiuVideo";

    public AboutAppDialog(@NotNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app_dialog);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setWindowAnimations(R.style.paning_anim_style);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(attributes);

        initView();
    }

    private void initView() {
        ((TagView) findViewById(R.id.about_app_version)).setRightValue(getContext().getString(R.string.versionName));
        findViewById(R.id.about_app_gitee).setOnClickListener(this);
        findViewById(R.id.about_app_github).setOnClickListener(this);
        findViewById(R.id.about_app_group).setOnClickListener(this);
        findViewById(R.id.about_app_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.about_app_gitee:
                intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri uriGitee = Uri.parse(GITEE);
                intent.setData(uriGitee);
                getContext().startActivity(intent);
                break;
            case R.id.about_app_github:
                intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri uriGithub = Uri.parse(GITHUB);
                intent.setData(uriGithub);
                getContext().startActivity(intent);
                break;
            case R.id.about_app_group:
                intent = new Intent();
                intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + "f7iJCgrDQnvPELv8QrJizOtBYTymrh5c"));
                try {
                    getContext().startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "调起QQ失败，请检查QQ是否为最新版或是否已安装QQ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.about_app_close:
                dismiss();
                break;
            default:
                break;
        }
    }
}
