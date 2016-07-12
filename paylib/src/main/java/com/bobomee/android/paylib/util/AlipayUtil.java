package com.bobomee.android.paylib.util;

import android.app.Activity;
import com.alipay.sdk.app.PayTask;
import com.bobomee.android.paylib.interfaces.AlipayResultListener;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 支付宝支付工具类
 * @author BoBoMEe
 *
 */
public class AlipayUtil {

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public static String getSignType() {
        return "sign_type=\"RSA\"";
    }

    public static void aliPay(final Activity activity, String orderInfo, String sign,
        final AlipayResultListener listener) {

        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        ThreadManager.getShortPool().execute(new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);
                // 回调支付结果
                if (null != listener) listener.payResult(result);

            }
        });
    }
}
