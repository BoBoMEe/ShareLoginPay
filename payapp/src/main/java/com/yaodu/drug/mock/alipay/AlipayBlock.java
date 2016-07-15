package com.yaodu.drug.mock.alipay;

/**
 * 支付数据初始化
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class AlipayBlock {

  private static AlipayBlock mInstance;

  private AlipayBlock() {
  }

  public static AlipayBlock getInstance() {
    if (mInstance == null) {
      mInstance = new AlipayBlock();
    }
    return mInstance;
  }

  private String mAlipayPartner;

  private String mAlipaySeller;

  private String mAlipayRsa_private;

  /**
   * init alipay params
   */
  public void initAliPay(String alipayPartner, String alipaySeller, String alipayRsa_private) {
    this.mAlipayPartner = alipayPartner;
    this.mAlipaySeller = alipaySeller;
    this.mAlipayRsa_private = alipayRsa_private;
  }

  public String getAlipayPartner() {
    return mAlipayPartner;
  }

  public String getAlipayRsa_private() {
    return mAlipayRsa_private;
  }

  public String getAlipaySeller() {
    return mAlipaySeller;
  }
}
