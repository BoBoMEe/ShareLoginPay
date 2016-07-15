package com.bobomee.android.sharelogin.login.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.bobomee.android.sharelogin.login.LoginBlock;
import com.bobomee.android.sharelogin.login.interfaces.ILogin;
import com.bobomee.android.sharelogin.login.interfaces.ILoginCallback;
import com.bobomee.android.sharelogin.login.sinaapi.User;
import com.bobomee.android.sharelogin.login.sinaapi.UsersAPI;
import com.bobomee.android.sharelogin.login.util.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

/**
 * Created on 2016/3/25.下午9:51.
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class WeiboLogin implements ILogin {

  private Activity activity;
  private ILoginCallback iLoginCallback;

  /**
   * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
   */
  private SsoHandler mSsoHandler;

  /**
   * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
   * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
   * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
   * SharedPreferences 中。
   */
  private class AuthListener implements WeiboAuthListener {

    @Override public void onComplete(Bundle values) {
      // 从 Bundle 中解析 Token
      Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);
      // 从这里获取用户输入的 电话号码信息
      // String phoneNum = mAccessToken.getPhoneNum();
      if (null != mAccessToken && mAccessToken.isSessionValid()) {
        // 显示 Token
        // 保存 Token 到 SharedPreferences
        // 保存 Token 到 SharedPreferences

        AccessTokenKeeper.writeAccessToken(activity, mAccessToken);

        if (null != iLoginCallback) iLoginCallback.tokeCallBack(mAccessToken);

        String wbAppKey = LoginBlock.getInstance().getWbAppKey();

        if (!TextUtils.isEmpty(wbAppKey)) {
          //获取用户信息
          UsersAPI mUserAPI = new UsersAPI(activity, wbAppKey, mAccessToken);

          long uid = Long.parseLong(mAccessToken.getUid());// 获取openid

          mUserAPI.show(uid, mListener);
        }
      } else {
        // 以下几种情况，您会收到 Code：
        // 1. 当您未在平台上注册的应用程序的包名与签名时；
        // 2. 当您注册的应用程序包名与签名不正确时；
        // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
        String code = values.getString("code");

        if (null != iLoginCallback) iLoginCallback.onError(new Exception(code));
      }
    }

    @Override public void onCancel() {
      if (null != iLoginCallback) iLoginCallback.onCancel();
    }

    @Override public void onWeiboException(WeiboException e) {
      if (null != iLoginCallback) iLoginCallback.onError(e);
    }
  }

  private RequestListener mListener = new RequestListener() {
    @Override public void onComplete(String response) {
      if (!TextUtils.isEmpty(response)) {
        // 调用 User#parse 将JSON串解析成User对象
        User user = User.parse(response);
        if (null != iLoginCallback) iLoginCallback.infoCallBack(user);
      }
    }

    @Override public void onWeiboException(WeiboException e) {
      if (null != iLoginCallback)
      iLoginCallback.onError(e);
    }
  };

  @Override public void doLogin(Activity activity, ILoginCallback callback) {

    String wbAppKey = LoginBlock.getInstance().getWbAppKey();
    String wbRedirectUrl = LoginBlock.getInstance().getWbRedirectUrl();
    String wbScope = LoginBlock.getInstance().getWbScope();

    if (!TextUtils.isEmpty(wbAppKey) && !TextUtils.isEmpty(wbRedirectUrl) && !TextUtils.isEmpty(
        wbScope)) {
      //instance
      this.activity = activity;
      this.iLoginCallback = callback;
      AccessTokenKeeper.clear(activity);

      // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
      AuthInfo mAuthInfo = new AuthInfo(activity, wbAppKey, wbRedirectUrl, wbScope);
      mSsoHandler = new SsoHandler(activity, mAuthInfo);

      //login
      mSsoHandler.authorize(new AuthListener());
    }
  }

  @Override public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    if (null != iLoginCallback && null != mSsoHandler) {
      mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
      return true;
    }
    return false;
  }
}
