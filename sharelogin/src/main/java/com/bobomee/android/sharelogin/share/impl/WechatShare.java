package com.bobomee.android.sharelogin.share.impl;

import android.app.Activity;
import android.graphics.Bitmap;
import com.bobomee.android.sharelogin.login.util.ThreadManager;
import com.bobomee.android.sharelogin.share.content.ShareContent;
import com.bobomee.android.sharelogin.share.interfaces.ContentType;
import com.bobomee.android.sharelogin.share.interfaces.IShare;
import com.bobomee.android.sharelogin.share.interfaces.IShareCallBack;
import com.bobomee.android.sharelogin.share.interfaces.ShareType;
import com.bobomee.android.sharelogin.share.util.BitmapUtil;
import com.bobomee.android.sharelogin.share.util.ShareUtil;
import com.bobomee.android.sharelogin.util.WxLSUtil;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;

/**
 * 微信分享
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class WechatShare implements IShare {

  private static final int THUMB_SIZE = 116;

  private static IWXAPI mIWXAPI;

  private static  IShareCallBack mIShareCallBack;

  /**
   * friends
   */
  public static final int WEIXIN_SHARE_TYPE_TALK = SendMessageToWX.Req.WXSceneSession;

  /**
   * friends TimeLine
   */
  public static final int WEIXIN_SHARE_TYPE_FRENDS = SendMessageToWX.Req.WXSceneTimeline;

  public WechatShare(Activity activity) {

    WxLSUtil.init(activity);
    mIWXAPI = WxLSUtil.getmIWXAPI();

  }

  @Override public void share(ShareContent shareContent, IShareCallBack iShareCallBack) {
    mIShareCallBack = iShareCallBack;

    if (null != mIWXAPI) {
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

    String text = shareContent.getContent();
    //初始化一个WXTextObject对象
    WXTextObject textObj = new WXTextObject();
    textObj.text = text;
    //用WXTextObject对象初始化一个WXMediaMessage对象
    WXMediaMessage msg = new WXMediaMessage();
    msg.mediaObject = textObj;
    msg.description = text;
    //构造一个Req
    SendMessageToWX.Req req = new SendMessageToWX.Req();
    //transaction字段用于唯一标识一个请求
    req.transaction = String.valueOf(System.currentTimeMillis());
    req.message = msg;
    //发送的目标场景， 可以选择发送到会话 WXSceneSession 或者朋友圈 WXSceneTimeline。 默认发送到会话。
    ShareType shareType = shareContent.getShareType();
    if (shareType == ShareType.WECHAT) {
      req.scene = WEIXIN_SHARE_TYPE_TALK;
    } else {
      req.scene = WEIXIN_SHARE_TYPE_FRENDS;
    }
    mIWXAPI.sendReq(req);
  }

  private void sharePicture(ShareContent shareContent) {
    WXImageObject imgObj = new WXImageObject();
    WXMediaMessage msg = new WXMediaMessage();
    msg.mediaObject = imgObj;

    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = String.valueOf(System.currentTimeMillis());
    req.message = msg;
    ShareType shareType = shareContent.getShareType();
    if (shareType == ShareType.WECHAT) {
      req.scene = WEIXIN_SHARE_TYPE_TALK;
    } else {
      req.scene = WEIXIN_SHARE_TYPE_FRENDS;
    }
    sendShare(shareContent.getImageUrl(), req);
  }

  private void shareWebPage(final ShareContent shareContent) {
    WXWebpageObject webpage = new WXWebpageObject();
    webpage.webpageUrl = shareContent.getUrl();
    final WXMediaMessage msg = new WXMediaMessage(webpage);
    msg.title = shareContent.getTitle();
    msg.description = shareContent.getContent();

    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = String.valueOf(System.currentTimeMillis());
    req.message = msg;
    ShareType shareType = shareContent.getShareType();
    if (shareType == ShareType.WECHAT) {
      req.scene = WEIXIN_SHARE_TYPE_TALK;
    } else {
      req.scene = WEIXIN_SHARE_TYPE_FRENDS;
    }
    sendShare(shareContent.getImageUrl(), req);
  }

  private void shareMusic(ShareContent shareContent) {

    WXMusicObject music = new WXMusicObject();
    //Str1+"#wechat_music_url="+str2 ;str1是网页地址，str2是音乐地址。

    music.musicUrl = shareContent.getUrl() + "#wechat_music_url=" + shareContent.getMusicUrl();
    WXMediaMessage msg = new WXMediaMessage(music);
    msg.title = shareContent.getTitle();
    msg.description = shareContent.getContent();

    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = String.valueOf(System.currentTimeMillis());
    req.message = msg;
    ShareType shareType = shareContent.getShareType();
    if (shareType == ShareType.WECHAT) {
      req.scene = WEIXIN_SHARE_TYPE_TALK;
    } else {
      req.scene = WEIXIN_SHARE_TYPE_FRENDS;
    }
    sendShare(shareContent.getImageUrl(), req);
  }

  private void sendShare(final String imageUrl, final SendMessageToWX.Req req) {

    ThreadManager.getShortPool().execute(new Runnable() {
      @Override public void run() {
        try {
          Bitmap image = BitmapUtil.getBitmapFromUrl(imageUrl);
          if (image != null) {
            // todo image.length <= 10485760
            if (req.message.mediaObject instanceof WXImageObject) {
              req.message.mediaObject = new WXImageObject(image);
            }
            req.message.thumbData =
                ShareUtil.bmpToByteArray(BitmapUtil.scaleCenterCrop(image, THUMB_SIZE, THUMB_SIZE));
          }
          // 就算图片没有了 尽量能发出分享
          mIWXAPI.sendReq(req);
        } catch (Throwable throwable) {

        }
      }
    });
  }

  public static IShareCallBack getmIShareCallBack() {
    return mIShareCallBack;
  }

}
