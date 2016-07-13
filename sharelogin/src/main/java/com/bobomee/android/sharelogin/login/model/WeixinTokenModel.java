package com.bobomee.android.sharelogin.login.model;

import org.json.JSONObject;

/**
 * Created on 2015/11/10.下午10:13.
 *
 * @author Administrator.
 *         wbwjx115@gmail.com
 */
public class WeixinTokenModel {

  public String access_token;
  public String expires_in;

  public String refresh_token;

  public String openid;

  public String scope;

  public String unionid;

  public static WeixinTokenModel parseToken(String json) {

    WeixinTokenModel weixinTokenModel = null;

    try {
      JSONObject jsonObject = new JSONObject(json);
      final String accessToken = jsonObject.optString("access_token");
      final String openId = jsonObject.optString("openid");
      final String expires_in = jsonObject.optString("expires_in");
      final String refresh_token = jsonObject.optString("refresh_token");
      final String scope = jsonObject.optString("scope");
      final String unionid = jsonObject.optString("unionid");

      weixinTokenModel = new WeixinTokenModel();
      weixinTokenModel.access_token = accessToken;
      weixinTokenModel.openid = openId;
      weixinTokenModel.expires_in = expires_in;
      weixinTokenModel.refresh_token = refresh_token;
      weixinTokenModel.scope = scope;
      weixinTokenModel.unionid = unionid;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return weixinTokenModel;
  }

  @Override public String toString() {
    return "WeixinTokenModel{" +
        "access_token='" + access_token + '\'' +
        ", expires_in='" + expires_in + '\'' +
        ", refresh_token='" + refresh_token + '\'' +
        ", openid='" + openid + '\'' +
        ", scope='" + scope + '\'' +
        ", unionid='" + unionid + '\'' +
        '}';
  }
}
