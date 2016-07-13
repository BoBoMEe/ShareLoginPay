package com.bobomee.android.sharelogin.login;

import com.bobomee.android.sharelogin.login.model.WeixinInfoModel;
import com.bobomee.android.sharelogin.login.model.WeixinTokenModel;
import com.bobomee.android.sharelogin.login.util.HttpUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信登录token info 获取接口
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class WechatApiService {

  private static final String API_URL = "https://api.weixin.qq.com";

  private static Map<String, String> infoParams = new HashMap<>();

  /**
   * 通过code获取access_token
   */
  public static WeixinTokenModel getAccessToken(String appid, String secret, String code) {
    String url = API_URL + "/sns/oauth2/access_token";

    Map<String, String> tokenParams = new HashMap<>();
    tokenParams.put("appid", appid);
    tokenParams.put("secret", secret);
    tokenParams.put("code", code);
    tokenParams.put("grant_type", "authorization_code");
    try {
      String fullUrl = HttpUtil.appendQueryParams(url, tokenParams);
      String s = HttpUtil.get(fullUrl);
      return WeixinTokenModel.parseToken(s);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取用户个人信息（UnionID机制）
   */
  public static WeixinInfoModel getWechatUserInfo(String access_token, String openid) {
    String url = API_URL + "/sns/userinfo";

    Map<String, String> infoParams = new HashMap<>();
    infoParams.put("access_token", access_token);
    infoParams.put("openid", openid);

    try {
      String fullUrl = HttpUtil.appendQueryParams(url, infoParams);
      String s = HttpUtil.get(fullUrl);

      return WeixinInfoModel.parseInfo(s);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }




}
