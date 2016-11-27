package com.yaodu.drug.mock.alipay;

import com.bobomee.android.paylib.util.SignUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * 支付宝支付数据模拟
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class AlipayOrderInfo {

  /**
   * create the order info. 创建订单信息
   * 一般情况下,由服务器生成订单,客户端调起支付宝支付
   */
  public static String getOrderInfo(String subject, String body, String price) {

    // 签约合作者身份ID
    String orderInfo = "partner=" + "\"" + AlipayBlock.getInstance().getAlipayPartner() + "\"";

    // 签约卖家支付宝账号
    orderInfo += "&seller_id=" + "\"" + AlipayBlock.getInstance().getAlipaySeller() + "\"";

    // 商户网站唯一订单号
    orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

    // 商品名称
    orderInfo += "&subject=" + "\"" + subject + "\"";

    // 商品详情
    orderInfo += "&body=" + "\"" + body + "\"";

    // 商品金额
    orderInfo += "&total_fee=" + "\"" + price + "\"";

    // 服务器异步通知页面路径
    orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

    // 服务接口名称， 固定值
    orderInfo += "&service=\"mobile.securitypay.pay\"";

    // 支付类型， 固定值
    orderInfo += "&payment_type=\"1\"";

    // 参数编码， 固定值
    orderInfo += "&_input_charset=\"utf-8\"";

    // 设置未付款交易的超时时间
    // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
    // 取值范围：1m～15d。
    // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
    // 该参数数值不接受小数点，如1.5h，可转换为90m。
    orderInfo += "&it_b_pay=\"30m\"";

    // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
    // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

    // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
    orderInfo += "&return_url=\"m.alipay.com\"";

    // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
    // orderInfo += "&paymethod=\"expressGateway\"";

    return orderInfo;
  }

  /**
   * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
   */
  private static String getOutTradeNo() {
    SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
    Date date = new Date();
    String key = format.format(date);

    Random r = new Random();
    key = key + r.nextInt();
    key = key.substring(0, 15);
    return key;
  }

  /**
   * sign the order info. 对订单信息进行签名
   *
   * @param content 待签名订单信息
   */
  public static String sign(String content) {
    return SignUtils.sign(content, AlipayBlock.getInstance().getAlipayRsa_private());
  }
}
