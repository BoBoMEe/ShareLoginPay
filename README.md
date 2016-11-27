# ShareLoginPay
-----

A simple tools for share (WeChat,Weibo, QQ)
login(WeChat,Weibo,QQ)
pay(Alipay,WeChatPay)

## ThirdPay

### Wechat Pay

```java
//todo init appid
      PayBlock.getInstance().initWechatPay(wechatAppId);
      WxpayUtil.init(this);
```

```java
//generate payinfo from server and start pay
     String payInfo = WechatOderInfo.getPayInfo();
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
```

NOTE: 微信支付必须使用签名包,新建回调类[wxapi/WXPayEntryActivity.java](https://github.com/BoBoMEe/ShareLoginPay/blob/master/paylib/src/main/java/com/bobomee/android/paylib/wxapi/WXPayHandlerActivity.java)

### AliPay

```java
//todo mock data,will be set in server,
      AlipayBlock.getInstance().initAliPay(alipayPartner, alipaySeller, alipayRsa_private);
```


```java
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
```

NOTE: 支付宝支付的订单信息和签名信息都在服务端生成.最终成功与否需要看服务器异步回调.

详细说明: [Android 支付宝和微信支付集成](http://blog.csdn.net/wbwjx/article/details/50383183)

## ThirdLogin

### 1.Init

```java
// TODO: init appid
    LoginShareBlock.getInstance().initWechat(wechatAppId, wechatAppSecret);
    LoginShareBlock.getInstance().initQQ(qqAppId);
    LoginShareBlock.getInstance().initWb(wbAppKey, wbRedirectUrl);
```

### 2.Wechat Login

```java
 private LoginManager mLoginManager = LoginManager.INSTANCE;

 mLoginManager.loginClass(WeiXinLogin.class).loginCallback(new ILoginCallback() {
      @Override public void tokeCallBack(Object o) {
        final WeixinTokenModel tokenModel = (WeixinTokenModel) o;
        runOnUiThread(new Runnable() {
          @Override public void run() {
            Toast.makeText(MainActivity.this, tokenModel.toString(), Toast.LENGTH_SHORT).show();
          }
        });
      }

      @Override public void infoCallBack(Object o) {
        final WeixinInfoModel infoModel = (WeixinInfoModel) o;
        runOnUiThread(new Runnable() {
          @Override public void run() {
            Toast.makeText(MainActivity.this, infoModel.toString(), Toast.LENGTH_SHORT).show();
          }
        });
      }

      @Override public void onError(final Exception e) {
        runOnUiThread(new Runnable() {
          @Override public void run() {
            Toast.makeText(MainActivity.this, "onError()--->" + e.getMessage(), Toast.LENGTH_SHORT)
                .show();
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
    }).doLogin(this);
```

NOTE:需要复写onActivityResult()方法才能监听回调,新建微信回调类[wxapi/WXEntryActivity.java](https://github.com/BoBoMEe/ShareLoginPay/blob/master/sharelogin/src/main/java/com/bobomee/android/sharelogin/wxapi/WXAuthHandlerActivity.java)

详细说明：[Android中QQ,微信,微博三方登陆及注意事项](http://blog.csdn.net/wbwjx/article/details/50365481)

## ThirdShare

### Wechat Share

```java
public void wxshare(View view) {
    IShare wxshare = new WechatShare(this);
    ShareContent shareContent = new ShareContent();
    shareContent.setContent("分享的内容");
    shareContent.setTitle("分享的标题");
    // 分享的类型,包括文本,图片,webpage,music等
    shareContent.setContentType(ContentType.TEXT);
    // 分享的类型，包括 微信好友和微信朋友圈
    shareContent.setShareType(ShareType.WECHAT);

    wxshare.share(shareContent, new IShareCallBack() {
      @Override public void onComplete(Object o) {
        Toast.makeText(MainActivity.this, "onComplete-->" + o.toString(), Toast.LENGTH_SHORT)
            .show();
      }

      @Override public void onError(Exception e) {
        Toast.makeText(MainActivity.this, "onError-->" + e.toString(), Toast.LENGTH_SHORT).show();
      }

      @Override public void onCancel() {
        Toast.makeText(MainActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
      }
    });
  }
```

微信分享同样需要签名,新建微信回调类[wxapi/WXEntryActivity.java](https://github.com/BoBoMEe/ShareLoginPay/blob/master/sharelogin/src/main/java/com/bobomee/android/sharelogin/wxapi/WXAuthHandlerActivity.java)

详细说明: [Android 三方分享与ShareSDK分享](http://blog.csdn.net/wbwjx/article/details/50278795)

## Thanks to

[lingochamp/ShareLoginLib](https://github.com/lingochamp/ShareLoginLib)