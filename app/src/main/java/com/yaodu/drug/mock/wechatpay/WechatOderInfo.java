package com.yaodu.drug.mock.wechatpay;

import com.tencent.mm.sdk.modelpay.PayReq;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 微信orderinfo模拟,
 * 这里一般有服务器实现,
 * 这里使用了官方demo中的支付数据
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public class WechatOderInfo {

  public static PayReq getWeixinPayReq(String payInfo) {
    PayReq req = null;
    try {
      req = new PayReq();
      JSONObject json = new JSONObject(payInfo);

      req.appId = json.getString("appid");
      req.partnerId = json.getString("partnerid");
      req.prepayId = json.getString("prepayid");
      req.nonceStr = json.getString("noncestr");
      req.timeStamp = json.getString("timestamp");
      req.packageValue = json.getString("package");
      req.sign = json.getString("sign");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return req;
  }

  public interface WeixinPay {
    @GET("app_pay.php?plat=android") Observable<ResponseBody> contributors();
  }

  public static Observable<String> getWeixinPayInfo() {
    // Create a very simple REST adapter which points the GitHub API.
    Retrofit retrofit = new Retrofit.Builder().client(new OkHttpClient())
        .baseUrl("http://wxpay.weixin.qq.com/pub_v2/app/")
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    // Create an instance of our GitHub API interface.
    WeixinPay weixinPay = retrofit.create(WeixinPay.class);

    return weixinPay.contributors()
        .subscribeOn(Schedulers.io())
        .map(new Func1<ResponseBody, String>() {
          @Override public String call(ResponseBody responseBody) {

            try {
              return responseBody.string();
            } catch (IOException e) {
              e.printStackTrace();
              return null;
            }
          }
        });
  }
}
