package com.bobomee.android.sharelogin.login.manager;

import android.app.Activity;
import android.content.Intent;
import com.bobomee.android.sharelogin.login.interfaces.ILogin;
import com.bobomee.android.sharelogin.login.interfaces.ILoginCallback;
import com.bobomee.android.sharelogin.util.WxLSUtil;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;

/**
 * Created on 2016/3/25.下午10:00.
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class WeiXinLogin implements ILogin {

  // 微信相关
  private static IWXAPI api;

  private static ILoginCallback iLoginCallback;

  @Override public void prepare(Activity _activity) {
    WxLSUtil.init(_activity);
    api = WxLSUtil.getmIWXAPI();
  }

  @Override public void doLogin(Activity activity, ILoginCallback callback) {

    iLoginCallback = callback;

    login();
  }

  private void login() {
    if (WxLSUtil.isWXAppInstalledAndSupported(api)) {
      SendAuth.Req req = new SendAuth.Req();
      req.scope = "snsapi_userinfo";
      req.state = "wechat_sdk_demo";
      api.sendReq(req);//第三方发送消息给微信。
    } else {
      if (null != iLoginCallback) iLoginCallback.onError(new Exception("not support"));
    }
  }

  @Override public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    if (iLoginCallback != null) {
      return true;
    }
    return false;
  }

  public static IWXAPI getApi() {
    return api;
  }

  public static ILoginCallback getiLoginCallback() {
    return iLoginCallback;
  }

}
