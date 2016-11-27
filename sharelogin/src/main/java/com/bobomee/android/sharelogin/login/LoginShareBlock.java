package com.bobomee.android.sharelogin.login;

/**
 * 登录appid 及 key数据
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public final class LoginShareBlock {

  private LoginShareBlock() {

  }

  private static LoginShareBlock instance;

  public static LoginShareBlock getInstance() {
    if (instance == null) {
      instance = new LoginShareBlock();
    }
    return instance;
  }

  private String mWechatAppId;

  private String mWechatAppSecret;

  private String mQQAppId;

  private String mWbAppKey;

  private String mWbRedirectUrl;

  private String mWbScope = "email,direct_messages_read,direct_messages_write,"
      + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
      + "follow_app_official_microblog,"
      + "invitation_write";

  public final void initWechat(String wechatAppId, String wechatAppSecret) {
    this.mWechatAppId = wechatAppId;
    this.mWechatAppSecret = wechatAppSecret;
  }

  public final void initQQ(String qqAppId) {
    this.mQQAppId = qqAppId;
  }

  public final void initWb(String wbAppKey, String wbRedirectUrl) {
    this.mWbAppKey = wbAppKey;
    this.mWbRedirectUrl = wbRedirectUrl;
  }

  public final String getQQAppId() {
    return mQQAppId;
  }

  public final String getWbAppKey() {
    return mWbAppKey;
  }

  public final String getWbRedirectUrl() {
    return mWbRedirectUrl;
  }

  public final String getWbScope() {
    return mWbScope;
  }

  public final String getWechatAppId() {
    return mWechatAppId;
  }

  public final String getWechatAppSecret() {
    return mWechatAppSecret;
  }
}
