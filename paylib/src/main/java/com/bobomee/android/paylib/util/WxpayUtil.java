package com.bobomee.android.paylib.util;

import com.bobomee.android.paylib.interfaces.WxpayResultListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;

/**
 * 微信支付工具类
 * @author BoBoMEe
 */
public class WxpayUtil {

    public static WxpayResultListener sWxpayResultListener;

    public static void weixinPay(final IWXAPI msgApi, final PayReq req,

        WxpayResultListener wxpayResultListener) {

        ThreadManager.getShortPool().execute(new Runnable() {
            @Override
            public void run() {
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                msgApi.sendReq(req);
            }
        });

        sWxpayResultListener = wxpayResultListener;
    }

    public static boolean isWXAppInstalledAndSupported(IWXAPI msgApi) {

        return msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI();
    }

    public static WxpayResultListener getWxpayResultListener() {
        return sWxpayResultListener;
    }
}
