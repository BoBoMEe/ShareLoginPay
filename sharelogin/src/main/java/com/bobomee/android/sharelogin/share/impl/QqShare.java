package com.bobomee.android.sharelogin.share.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.bobomee.android.sharelogin.login.LoginShareBlock;
import com.bobomee.android.sharelogin.share.content.ShareContent;
import com.bobomee.android.sharelogin.share.interfaces.IShare;
import com.bobomee.android.sharelogin.share.interfaces.IShareCallBack;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * QQ 分享
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class QqShare implements IShare {

  private QQShare mQQShare;

  private Activity mActivity;
  private Tencent mTencent;
  private IShareCallBack iShareCallBack;

  public QqShare(Activity activity) {
    String qqAppId = LoginShareBlock.getInstance().getQQAppId();
    if (!TextUtils.isEmpty(qqAppId)){
      this.mActivity = activity;
      mTencent = Tencent.createInstance(qqAppId, activity);
     this.mQQShare = new QQShare(activity, mTencent.getQQToken());
    }
  }

  /**
   * 设置分享内容
   * 参数说明
   * QzoneShare.SHARE_TO_QQ_KEY_TYPE	选填	Int	SHARE_TO_QZONE_TYPE_IMAGE_TEXT（图文）
   * QzoneShare.SHARE_TO_QQ_TITLE	必填	Int	分享的标题，最多200个字符。
   * QzoneShare.SHARE_TO_QQ_SUMMARY	选填	String	分享的摘要，最多600字符。
   * QzoneShare.SHARE_TO_QQ_TARGET_URL	必填	String	需要跳转的链接，URL字符串。
   * QzoneShare.SHARE_TO_QQ_IMAGE_URL	选填	String	分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：图片最多支持9张图片，多余的图片会被丢弃）。
   */

  @Override public void share(ShareContent shareContent, final IShareCallBack iShareCallBack) {

    this.iShareCallBack = iShareCallBack;

    final Bundle bundle = new Bundle();

    String title = shareContent.getTitle();
    if (!TextUtils.isEmpty(title))
    bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
    String url = shareContent.getUrl();
    if (!TextUtils.isEmpty(url))
    bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
    String content = shareContent.getContent();
    if (!TextUtils.isEmpty(content))
    bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
    String imageUrl = shareContent.getImageUrl();
    if (!TextUtils.isEmpty(imageUrl))
      bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
    bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);

    if (null != mQQShare){
      mQQShare.shareToQQ(mActivity, bundle, baseUiListener);
    }

  }

  private IUiListener baseUiListener = new IUiListener() {
    @Override public void onComplete(Object o) {
      if (null != iShareCallBack)iShareCallBack.onComplete(o);
    }

    @Override public void onError(UiError uiError) {
      if (null != iShareCallBack)iShareCallBack.onError(new Exception(uiError.errorDetail));
    }

    @Override public void onCancel() {
      if (null != iShareCallBack)iShareCallBack.onCancel();
    }
  };

  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == Constants.REQUEST_QQ_SHARE && resultCode == Constants.ACTIVITY_OK) {
      mTencent.handleLoginData(data, baseUiListener);
      return true;
    }
    return false;
  }
}
