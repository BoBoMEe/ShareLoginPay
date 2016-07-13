package com.bobomee.android.sharelogin.login;

/**
 * 登录appid 及 key数据
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class LoginBlock {

  private LoginBlock() {

  }

  private static LoginBlock instance;

  public static LoginBlock getInstance() {
    if (instance == null) {
      instance = new LoginBlock();
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

  public void initWechatLogin(String wechatAppId, String wechatAppSecret) {
    this.mWechatAppId = wechatAppId;
    this.mWechatAppSecret = wechatAppSecret;
  }

  public void initQQLogin(String qqAppId) {
    this.mQQAppId = qqAppId;
  }

  public void initWbLogin(String wbAppKey, String wbRedirectUrl) {
    this.mWbAppKey = wbAppKey;
    this.mWbRedirectUrl = wbRedirectUrl;
  }

  public String getQQAppId() {
    return mQQAppId;
  }

  public String getWbAppKey() {
    return mWbAppKey;
  }

  public String getWbRedirectUrl() {
    return mWbRedirectUrl;
  }

  public String getWbScope() {
    return mWbScope;
  }

  public String getWechatAppId() {
    return mWechatAppId;
  }

  public String getWechatAppSecret() {
    return mWechatAppSecret;
  }
}
