package com.yaodu.drug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yaodu.drug.model.AliPayResult;
import com.yaodu.drug.model.AliPayResultS;
import com.yaodu.drug.utils.AlipayUtil;
import com.yaodu.drug.utils.WxpayUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AlipayResultListener {

    private IWXAPI api;
    LocalBroadcastManager mLocalBroadcastManager;
    BroadcastReceiver mReceiver;

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
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mReceiver = new PayReceiver();
        IntentFilter intentFilter = new IntentFilter(Constants.payAction);
        mLocalBroadcastManager.registerReceiver(mReceiver, intentFilter);

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
                if (TextUtils.isEmpty(Constants.PARTNER) || TextUtils.isEmpty(Constants.RSA_PRIVATE)
                        || TextUtils.isEmpty(Constants.SELLER)) {
                    new AlertDialog.Builder(this)
                            .setTitle("警告")
                            .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialoginterface, int i) {
                                            //
                                            finish();
                                        }
                                    }).show();
                    return;
                } else {
                    AlipayUtil.aliPay(this, this);
                }

                break;
            case 1:
                if ("wxb4ba3c02aa476ea1".equals(Constants.APP_ID)) {
                    new AlertDialog.Builder(this)
                            .setTitle("警告")
                            .setMessage("需要配置APP_ID")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialoginterface, int i) {
                                            //
                                            finish();
                                        }
                                    }).show();
                    return;
                } else {
                    WxpayUtil.weixinPay(api);
                }
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
    class PayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String prepayId = intent.getStringExtra(Constants.prepayId);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }
}
