# ShareLoginPay
-----

A simple tools for share (WeChat,Weibo and QQ);
login(WeChat,Weibo and QQ)
pay(Alipay,WeChat)

## ThirdPay

### 微信支付

```java
// 初始化app_id
 PayBlock.getInstance().initWechatPay(wechatAppid);
```


```java
//从服务器获取orderinfo,发起支付
  String payInfo = "";
            PayReq req = WechatOderInfo.getWeixinPayReq(payInfo);
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
```

注意: 微信支付要注意包名,签名,因为需要签名验证,并新建wxapi/WXPayEntryActivity.java

### 支付宝支付

```java
            //从服务器获取orderinfo和sign,发起支付
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

注意: 支付宝支付的orderinfo和sign信息都在服务端生成.最终成功与否需要看服务器异步回调.

详细说明:[Android 支付宝和微信支付集成](http://blog.csdn.net/wbwjx/article/details/50383183)

## ThirdLogin and ThirdShare

### 1.初始化

```java
    LoginBlock.getInstance().initWechatLogin(wechatAppId, wechatAppSecret);
    LoginBlock.getInstance().initQQLogin(qqAppId);
    LoginBlock.getInstance().initWbLogin(wbAppKey, wbRedirectUrl);
```

### 2.以微信登录为例

```java
 mWeixinLogin = new LoginManager(this, WeiXinLogin.class, new ILoginCallback() {
      @Override public void tokeCallBack(Object o) {
        final WeixinTokenModel tokenModel = (WeixinTokenModel) o;
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
    });

    mWeixinLogin.doLogin();
```

注意:微信,微博,QQ登录需要复写onActivityResult()方法,微信需要新建`wxapi/WXEntryActivity.java`

详细说明[Android中QQ,微信,微博三方登陆及注意事项](http://blog.csdn.net/wbwjx/article/details/50365481)

### 以微信分享为例

```java
public void wxshare(View view) {
    IShare wxshare = new WechatShare(this);
    ShareContent shareContent = new ShareContent();
    shareContent.setContent("分享的内容");
    shareContent.setTitle("分享的标题");
    // 分享的类型,包括文本,图片,webpage,music等
    shareContent.setContentType(ContentType.TEXT);
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

微信分享同样需要签名,及新建回调类`wxapi/WXEntryActivity.java`

详细说明: [Android 三方分享与ShareSDK分享](http://blog.csdn.net/wbwjx/article/details/50278795)

## Thanks to

[lingochamp/ShareLoginLib](https://github.com/lingochamp/ShareLoginLib)