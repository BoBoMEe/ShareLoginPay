//#if def{lang} == cn
/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 * 
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */
//#elif def{lang} == en
/*
 * Offical Website:http://www.mob.com
 * Support QQ: 4006852216
 * Offical Wechat Account:ShareSDK   (We will inform you our updated news at the first time by Wechat, if we release a new version. If you get any problem, you can also contact us with Wechat, we will reply you within 24 hours.)
 * 
 * Copyright (c) 2013 mob.com. All rights reserved.
 */
//#endif

package com.bobomee.android.sharelogin.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.bobomee.android.sharelogin.login.LoginBlock;
import com.bobomee.android.sharelogin.login.WechatApiService;
import com.bobomee.android.sharelogin.login.interfaces.ILoginCallback;
import com.bobomee.android.sharelogin.login.manager.WeiXinLogin;
import com.bobomee.android.sharelogin.login.model.WeixinInfoModel;
import com.bobomee.android.sharelogin.login.model.WeixinTokenModel;
import com.bobomee.android.sharelogin.login.util.ThreadManager;
import com.bobomee.android.sharelogin.share.impl.WechatShare;
import com.bobomee.android.sharelogin.share.interfaces.IShareCallBack;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * 微信授权登录后的回调
 *
 * @author BoBoMEe
 */
public class WXAuthHandlerActivity extends AppCompatActivity implements IWXAPIEventHandler {

  // IWXAPI 是第三方app和微信通信的openapi接口
  private static IWXAPI api;
  private static ILoginCallback sILoginCallback;

  private static IShareCallBack sIShareCallBack;

  /**
   * BaseResp的getType函数获得的返回值，1:第三方授权， 2:分享
   */
  private static final int TYPE_LOGIN = 1;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // 注册微信
    api = WeiXinLogin.getApi();

    sILoginCallback = WeiXinLogin.getiLoginCallback();
    sIShareCallBack = WechatShare.getmIShareCallBack();
    if (null != api)
    api.handleIntent(getIntent(), this);
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    if (null != api)
    api.handleIntent(intent, this);
    finish();
  }

  @Override public void onReq(BaseReq arg0) {
  }

  /**
   * 授权登录，成功后会回调该方法
   */
  @Override public void onResp(BaseResp resp) {
    switch (resp.errCode) {
      case BaseResp.ErrCode.ERR_OK:
        if (resp.getType() == TYPE_LOGIN) {
          if (resp instanceof SendAuth.Resp) {
            final String code = ((SendAuth.Resp) resp).code;

            ThreadManager.getShortPool().execute(new Runnable() {
              @Override public void run() {
                WeixinTokenModel accessToken =
                    WechatApiService.getAccessToken(LoginBlock.getInstance().getWechatAppId(),
                        LoginBlock.getInstance().getWechatAppSecret(), code);

                if (null != sILoginCallback) sILoginCallback.tokeCallBack(accessToken);

                if (null != accessToken) {
                  WeixinInfoModel wechatUserInfo =
                      WechatApiService.getWechatUserInfo(accessToken.access_token,
                          accessToken.openid);

                  if (null != sILoginCallback) sILoginCallback.infoCallBack(wechatUserInfo);
                }
              }
            });
          }
        } else {
          if (null != sIShareCallBack) {
            sIShareCallBack.onComplete(resp);
          }
        }

        break;
      case BaseResp.ErrCode.ERR_USER_CANCEL:
        //取消
        if (resp.getType() == TYPE_LOGIN) {
          if (resp instanceof SendAuth.Resp && null != sILoginCallback) {
            sILoginCallback.onCancel();
          }
        } else {
          if (null != sIShareCallBack) {
            sIShareCallBack.onCancel();
          }
        }
        break;
      case BaseResp.ErrCode.ERR_AUTH_DENIED:
        // 拒绝
      default:
        //失败
        if (resp.getType() == TYPE_LOGIN) {
          if (resp instanceof SendAuth.Resp && null != sILoginCallback) {
            sILoginCallback.onError(new Exception(resp.errStr));
          }
        } else {
          if (null != sIShareCallBack) {
            sIShareCallBack.onError(new Exception(resp.errStr));
          }
        }
        break;
    }
    finish();
  }
}
