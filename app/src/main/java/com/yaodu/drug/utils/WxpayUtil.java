package com.yaodu.drug.utils;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;

/**
 * 微信支付工具类
 * @author BoBoMEe
 */
public class WxpayUtil {
    public interface WeixinPay {
        @GET("http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android")
        Call<PayReq> contributors();
    }

    public static PayReq getWeixinPayInfo() {
        // Create a very simple REST adapter which points the GitHub API.
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our GitHub API interface.
        WeixinPay github = retrofit.create(WeixinPay.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<PayReq> call = github.contributors();

        // Fetch and print a list of the contributors to the library.
        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void weixinPay(final IWXAPI msgApi) {

        ThreadManager.getShortPool().execute(new Runnable() {
            @Override
            public void run() {
                PayReq req = getWeixinPayInfo();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                msgApi.sendReq(req);
            }
        });
    }
}
