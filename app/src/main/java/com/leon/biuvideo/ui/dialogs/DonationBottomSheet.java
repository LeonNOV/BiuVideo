package com.leon.biuvideo.ui.dialogs;

import android.app.Activity;
import android.content.Context;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leon.biuvideo.R;
import com.leon.biuvideo.utils.downloadUtils.ResourceDownloadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;

/**
 * @Author Leon
 * @Time 2021/5/16
 * @Desc 捐赠弹窗
 */
public class DonationBottomSheet extends BottomSheetDialog implements View.OnClickListener {
    private final Activity activity;
    private boolean isDonation = false;

    public DonationBottomSheet(@NotNull Context context, Activity activity) {
        super(context);

        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_amount_dialog);

        Window window = getWindow();

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(attributes);

        // 设置底部透明
        FrameLayout bottom = findViewById(R.id.design_bottom_sheet);
        if (bottom != null) {
            bottom.setBackgroundResource(android.R.color.transparent);
        }

        initView();
    }

    private void initView() {
        findViewById(R.id.donation_aliPay).setOnClickListener(this);
        findViewById(R.id.donation_wechatPay).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.donation_aliPay:
                boolean status = AlipayDonate.hasInstalledAlipayClient(getContext());
                if (status) {
                    AlipayDonate.startAlipayClient(activity, "a6x01339a0oa09cw0ds024b");
                    isDonation = true;
                }
                break;
            case R.id.donation_wechatPay:
                InputStream weChatPayQrcode = getContext().getResources().openRawResource(R.raw.we_chat_pay_qrcode);

                File resourcesPath = getContext().getExternalFilesDir(ResourceDownloadTask.RESOURCE);
                File picturesPath = new File(resourcesPath, ResourceDownloadTask.PICTURES);

                if (!picturesPath.exists()) {
                    picturesPath.mkdirs();
                }

                String qrPath = new File(picturesPath, "WeChatDonation.png").getAbsolutePath();
                WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weChatPayQrcode));
                WeiXinDonate.donateViaWeiXin(activity, qrPath);
                isDonation = true;

                break;
            default:
                break;
        }
    }

    public boolean isDonation() {
        return isDonation;
    }
}