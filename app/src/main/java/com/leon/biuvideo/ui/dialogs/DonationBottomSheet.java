package com.leon.biuvideo.ui.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leon.biuvideo.R;
import com.leon.biuvideo.utils.downloadUtils.ResourceDownloadTask;
import com.leon.biuvideo.utils.pay.AliDonationUtils;
import com.leon.biuvideo.utils.pay.WeChatDonationUtils;

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
                boolean status = AliDonationUtils.hasInstalledAlipayClient(getContext());
                if (status) {
                    AliDonationUtils.startAlipayClient(activity, "a6x01339a0oa09cw0ds024b");
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

                File qrCodeFile = new File(picturesPath, "WeChatDonation.png");
                WeChatDonationUtils.saveDonateQrImage2SDCard(qrCodeFile.getAbsolutePath(), BitmapFactory.decodeStream(weChatPayQrcode));
                Toast.makeText(activity, "正在跳转中，请不要进行任何操作", Toast.LENGTH_SHORT).show();
                WeChatDonationUtils.donateViaWeiXin(activity, qrCodeFile);
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