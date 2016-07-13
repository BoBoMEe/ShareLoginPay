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
public class LoginManager {

    private final Class<? extends ILogin> loginClass;
    private final Activity activity;
    private final ILoginCallback loginCallback;
    private ILogin iLogin;

    public LoginManager(Activity activity, Class<? extends ILogin> loginClass, ILoginCallback loginCallback) {
        this.activity = activity;
        this.loginClass = loginClass;
        this.loginCallback = loginCallback;
    }

    public void doLogin() {

        try {

            iLogin = loginClass.newInstance();
            iLogin.doLogin(activity, loginCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return null != iLogin && iLogin.onActivityResult(requestCode, resultCode, data);
    }

}
