package com.yaodu.drug;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.bobomee.android.paylib.PayBlock;
import com.bobomee.android.paylib.interfaces.AlipayResultListener;
import com.bobomee.android.paylib.interfaces.WxpayResultListener;
import com.bobomee.android.paylib.model.AliPayResult;
import com.bobomee.android.paylib.model.AliPayResultS;
import com.bobomee.android.paylib.util.AlipayUtil;
import com.bobomee.android.paylib.util.WxpayUtil;
import com.bobomee.android.sharelogin.login.LoginBlock;
import com.bobomee.android.sharelogin.login.interfaces.ILoginCallback;
import com.bobomee.android.sharelogin.login.manager.LoginManager;
import com.bobomee.android.sharelogin.login.manager.QQLogin;
import com.bobomee.android.sharelogin.login.manager.WeiXinLogin;
import com.bobomee.android.sharelogin.login.manager.WeiboLogin;
import com.bobomee.android.sharelogin.login.model.QQInfoModel;
import com.bobomee.android.sharelogin.login.model.QQTokenModel;
import com.bobomee.android.sharelogin.login.model.WeixinInfoModel;
import com.bobomee.android.sharelogin.login.model.WeixinTokenModel;
import com.bobomee.android.sharelogin.login.sinaapi.User;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.yaodu.drug.mock.alipay.AlipayBlock;
import com.yaodu.drug.mock.alipay.AlipayOrderInfo;
import com.yaodu.drug.mock.wechatpay.WechatOderInfo;

public class MainActivity extends AppCompatActivity {

    private LoginManager mWeixinLogin;
    private LoginManager mQqLogin;
    private LoginManager mWbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////////////////////pay//////////////////////////
        //todo init
        PayBlock.getInstance().initWechatPay("");
        AlipayBlock.getInstance().initAliPay("", "", "");

        /////////////////////////login///////////////////////
        LoginBlock.getInstance().initWechatLogin("", "");
        LoginBlock.getInstance().initQQLogin("");
        LoginBlock.getInstance().initWbLogin("", "");


    }

    ////////////////////////////////////////////////////////////
    public void alipay(View view) {
        if (TextUtils.isEmpty(AlipayBlock.getInstance().getAlipayPartner()) || TextUtils.isEmpty(
            AlipayBlock.getInstance().getAlipayRsa_private()) || TextUtils.isEmpty(
            AlipayBlock.getInstance().getAlipaySeller())) {
            new AlertDialog.Builder(this).setTitle("警告")
                .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        //
                    }
                })
                .show();
            return;
        } else {

            // TODO: inin orderindo
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
                        final String tradNo = trand.getResult();

                        runOnUiThread(new Runnable() {
                            @Override public void run() {
                                Toast.makeText(MainActivity.this,
                                    "payResult: tradNo --- > " + tradNo, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            //支付结果确认中
                            runOnUiThread(new Runnable() {
                                @Override public void run() {
                                    Toast.makeText(MainActivity.this, "payResult: 支付结果确认中",
                                        Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            //支付失败
                            runOnUiThread(new Runnable() {
                                @Override public void run() {
                                    Toast.makeText(MainActivity.this, "payResult: 支付失败",
                                        Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public void wxpay(View view) {
        if (TextUtils.isEmpty(PayBlock.getInstance().getWechatAppId())) {
            new AlertDialog.Builder(this).setTitle("警告")
                .setMessage("需要配置APP_ID")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        //
                    }
                })
                .show();
            return;
        } else {

            // TODO: inin orderindo
            String payInfo = "";
            PayReq req = WechatOderInfo.getWeixinPayReq(payInfo);

            if (null != req) {
                WxpayUtil.weixinPay(req, new WxpayResultListener() {
                    @Override public void payResult(PayResp payResp) {
                        String prepayId = payResp.prepayId;
                        Toast.makeText(MainActivity.this, "prepayid--->" + prepayId,
                            Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onError(int errCode) {
                        Toast.makeText(MainActivity.this, "onError()-->" + errCode,
                            Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onCancel() {
                        Toast.makeText(MainActivity.this, "onCancel()", Toast.LENGTH_SHORT).show();
                    }

                    @Override public void notSupport() {
                        Toast.makeText(MainActivity.this, "没有安装微信,或版本太低", Toast.LENGTH_SHORT)
                            .show();
                    }
                });
            }
        }
    }

    ///////////////////////////////////////////////

    public void wxlogin(View view) {

        mWeixinLogin = new LoginManager(this, WeiXinLogin.class, new ILoginCallback() {
            @Override public void tokeCallBack(Object o) {
                final WeixinTokenModel tokenModel = (WeixinTokenModel) o;

                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Toast.makeText(MainActivity.this, tokenModel.toString(), Toast.LENGTH_SHORT)
                            .show();
                    }
                });
            }

            @Override public void infoCallBack(Object o) {
                final WeixinInfoModel infoModel = (WeixinInfoModel) o;
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Toast.makeText(MainActivity.this, infoModel.toString(), Toast.LENGTH_SHORT)
                            .show();
                    }
                });
            }

            @Override public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Toast.makeText(MainActivity.this, "onError()--->" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override public void onCancel() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Toast.makeText(MainActivity.this, "onCancel()", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mWeixinLogin.doLogin();
    }

    public void qqlogin(View view) {
        mQqLogin = new LoginManager(this, QQLogin.class, new ILoginCallback() {
            @Override public void tokeCallBack(Object o) {
                QQTokenModel qqTokenModel = (QQTokenModel) o;
            }

            @Override public void infoCallBack(Object o) {
                QQInfoModel qqInfoModel = (QQInfoModel) o;
            }

            @Override public void onError(Exception e) {

            }

            @Override public void onCancel() {

            }
        });

        mQqLogin.doLogin();
    }

    public void wblogin(View view) {
        mWbLogin = new LoginManager(this, WeiboLogin.class, new ILoginCallback() {
            @Override public void tokeCallBack(Object o) {
                Oauth2AccessToken mAccessToken = (Oauth2AccessToken) o;
            }

            @Override public void infoCallBack(Object o) {
                User user = (User) o;
            }

            @Override public void onError(Exception e) {

            }

            @Override public void onCancel() {

            }
        });
        mWbLogin.doLogin();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mQqLogin.onActivityResult(requestCode, resultCode, data)) {

        }
        if (mWbLogin.onActivityResult(requestCode, resultCode, data)) {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    ///////////////////////////////////////////////
}
