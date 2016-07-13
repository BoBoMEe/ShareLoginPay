package com.bobomee.android.sharelogin.login.interfaces;

import android.app.Activity;
import android.content.Intent;

/**
 * Created on 2016/3/25.下午7:46.
 * @author bobomee.
 * wbwjx115@gmail.com
 */
public interface ILogin {

  /**
   * 执行登录
   *
   * @param activity 上下文activity
   * @param callback 登录callback
   */
  void doLogin(Activity activity, ILoginCallback callback);

  /**
   * {@link Activity#onActivityResult(int, int, Intent)}
   *
   * @return true拦截, false 不拦截
   */
  boolean onActivityResult(int requestCode, int resultCode, Intent data);

}
