package com.bobomee.android.paylib;

/**
 * 支付数据初始化
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class PayBlock {

  private static PayBlock mInstance;

  private PayBlock() {
  }

  public static PayBlock getInstance() {
    if (mInstance == null) {
      mInstance = new PayBlock();
    }
    return mInstance;
  }

  private String mWechatAppId;

  /**
   * init  wechat pay params
   */
  public void initWechatPay(String wechatAppId) {
    this.mWechatAppId = wechatAppId;
  }

  public String getWechatAppId() {
    return mWechatAppId;
  }
}
