package com.bobomee.android.sharelogin.login.model;

import org.json.JSONObject;

/**
 * Created on 16/1/15.下午10:13.
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class QQTokenModel {
  public String ret;
  public String pay_token;
  public String pf;
  public String query_authority_cost;
  public String authority_cost;
  public String openid;
  public String expires_in;
  public String pfkey;
  public String msg;
  public String access_token;
  public String login_cost;

  public static QQTokenModel parseToken(String json) {

    QQTokenModel qqTokenModel = null;

    try {
      JSONObject jsonObject = new JSONObject(json);
      final String ret = jsonObject.optString("ret");
      final String pay_token = jsonObject.optString("pay_token");
      final String pf = jsonObject.optString("pf");
      final String query_authority_cost = jsonObject.optString("query_authority_cost");
      final String authority_cost = jsonObject.optString("authority_cost");
      final String openid = jsonObject.optString("openid");
      final String expires_in = jsonObject.optString("expires_in");
      final String pfkey = jsonObject.optString("pfkey");
      final String msg = jsonObject.optString("msg");
      final String access_token = jsonObject.optString("access_token");
      final String login_cost = jsonObject.optString("login_cost");

      qqTokenModel = new QQTokenModel();
      qqTokenModel.ret = ret;
      qqTokenModel.pay_token = pay_token;
      qqTokenModel.pf = pf;
      qqTokenModel.query_authority_cost = query_authority_cost;
      qqTokenModel.authority_cost = authority_cost;
      qqTokenModel.openid = openid;
      qqTokenModel.expires_in = expires_in;
      qqTokenModel.pfkey = pfkey;
      qqTokenModel.msg = msg;
      qqTokenModel.access_token = access_token;
      qqTokenModel.login_cost = login_cost;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return qqTokenModel;
  }
}
