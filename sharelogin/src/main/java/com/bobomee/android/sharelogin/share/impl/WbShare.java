package com.bobomee.android.sharelogin.share.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import com.bobomee.android.sharelogin.login.LoginShareBlock;
import com.bobomee.android.sharelogin.login.util.AccessTokenKeeper;
import com.bobomee.android.sharelogin.share.content.ShareContent;
import com.bobomee.android.sharelogin.share.interfaces.ContentType;
import com.bobomee.android.sharelogin.share.interfaces.IShare;
import com.bobomee.android.sharelogin.share.interfaces.IShareCallBack;
import com.bobomee.android.sharelogin.share.util.ShareUtil;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 微博分享
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class WbShare implements IShare {

  private IWeiboShareAPI mSinaAPI;
  private String mWbAppKey;
  private String mWbScope;
  private String mWbRedirectUrl;

  private Activity mActivity;

  private IShareCallBack mIShareCallBack;

  public WbShare(Activity activity) {
    mWbAppKey = LoginShareBlock.getInstance().getWbAppKey();

    if (!TextUtils.isEmpty(mWbAppKey)) {
      this.mActivity = activity;
      mSinaAPI = WeiboShareSDK.createWeiboAPI(activity, mWbAppKey);
      mSinaAPI.registerApp();
      mWbRedirectUrl = LoginShareBlock.getInstance().getWbRedirectUrl();
      mWbScope = LoginShareBlock.getInstance().getWbScope();
    }
  }

  @Override public void share(ShareContent shareContent, IShareCallBack iShareCallBack) {

    this.mIShareCallBack = iShareCallBack;

    if (null != mSinaAPI) {
      ContentType contentType = shareContent.getContentType();
      switch (contentType) {
        case TEXT:
          shareText(shareContent);
          break;
        case PICTURE:
          sharePicture(shareContent);
          break;
        case WEBPAG:
          shareWebPage(shareContent);
          break;
        case MUSIC:
          shareMusic(shareContent);
          break;
      }
    }
  }

  private void shareText(ShareContent shareContent) {

    //初始化微博的分享消息
    WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
    weiboMultiMessage.textObject = getTextObj(shareContent.getContent());
    //初始化从第三方到微博的消息请求
    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
    request.transaction = String.valueOf(System.currentTimeMillis());
    request.multiMessage = weiboMultiMessage;
    allInOneShare(mActivity, request);
  }

  private void sharePicture(ShareContent shareContent) {

    WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
    weiboMultiMessage.imageObject = getImageObj(shareContent.getImageUrl());
    //初始化从第三方到微博的消息请求
    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
    request.transaction = String.valueOf(System.currentTimeMillis());
    request.multiMessage = weiboMultiMessage;
    allInOneShare(mActivity, request);
  }

  private void shareWebPage(ShareContent shareContent) {

    WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
    weiboMultiMessage.textObject = getTextObj(shareContent.getContent());
    weiboMultiMessage.imageObject = getImageObj(shareContent.getImageUrl());
    // 初始化从第三方到微博的消息请求
    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
    // 用transaction唯一标识一个请求
    request.transaction = String.valueOf(System.currentTimeMillis());
    request.multiMessage = weiboMultiMessage;
    allInOneShare(mActivity, request);
  }

  private void shareMusic(ShareContent shareContent) {
    WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
    weiboMultiMessage.mediaObject = getMusicObj(shareContent);
    //初始化从第三方到微博的消息请求
    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
    request.transaction = String.valueOf(System.currentTimeMillis());
    request.multiMessage = weiboMultiMessage;
    allInOneShare(mActivity, request);
  }

  /**
   * 创建文本消息对象。
   *
   * @return 文本消息对象。
   */
  private TextObject getTextObj(String text) {
    TextObject textObject = new TextObject();
    textObject.text = text;
    return textObject;
  }

  /**
   * 创建图片消息对象。
   *
   * @return 图片消息对象。
   */
  private ImageObject getImageObj(String imageUrl) {
    ImageObject imageObject = new ImageObject();
    Bitmap bmp = BitmapFactory.decodeFile(imageUrl);
    imageObject.setImageObject(bmp);
    return imageObject;
  }

  /**
   * 创建多媒体（网页）消息对象。
   *
   * @return 多媒体（网页）消息对象。
   */
  private WebpageObject getWebpageObj(ShareContent shareContent) {
    WebpageObject mediaObject = new WebpageObject();
    mediaObject.identify = Utility.generateGUID();
    mediaObject.title = shareContent.getTitle();
    mediaObject.description = shareContent.getContent();

    // 设置 Bitmap 类型的图片到视频对象里
    Bitmap bmp = ShareUtil.extractThumbNail(shareContent.getImageUrl(), 150, 150, true);
    mediaObject.setThumbImage(bmp);
    mediaObject.actionUrl = shareContent.getUrl();
    mediaObject.defaultText = shareContent.getContent();
    return mediaObject;
  }

  /**
   * 创建多媒体（音乐）消息对象。
   *
   * @return 多媒体（音乐）消息对象。
   */
  private MusicObject getMusicObj(ShareContent shareContent) {
    // 创建媒体消息
    MusicObject musicObject = new MusicObject();
    musicObject.identify = Utility.generateGUID();
    musicObject.title = shareContent.getTitle();
    musicObject.description = shareContent.getContent();

    // 设置 Bitmap 类型的图片到视频对象里
    Bitmap bmp = BitmapFactory.decodeFile(shareContent.getImageUrl());
    musicObject.setThumbImage(bmp);
    musicObject.actionUrl = shareContent.getUrl();
    musicObject.dataUrl = mWbRedirectUrl;
    musicObject.dataHdUrl = mWbRedirectUrl;
    musicObject.duration = 10;
    musicObject.defaultText = shareContent.getContent();
    return musicObject;
  }

  private void allInOneShare(final Context context, SendMultiMessageToWeiboRequest request) {

    AuthInfo authInfo = new AuthInfo(context, mWbAppKey, mWbRedirectUrl, mWbScope);
    Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context);
    String token = "";
    if (accessToken != null) {
      token = accessToken.getToken();
    }

    // 使用网页分享的时候会回调
    mSinaAPI.sendRequest((Activity) context, request, authInfo, token, new WeiboAuthListener() {

      @Override public void onWeiboException(WeiboException arg0) {
        if (null != mIShareCallBack) mIShareCallBack.onError(arg0);
      }

      @Override public void onComplete(Bundle bundle) {
        Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
        AccessTokenKeeper.writeAccessToken(context, newToken);
        if (null != mIShareCallBack) mIShareCallBack.onComplete(bundle);
      }

      @Override public void onCancel() {
        if (null != mIShareCallBack) mIShareCallBack.onCancel();
      }
    });
  }

  /**
   * @see {@link Activity#onNewIntent}
   * 客户端回调需要重写此方法
   */
  public void onNewIntent(Intent intent, IWeiboHandler.Response response) {

    // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
    // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
    // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
    mSinaAPI.handleWeiboResponse(intent, response);
  }

  // 使用客户端分享的时候会回调
  public void onResponse(BaseResponse baseResp) {
    if (baseResp != null) {
      switch (baseResp.errCode) {
        case WBConstants.ErrorCode.ERR_OK:
          if (null != mIShareCallBack) mIShareCallBack.onComplete(baseResp.errMsg);
          break;
        case WBConstants.ErrorCode.ERR_CANCEL:
          if (null != mIShareCallBack) mIShareCallBack.onCancel();
          break;
        case WBConstants.ErrorCode.ERR_FAIL:
          if (null != mIShareCallBack) mIShareCallBack.onError(new Exception(baseResp.errMsg));
          break;
      }
    }
  }
}
