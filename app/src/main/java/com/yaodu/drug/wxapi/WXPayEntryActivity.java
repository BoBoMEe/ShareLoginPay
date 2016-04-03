package com.yaodu.drug.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yaodu.drug.Constants;

/**
 * 微信支付回调
 *
 * @author BoBoMEe
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    LocalBroadcastManager mLocalBroadcastManager;

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp baseResp) {
//        String result = "";
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
//                result = "发送成功";
            {
                Intent intent = new Intent(Constants.payAction);
                intent.putExtra(Constants.prepayId, ((PayResp) baseResp).prepayId);
                mLocalBroadcastManager.sendBroadcast(intent);
            }
            break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                result = "发送取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                result = "发送被拒绝";
                break;
            default:
//                result = "发送返回";
                break;
        }
        finish();
    }

}