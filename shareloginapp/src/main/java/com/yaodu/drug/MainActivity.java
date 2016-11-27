package com.yaodu.drug;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.bobomee.android.sharelogin.login.LoginShareBlock;
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
import com.bobomee.android.sharelogin.share.content.ShareContent;
import com.bobomee.android.sharelogin.share.impl.QqShare;
import com.bobomee.android.sharelogin.share.impl.WbShare;
import com.bobomee.android.sharelogin.share.impl.WechatShare;
import com.bobomee.android.sharelogin.share.interfaces.ContentType;
import com.bobomee.android.sharelogin.share.interfaces.IShare;
import com.bobomee.android.sharelogin.share.interfaces.IShareCallBack;
import com.bobomee.android.sharelogin.share.interfaces.ShareType;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class MainActivity extends AppCompatActivity implements IWeiboHandler.Response {

  private QqShare mQqshare;
  private WbShare mMWbshare;

  private LoginManager mLoginManager = LoginManager.INSTANCE;
  private static final String TAG = "MainActivity";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // TODO: init appid
    LoginShareBlock.getInstance().initWechat("", "");
    LoginShareBlock.getInstance().initQQ("");
    LoginShareBlock.getInstance().initWb("", "");
  }

  /////////////////////////////////////
  public void wxlogin(View view) {

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

  }

  public void qqlogin(View view) {

    mLoginManager.loginClass(QQLogin.class).loginCallback(new ILoginCallback() {
      @Override public void tokeCallBack(Object o) {
        QQTokenModel qqTokenModel = (QQTokenModel) o;
        Log.d(TAG, "tokeCallBack: == " + qqTokenModel);
      }

      @Override public void infoCallBack(Object o) {
        QQInfoModel qqInfoModel = (QQInfoModel) o;
        Toast.makeText(MainActivity.this, "infoCallBack-->" + qqInfoModel.toString(),
            Toast.LENGTH_SHORT).show();
      }

      @Override public void onError(Exception e) {

      }

      @Override public void onCancel() {

      }
    }).doLogin(this);

  }

  public void wblogin(View view) {

    mLoginManager.loginClass(WeiboLogin.class).loginCallback(new ILoginCallback() {
      @Override public void tokeCallBack(Object o) {
        Oauth2AccessToken mAccessToken = (Oauth2AccessToken) o;
        Log.d(TAG, "tokeCallBack:  ---> " + mAccessToken);
      }

      @Override public void infoCallBack(Object o) {
        User user = (User) o;
        Toast.makeText(MainActivity.this, "infoCallBack-->" + user.toString(), Toast.LENGTH_SHORT)
            .show();
      }

      @Override public void onError(Exception e) {

      }

      @Override public void onCancel() {

      }
    }).doLogin(this);

  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (null != mLoginManager && mLoginManager.onActivityResult(requestCode, resultCode, data)) {

    }

    if (null != mQqshare && mQqshare.onActivityResult(requestCode, resultCode, data)) {
    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  ///////////////////////////////////////////////

  public void wxshare(View view) {
    IShare wxshare = new WechatShare(this);
    ShareContent shareContent = new ShareContent();
    shareContent.setContent("分享的内容");
    shareContent.setTitle("分享的标题");
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

  public void wxfshare(View view) {
    IShare wxfshare = new WechatShare(this);

    ShareContent shareContent = new ShareContent();
    shareContent.setContent("分享的内容");
    shareContent.setTitle("分享的标题");
    shareContent.setUrl("http://www.pharmacodia.com");
    shareContent.setContentType(ContentType.WEBPAG);
    shareContent.setShareType(ShareType.WECHAT_FRIENDS);

    wxfshare.share(shareContent, new IShareCallBack() {
      @Override public void onComplete(Object o) {
        Toast.makeText(MainActivity.this, "onComplete -->" + o.toString(), Toast.LENGTH_SHORT)
            .show();
      }

      @Override public void onError(Exception e) {
        Toast.makeText(MainActivity.this, "onError--->" + e.toString(), Toast.LENGTH_SHORT).show();
      }

      @Override public void onCancel() {
        Toast.makeText(MainActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
      }
    });
  }

  /**
   * 注意QQ 分享,url为必选项
   */
  public void qqshare(View view) {
    mQqshare = new QqShare(this);

    ShareContent shareContent = new ShareContent();
    shareContent.setContent("分享的内容");
    shareContent.setTitle("分享的标题");
    shareContent.setUrl("http://www.pharmacodia.com");

    mQqshare.share(shareContent, new IShareCallBack() {
      @Override public void onComplete(Object o) {
        Toast.makeText(MainActivity.this, "onComplete -->" + o.toString(), Toast.LENGTH_SHORT)
            .show();
      }

      @Override public void onError(Exception e) {
        Toast.makeText(MainActivity.this, "onError--->" + e.toString(), Toast.LENGTH_SHORT).show();
      }

      @Override public void onCancel() {
        Toast.makeText(MainActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
      }
    });
  }

  ///////////////////////////////////////////////////////////

  public void wbshare(View view) {
    mMWbshare = new WbShare(this);

    ShareContent shareContent = new ShareContent();
    shareContent.setContent("分享的内容");
    shareContent.setTitle("分享的标题");
    shareContent.setContentType(ContentType.TEXT);

    mMWbshare.share(shareContent, new IShareCallBack() {
      @Override public void onComplete(Object o) {
        Toast.makeText(MainActivity.this, "onComplete --- >" + o.toString(), Toast.LENGTH_SHORT)
            .show();
      }

      @Override public void onError(Exception e) {
        Toast.makeText(MainActivity.this, "onError--->" + e.toString(), Toast.LENGTH_SHORT).show();
      }

      @Override public void onCancel() {

      }
    });
  }

  @Override public void onResponse(BaseResponse baseResponse) {
    if (null != mMWbshare) {
      mMWbshare.onResponse(baseResponse);
    }
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (null != mMWbshare) {
      mMWbshare.onNewIntent(intent, this);
    }
  }
}
