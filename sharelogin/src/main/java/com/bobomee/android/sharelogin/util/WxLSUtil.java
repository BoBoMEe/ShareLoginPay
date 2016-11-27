package com.bobomee.android.sharelogin.util;

import android.content.Context;
import android.text.TextUtils;
import com.bobomee.android.sharelogin.login.LoginShareBlock;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created on 2016/11/27.下午1:06.
 *
 * @author bobomee.
 * @description
 */

public class WxLSUtil {

  private static IWXAPI mIWXAPI;

  public static void init(Context context) {
    String wechatAppId = LoginShareBlock.getInstance().getWechatAppId();
    if (!TextUtils.isEmpty(wechatAppId)) {
      mIWXAPI = WXAPIFactory.createWXAPI(context, wechatAppId);
      mIWXAPI.registerApp(wechatAppId);
    }
  }

  public static boolean isWXAppInstalledAndSupported(IWXAPI msgApi) {

    return msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI();
  }

  public static IWXAPI getmIWXAPI() {
    return mIWXAPI;
  }

  public static void detach() {
    mIWXAPI.detach();
  }
}
