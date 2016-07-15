package com.bobomee.android.sharelogin.share.interfaces;

/**
 * 分享回调接口
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public interface IShareCallBack {

  void onComplete(Object o);

  void onError(Exception e);

  void onCancel();

}
