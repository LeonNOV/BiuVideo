package com.leon.biuvideo.utils.pay;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class WeChatPayUtils {
    private final Context context;

    public WeChatPayUtils(Context context) {
        this.context = context;
    }

    /**
     * 检查微信客户端是否已安装
     */
    private boolean hasInstallWeChatClient() {
        String alipayPackageName = "com.tencent.mm";
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(alipayPackageName, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void startPay () {
        String url1="intent://platformapi/startapp?saId=10000007&" +
                "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2Fa6x01339a0oa09cw0ds024b%3F_s" +
                "%3Dweb-other&_t=1472443966571#Intent;" +
                "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";

        if (hasInstallWeChatClient()) {
            try {
                InputStream inputStream = context.getAssets().open("raw/we_chat_pay_qrcode.png");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "未安装微信客户端，可以使用支付宝来完成捐赠哦~(>ω･* )ﾉ", Toast.LENGTH_SHORT).show();
        }
    }
}
