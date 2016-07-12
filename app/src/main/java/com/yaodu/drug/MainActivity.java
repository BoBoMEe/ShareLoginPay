package com.yaodu.drug;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.bobomee.android.paylib.PayBlock;
import com.bobomee.android.paylib.interfaces.AlipayResultListener;
import com.bobomee.android.paylib.interfaces.WxpayResultListener;
import com.bobomee.android.paylib.model.AliPayResult;
import com.bobomee.android.paylib.model.AliPayResultS;
import com.bobomee.android.paylib.util.AlipayUtil;
import com.bobomee.android.paylib.util.WxpayUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yaodu.drug.mock.alipay.AlipayBlock;
import com.yaodu.drug.mock.alipay.AlipayOrderInfo;
import com.yaodu.drug.mock.wechatpay.WechatOderInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private IWXAPI api;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //todo init
        PayBlock.getInstance().initWechatPay("wxd930ea5d5a258f4f");
        AlipayBlock.getInstance()
            .initAliPay("2088811502784754", "admin@pharmacodia.com",
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK2qSk0rqJl6x1iB+B3eL0BJMXAKTWHbfJXHeDFG5TqW27Ip8uZeD057HColMGZChIYamKWe3A0j3Ywvap3oLcuTjPZJSKSNhc6qmchrTQAS9ZLX2USOgvs2YtC5VdJWYCwg2A9B+j2EDpNIaAyc+dCJ+YA+q0UlF5rrqIKlhWMXAgMBAAECgYEAgTzADIvX7Ve8b/ZQsdF98mZFvy47hf5zyXbm0C0FO8O2fPDQzXTkCTB+tjJ0wTTxMGcsf9wnwDOXlNJYkmiNYr7PGO2gg61iank+on0rw5pbyRESDc83cmenuIwyATvtudTPzynWX1CZMpnIYxx5TrRztyiQrXlYQFMJoLc60PECQQDhBUMUcdkzS+DeOI4DxK/HPdHyWIrh9g0nWYo9WdNlYiiZ5Vx99spcjh7tdaIrCV4kb1Wq8Kt6XJGJjArPGCQZAkEAxZMItuLvCSugRahpWPGxtjKH92QPnr0uT7wpMKG7QBcG0MGkHhV4CpJc9eHhxuyHuCw/ZKlrhczJC2MbAREmrwJABTnMDwN92dUfScnicm/07RmgcJJh11GRiKJptXcKk8YtzzUmar56gJ/EVBtxQrKa/xme0bN5KARSVPFtptWWsQJAbI5qRlqQLkC7Mirjn0xFca94B5UCBB08GfFvzFnA0ekVBJSxARqYt25OB4fSDwOdWrNtLXjtLDol6MzOZJqw3wJBAM0h7cpdJtAKbNrb9S9v3ag9QAPEXb7cv4BE9ARI7Svu/vkFskXCM82SkXd2MczNGAYZ1Jt35Hk4jtJI9hcIyiM=");

        api = WXAPIFactory.createWXAPI(this, PayBlock.getInstance().getWechatAppId());
        api.registerApp(PayBlock.getInstance().getWechatAppId());

        findViewById(R.id.alipay).setOnClickListener(this);
        findViewById(R.id.wxpay).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alipay:
                check(0);
                break;
            case R.id.wxpay:
                check(1);
                break;
        }
    }

    private void check(int i) {
        switch (i) {
            case 0:
                if (TextUtils.isEmpty(AlipayBlock.getInstance().getAlipayPartner())
                    || TextUtils.isEmpty(AlipayBlock.getInstance().getAlipayRsa_private())
                    || TextUtils.isEmpty(AlipayBlock.getInstance().getAlipaySeller())) {
                    new AlertDialog.Builder(this)
                            .setTitle("警告")
                            .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialoginterface, int i) {
                                            //
                                        }
                                    }).show();
                    return;
                } else {

                    String orderInfo = AlipayOrderInfo.getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");

                    String sign = AlipayOrderInfo.sign(orderInfo);

                    AlipayUtil.aliPay(this, orderInfo, sign, new AlipayResultListener() {
                        @Override public void payResult(String s) {

                            AliPayResult payResult = new AliPayResult(s);
                            String resultStatus = payResult.getResultStatus();
                            String resultResult = payResult.getResult();

                            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                            if (TextUtils.equals(resultStatus, "9000")) {
                                //支付成功
                                AliPayResultS trand = new AliPayResultS(resultResult);
                                String tradNo = trand.getResult();

                                Log.d(TAG, "payResult: tradNo --- > + tradNo");
                            } else {
                                // 判断resultStatus 为非“9000”则代表可能支付失败
                                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                if (TextUtils.equals(resultStatus, "8000")) {
                                    //支付结果确认中
                                    Log.d(TAG, "payResult: 支付结果确认中");
                                } else {
                                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                    //支付失败
                                    Log.d(TAG, "payResult: 支付失败");
                                }
                            }
                        }
                    });
                }

                break;
            case 1:
                if (TextUtils.isEmpty(PayBlock.getInstance().getWechatAppId())) {
                    new AlertDialog.Builder(this)
                            .setTitle("警告")
                            .setMessage("需要配置APP_ID")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialoginterface, int i) {
                                            //
                                        }
                                    }).show();
                    return;
                } else {
                    //判断用户是否安装微信
                    if (WxpayUtil.isWXAppInstalledAndSupported(api)) {
                        WechatOderInfo.getWeixinPayInfo()
                            .switchMap(new Func1<String, Observable<PayReq>>() {
                                @Override public Observable<PayReq> call(String s) {
                                    return Observable.just(WechatOderInfo.getWeixinPayReq(s));
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<PayReq>() {
                                @Override public void call(PayReq req) {
                                    WxpayUtil.weixinPay(api, req, new WxpayResultListener() {
                                        @Override public void payResult(PayResp payResp) {
                                            String prepayId = payResp.prepayId;
                                            Toast.makeText(MainActivity.this,
                                                "prepayid--->" + prepayId, Toast.LENGTH_SHORT)
                                                .show();
                                        }

                                        @Override public void onError(int errCode) {
                                            Toast.makeText(MainActivity.this,
                                                "onError()-->" + errCode, Toast.LENGTH_SHORT)
                                                .show();
                                        }

                                        @Override public void onCancel() {
                                            Toast.makeText(MainActivity.this, "onCancel()",
                                                Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }, new Action1<Throwable>() {
                                @Override public void call(Throwable throwable) {
                                    Toast.makeText(MainActivity.this,
                                        "onError--->" + throwable.toString(), Toast.LENGTH_SHORT)
                                        .show();
                                }
                            });
                    } else {
                        Toast.makeText(MainActivity.this, "没有安装微信", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

}
