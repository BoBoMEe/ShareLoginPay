package com.bobomee.android.paylib.model;

import android.text.TextUtils;

/**
 * 支付宝获取trade_no
 * @author BoBoMEe
 */
public class AliPayResultS {
    private String out_trade_no;

    public AliPayResultS(String result) {
        if (TextUtils.isEmpty(result))
            return;

        String[] resultParams = result.split("&");
        for (String resultParam : resultParams) {
            if (resultParam.startsWith("out_trade_no")) {
                out_trade_no = gatValue(resultParam, "out_trade_no");
            }
        }


    }

    private String gatValue(String content, String key) {
        String prefix = key + "=\"";
        return content.substring(content.indexOf(prefix) + prefix.length(),
                content.lastIndexOf("\""));
    }

    public String getResult() {
        return out_trade_no;
    }
}
