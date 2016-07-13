package com.bobomee.android.paylib.interfaces;

import com.tencent.mm.sdk.modelpay.PayResp;

/**
 * 微信支付回调
 *
 * @author bobomee.
 *         wbwjx115@gmail.com
 */
public interface WxpayResultListener {

  void payResult(PayResp payResp);

  void onError(int errCode);

  void onCancel();

  void notSupport();
}
