package com.leon.biuvideo.utils.pay;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.net.URISyntaxException;

public class AliPayUtils {
    private final Context context;

    public AliPayUtils(Context context) {
        this.context = context;
    }

    /**
     * 检查支付宝客户端是否已安装
     */
    private boolean hasInstallAlipayClient () {
        String alipayPackageName = "com.eg.android.AlipayGphone";
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

        if (hasInstallAlipayClient()) {
            try {
                Intent intent = Intent.parseUri(url1, Intent.URI_INTENT_SCHEME);
                context.startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "未安装支付宝客户端，可以使用微信来完成捐赠哦~(>ω･* )ﾉ", Toast.LENGTH_SHORT).show();
        }
    }
}
