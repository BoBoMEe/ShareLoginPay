package com.yaodu.drug;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yaodu.drug.model.AliPayResult;
import com.yaodu.drug.model.AliPayResultS;
import com.yaodu.drug.utils.AlipayUtil;
import com.yaodu.drug.utils.WxpayUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AlipayResultListener {

    Button alipay;
    Button wxpay;
    private IWXAPI api;
    private BaseResp resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);

        findViewById(R.id.alipay).setOnClickListener(this);
        findViewById(R.id.wxpay).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alipay:
                String orderInfo = AlipayUtil.getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
                AlipayUtil.aliPay(this, orderInfo, this);
                break;
            case R.id.wxpay:
                WxpayUtil.weixinPay(new PayReq(), api);
                break;
        }
    }

    // 支付宝支付回调
    @Override
    public void payResult(String result) {

        AliPayResult payResult = new AliPayResult(result);
        String resultStatus = payResult.getResultStatus();
        String resultResult = payResult.getResult();

        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        if (TextUtils.equals(resultStatus, "9000")) {
            //支付成功
            AliPayResultS s = new AliPayResultS(resultResult);
            String tradNo = s.getResult();

        } else {
            // 判断resultStatus 为非“9000”则代表可能支付失败
            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                //支付结果确认中

            } else {
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                //支付失败
            }
        }
    }


    // 微信支付数据返回处理
    @Override
    protected void onResume() {
        super.onResume();
        resp = Constants.baseResp;
        if (null != resp && resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            // code返回
            if (resp.errCode == 0) {
                // 支付成功
                if (resp instanceof com.tencent.mm.sdk.modelpay.PayResp) {
                    com.tencent.mm.sdk.modelpay.PayResp payResp = (PayResp) resp;
                    String prepayId = payResp.prepayId;

                }
            }
        }
        Constants.baseResp = null;
    }
}
