package com.bobomee.android.sharelogin.login.model;

import org.json.JSONObject;

/**
 * Created on 2015/11/10.下午10:13.
 *
 * @author Administrator.
 *         wbwjx115@gmail.com
 */
public class WeixinInfoModel {

  public String openid;
  public String nickname;
  public int sex;
  public String language;
  public String city;
  public String province;

  public String country;

  public String headimgurl;

  public String unionid;

  public static WeixinInfoModel parseInfo(String json) {

    WeixinInfoModel weixinInfoModel = null;
    try {
      JSONObject jsonObject = new JSONObject(json);

      final String openid = jsonObject.optString("openid");
      final String nickname = jsonObject.optString("nickname");
      final int sex = jsonObject.optInt("sex");
      final String language = jsonObject.optString("language");
      final String city = jsonObject.optString("city");
      final String province = jsonObject.optString("province");
      final String country = jsonObject.optString("country");
      final String headimgurl = jsonObject.optString("headimgurl");
      final String unionid = jsonObject.optString("unionid");

      weixinInfoModel = new WeixinInfoModel();
      weixinInfoModel.openid = openid;
      weixinInfoModel.nickname = nickname;
      weixinInfoModel.sex = sex;
      weixinInfoModel.language = language;
      weixinInfoModel.city = city;
      weixinInfoModel.province = province;
      weixinInfoModel.country = country;
      weixinInfoModel.headimgurl = headimgurl;
      weixinInfoModel.unionid = unionid;

      return weixinInfoModel;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return weixinInfoModel;
  }

  @Override public String toString() {
    return "WeixinInfoModel{" +
        "city='" + city + '\'' +
        ", openid='" + openid + '\'' +
        ", nickname='" + nickname + '\'' +
        ", sex=" + sex +
        ", language='" + language + '\'' +
        ", province='" + province + '\'' +
        ", country='" + country + '\'' +
        ", headimgurl='" + headimgurl + '\'' +
        ", unionid='" + unionid + '\'' +
        '}';
  }
}
