package com.bobomee.android.sharelogin.login.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.bobomee.android.sharelogin.login.LoginBlock;
import com.bobomee.android.sharelogin.login.interfaces.ILogin;
import com.bobomee.android.sharelogin.login.interfaces.ILoginCallback;
import com.bobomee.android.sharelogin.login.model.QQInfoModel;
import com.bobomee.android.sharelogin.login.model.QQTokenModel;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import org.json.JSONObject;

/**
 * Created on 2016/3/25.下午7:47.
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class QQLogin implements ILogin {

  private final int INFO = 0;
  private final int TOKEN = 1;
  private static Tencent mTencent;
  private BaseUiListener baseUiListener;

  private Activity activity;
  private ILoginCallback iLoginCallback;

  private void initOpenidAndToken(JSONObject jsonObject) {
    try {
      String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
      String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
      String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
      if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
        mTencent.setAccessToken(token, expires);
        mTencent.setOpenId(openId);
      }
    } catch (Exception e) {
    }
  }

  class BaseUiListener implements IUiListener {

    private int type;
    private Context context;

    //V2.0版本，参数类型由JSONObject 改成了Object,具体类型参考api文档
    public BaseUiListener(Context context, int i) {
      this.context = context;
      this.type = i;
    }

    @Override public void onComplete(Object o) {
      JSONObject object = (JSONObject) o;
      doComplete(object);
    }

    //在这里可以做一些登录成功的处理
    protected void doComplete(Object values) {

      if (type == TOKEN) {
        QQTokenModel qqTokenModel = QQTokenModel.parseToken(values.toString());
        initOpenidAndToken((JSONObject) values);
        if (null != iLoginCallback) iLoginCallback.tokeCallBack(qqTokenModel);
        UserInfo userInfo = new UserInfo(context, mTencent.getQQToken());
        userInfo.getUserInfo(new BaseUiListener(activity, INFO));
      } else {
        QQInfoModel qqInfoModel = QQInfoModel.parseInfo(values.toString());
        if (null != iLoginCallback) iLoginCallback.infoCallBack(qqInfoModel);
      }
    }

    //在这里可以做登录失败的处理
    @Override public void onError(UiError e) {
      if (null != iLoginCallback) iLoginCallback.onError(new Exception(e.errorDetail));
    }

    //在这里可以做登录被取消的处理
    @Override public void onCancel() {
      if (null != iLoginCallback) iLoginCallback.onCancel();
    }
  }

  @Override public void doLogin(Activity activity, ILoginCallback callback) {

    //instance
    this.activity = activity;
    this.iLoginCallback = callback;

    mTencent = Tencent.createInstance(LoginBlock.getInstance().getQQAppId(), activity);

    //login
    if (!mTencent.isSessionValid()) {
      mTencent.login(activity, "all", baseUiListener = new BaseUiListener(activity, TOKEN));
    } else {
      mTencent.logout(activity);
    }
  }

  @Override public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == Constants.REQUEST_API
        && resultCode == Constants.RESULT_LOGIN
        && null != iLoginCallback) {
      mTencent.handleLoginData(data, baseUiListener);
      return true;
    }
    return false;
  }
}
