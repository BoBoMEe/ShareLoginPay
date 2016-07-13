package com.bobomee.android.sharelogin.login.interfaces;

/**
 * Created on 2016/3/25.下午9:50.
 * @author bobomee.
 * wbwjx115@gmail.com
 */
public interface ILoginCallback<TOKEN, INFO> {

  /**
   * 登录成功
   *
   * @param token 返回token
   */
  void tokeCallBack(TOKEN token);

  /**
   * 登录成功
   *
   * @param info 返回info
   */

  void infoCallBack(INFO info);

  /**
   * 登录失败
   */
  void onError(Exception e);

  /**
   * 取消登录
   */
  void onCancel();
}
