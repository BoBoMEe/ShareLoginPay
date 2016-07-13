package com.bobomee.android.sharelogin.login.model;

import org.json.JSONObject;

/**
 * Created on 16/1/15.下午10:13.
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class QQInfoModel {
  public String is_yellow_year_vip;
  public String ret;
  public String figureurl_qq_1;
  public String level;
  public String figureurl_qq_2;
  public String nickname;
  public String yellow_vip_level;
  public String is_lost;
  public String msg;
  public String city;
  public String figureurl_1;
  public String vip;

  public String figureurl_2;
  public String province;
  public String is_yellow_vip;
  public String gender;
  public String figureurl;

  public static QQInfoModel parseInfo(String json) {

    QQInfoModel qqInfoModel = null;

    try {
      JSONObject jsonObject = new JSONObject(json);
      final String ret = jsonObject.optString("ret");
      final String is_yellow_year_vip = jsonObject.optString("is_yellow_year_vip");
      final String figureurl_qq_1 = jsonObject.optString("figureurl_qq_1");
      final String level = jsonObject.optString("level");
      final String figureurl_qq_2 = jsonObject.optString("figureurl_qq_2");
      final String nickname = jsonObject.optString("nickname");
      final String yellow_vip_level = jsonObject.optString("yellow_vip_level");
      final String is_lost = jsonObject.optString("is_lost");
      final String msg = jsonObject.optString("msg");
      final String city = jsonObject.optString("city");
      final String figureurl_1 = jsonObject.optString("figureurl_1");
      final String vip = jsonObject.optString("vip");
      final String figureurl_2 = jsonObject.optString("figureurl_2");
      final String province = jsonObject.optString("province");
      final String is_yellow_vip = jsonObject.optString("is_yellow_vip");
      final String gender = jsonObject.optString("gender");
      final String figureurl = jsonObject.optString("figureurl");

      qqInfoModel = new QQInfoModel();

      qqInfoModel.ret = ret;
      qqInfoModel.is_yellow_year_vip = is_yellow_year_vip;
      qqInfoModel.figureurl_qq_1 = figureurl_qq_1;
      qqInfoModel.level = level;
      qqInfoModel.figureurl_qq_2 = figureurl_qq_2;
      qqInfoModel.nickname = nickname;
      qqInfoModel.yellow_vip_level = yellow_vip_level;
      qqInfoModel.is_lost = is_lost;
      qqInfoModel.msg = msg;
      qqInfoModel.city = city;
      qqInfoModel.figureurl_1 = figureurl_1;
      qqInfoModel.vip = vip;

      qqInfoModel.figureurl_2 = figureurl_2;
      qqInfoModel.province = province;
      qqInfoModel.is_yellow_vip = is_yellow_vip;
      qqInfoModel.gender = gender;
      qqInfoModel.figureurl = figureurl;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return qqInfoModel;
  }
}
