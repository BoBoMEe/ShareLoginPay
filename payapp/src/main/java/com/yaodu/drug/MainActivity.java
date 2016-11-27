package com.yaodu.drug;

import android.content.DialogInterface;
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
import com.bobomee.android.paylib.util.ThreadManager;
import com.bobomee.android.paylib.util.WxpayUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.yaodu.drug.mock.alipay.AlipayBlock;
import com.yaodu.drug.mock.alipay.AlipayOrderInfo;
import com.yaodu.drug.mock.wechatpay.WechatOderInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        /////////////////////////pay//////////////////////////
      //todo init appid
      PayBlock.getInstance().initWechatPay("wxb4ba3c02aa476ea1");
      WxpayUtil.init(this);
      //todo mock data,will be set in server,
      AlipayBlock.getInstance().initAliPay("", "", "");

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

          // todo Using mock data to generate payinfo
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

          // todo Using mock data to generate payinfo
          final String[] payInfo = { "" };

          ThreadManager.getShortPool().execute(new Runnable() {
            @Override public void run() {
              payInfo[0] = WechatOderInfo.getPayInfo();
              pay(payInfo[0]);
            }
          });
        }
    }

  public void pay(String payInfo) {
    PayReq req = WechatOderInfo.getWeixinPayReq(payInfo);

    WxpayUtil.weixinPay(req, new WxpayResultListener() {
      @Override public void payResult(PayResp payResp) {
        String prepayId = payResp.prepayId;
        Toast.makeText(MainActivity.this, "prepayid--->" + prepayId, Toast.LENGTH_SHORT).show();
      }

      @Override public void onError(int errCode) {
        Toast.makeText(MainActivity.this, "onError()-->" + errCode, Toast.LENGTH_SHORT).show();
      }

      @Override public void onCancel() {
        Toast.makeText(MainActivity.this, "onCancel()", Toast.LENGTH_SHORT).show();
      }

      @Override public void notSupport() {
        Toast.makeText(MainActivity.this, "没有安装微信,或版本太低", Toast.LENGTH_SHORT).show();
      }
    });
    }

    ///////////////////////////////////////////////

  /**
   * Free memory to prevent memory leaks.
   */
  @Override protected void onDestroy() {
    super.onDestroy();
    WxpayUtil.detach();
  }
}
