package com.bobomee.android.sharelogin.login.manager;

import android.app.Activity;
import android.content.Intent;
import com.bobomee.android.sharelogin.login.interfaces.ILogin;
import com.bobomee.android.sharelogin.login.interfaces.ILoginCallback;

/**
 * Created on 2016/3/25.下午7:47.
 * @author bobomee.
 * wbwjx115@gmail.com
 */
public enum LoginManager {

    INSTANCE;

    private Class<? extends ILogin> loginClass;
    private ILoginCallback loginCallback;
    private ILogin iLogin;

    public LoginManager loginClass(Class<? extends ILogin> _loginClass) {
        loginClass = _loginClass;
        return this;
    }

    public LoginManager loginCallback(ILoginCallback _iLoginCallback) {
        loginCallback = _iLoginCallback;
        return this;
    }

    public void doLogin(Activity activity) {
        try {
            iLogin = loginClass.newInstance();
            iLogin.prepare(activity);
            iLogin.doLogin(activity, loginCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return null != iLogin && iLogin.onActivityResult(requestCode, resultCode, data);
    }

}
