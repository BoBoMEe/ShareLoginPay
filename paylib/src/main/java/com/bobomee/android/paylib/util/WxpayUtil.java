package com.bobomee.android.paylib.util;

import android.content.Context;
import android.text.TextUtils;
import com.bobomee.android.paylib.PayBlock;
import com.bobomee.android.paylib.interfaces.WxpayResultListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付工具类
 * @author BoBoMEe
 */
public class WxpayUtil {

    private static IWXAPI mIWXAPI;

    public WxpayUtil(Context context) {
        String wechatAppId = PayBlock.getInstance().getWechatAppId();
        if (!TextUtils.isEmpty(wechatAppId)) {
            mIWXAPI = WXAPIFactory.createWXAPI(context, wechatAppId);
            mIWXAPI.registerApp(wechatAppId);
        }
    }

    public static WxpayResultListener sWxpayResultListener;

    public static void weixinPay(final PayReq req,
        WxpayResultListener wxpayResultListener) {

        if (null != req && null != mIWXAPI) {
            sWxpayResultListener = wxpayResultListener;

            if (isWXAppInstalledAndSupported(mIWXAPI)) {
                ThreadManager.getShortPool().execute(new Runnable() {
                    @Override public void run() {
                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                        mIWXAPI.sendReq(req);
                    }
                });
            } else {
                if (null != sWxpayResultListener) sWxpayResultListener.notSupport();
            }
        }
    }

    public static boolean isWXAppInstalledAndSupported(IWXAPI msgApi) {

        return msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI();
    }

    public static WxpayResultListener getWxpayResultListener() {
        return sWxpayResultListener;
    }

    public static IWXAPI getmIWXAPI() {
        return mIWXAPI;
    }
}
