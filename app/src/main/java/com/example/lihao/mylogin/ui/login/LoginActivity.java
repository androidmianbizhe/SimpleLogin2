package com.example.lihao.mylogin.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.api.AsyncBaiduRunner;
import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialog;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
import com.baidu.api.Util;
import com.example.lihao.mylogin.R;
import com.example.lihao.mylogin.app.UserManager;
import com.example.lihao.mylogin.base.BaseActivity;
import com.example.lihao.mylogin.bean.BaiduLoginBean;
import com.example.lihao.mylogin.bean.LoginInfo;
import com.example.lihao.mylogin.bean.QQLoginBean;
import com.example.lihao.mylogin.http.Api;
import com.example.lihao.mylogin.http.CourseNetwork;
import com.example.lihao.mylogin.ui.MainActivity;
import com.example.lihao.mylogin.utils.MyTextUtils;
import com.example.lihao.mylogin.utils.SPUtils;
import com.example.lihao.mylogin.utils.UIUtils;
import com.google.gson.Gson;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.IOException;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.provider.UserDictionary.Words.APP_ID;

/**
 * Created by lihao on 17-10-22.
 */

public class LoginActivity extends BaseActivity {


    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.tv_signup)
    TextView tvSignup;
    @BindView(R.id.sv_root)
    ScrollView svRoot;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.iv_sina)
    ImageView ivSina;
    @BindView(R.id.iv_qq)
    ImageView ivQq;
    @BindView(R.id.iv_wechat)
    ImageView ivWechat;
    @BindView(R.id.iv_baidu)
    ImageView ivBaidu;

    //登陆方式
    //0 密码登陆 1 新浪登录 2 百度登录 3 qq登陆 4 微信登陆
    private int loginWay = 0;


    //新浪
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;

    //百度
    private Baidu baidu = null;

    private String clientId = "8mqfyFbXp3yg8yRPdc7H4ZGP";

    //是否每次授权都强制登陆
    private boolean isForceLogin = false;

    private boolean isConfirmLogin = true;

    private Handler mHandler = null;

    //qq
    Tencent mTencent = null;

    IUiListener listener = null;

    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void setPresenter() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @OnClick({R.id.btn_login, R.id.tv_signup, R.id.sv_root,R.id.iv_sina, R.id.iv_qq, R.id.iv_wechat, R.id.iv_baidu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_signup:
                break;
            case R.id.iv_sina:
                sinaLogin();
                break;
            case R.id.iv_qq:
                qqLogin();
                break;
            case R.id.iv_wechat:
                wechatLogin();
                break;
            case R.id.iv_baidu:
                baiduLogin();
                break;
        }
    }

    private void wechatLogin() {

        loginWay = 4;

    }

    private void qqLogin() {

        loginWay = 3;
        SPUtils.setSharedIntData(this,"loginWay",loginWay);
        //qq
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance("1106499896", this.getApplicationContext());

        listener = new IUiListener(){
            @Override
            public void onComplete(Object o) {
                //"回调结果"

                SPUtils.setSharedBooleanData(LoginActivity.this,"isLogin",true);
                SPUtils.setSharedlongData(LoginActivity.this,"deadline", System.currentTimeMillis()+7*24*60*60*1000);

                Gson gson = new Gson();
                QQLoginBean qqLoginBean = gson.fromJson(o.toString(), QQLoginBean.class);

                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("qq", (Serializable) qqLoginBean);
                intent.putExtra("loginWay",3);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }

            @Override
            public void onError(UiError e) {
                Util.logd("onError:", "code:" + e.errorCode + ", msg:"
                        + e.errorMessage + ", detail:" + e.errorDetail);

            }

            @Override
            public void onCancel() {
                Util.logd("cancel","");
            }
        };

        if (!mTencent.isSessionValid())
        {
            mTencent.login(this, "all", listener);
        }
    }

    private void sinaLogin() {

        loginWay = 1;
        SPUtils.setSharedIntData(this,"loginWay",loginWay);
        //sina
        mSsoHandler = new SsoHandler(this);

        mSsoHandler.authorize(new com.sina.weibo.sdk.auth.WbAuthListener(){
            @Override
            public void onSuccess(final Oauth2AccessToken token) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAccessToken = token;
                        if (mAccessToken.isSessionValid()) {
                            // 显示 Token
//                        updateTokenView(false);
                            // 保存 Token 到 SharedPreferences
                            AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);

                            SPUtils.setSharedBooleanData(LoginActivity.this,"isLogin",true);
                            SPUtils.setSharedlongData(LoginActivity.this,"deadline", System.currentTimeMillis()+7*24*60*60*1000);

                            Toast.makeText(LoginActivity.this,
                                    R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("sina",true);
                            intent.putExtra("loginWay",1);
                            LoginActivity.this.startActivity(intent);
                            LoginActivity.this.finish();
                        }
                    }
                });
            }

            @Override
            public void cancel() {
                Toast.makeText(LoginActivity.this,
                        R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(WbConnectErrorMessage errorMessage) {
                Toast.makeText(LoginActivity.this, errorMessage.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }

    private void baiduLogin() {

        loginWay = 2;
        SPUtils.setSharedIntData(this,"loginWay",loginWay);

        baidu = new Baidu(clientId, LoginActivity.this);

        baidu.authorize(LoginActivity.this, isForceLogin,isConfirmLogin,new BaiduDialog.BaiduDialogListener() {

            @Override
            public void onComplete(Bundle values) {

                new Thread(){
                    @Override
                    public void run() {
                        String json = "你好";
                        try {
                            json = baidu.request("https://openapi.baidu.com/rest/2.0/passport/users/getInfo", null, "GET");

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (BaiduException e) {
                            e.printStackTrace();
                        }

                        final String finalJson = json;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Util.logd("线程", Thread.currentThread()+"");
                                if (finalJson != null) {

                                    SPUtils.setSharedBooleanData(LoginActivity.this,"isLogin",true);
                                    SPUtils.setSharedlongData(LoginActivity.this,"deadline", System.currentTimeMillis()+7*24*60*60*1000);

                                    Gson gson = new Gson();
                                    BaiduLoginBean baiduLoginBean = gson.fromJson(finalJson, BaiduLoginBean.class);

                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    intent.putExtra("baidu", (Serializable) baiduLoginBean);
                                    intent.putExtra("loginWay",2);
                                    LoginActivity.this.startActivity(intent);
                                    LoginActivity.this.finish();
                                }
                            }
                        });

                    }
                }.start();


            }

            @Override
            public void onBaiduException(BaiduException e) {

            }

            @Override
            public void onError(BaiduDialogError e) {

            }

            @Override
            public void onCancel() {

            }
        });

    }

    private void login() {

        loginWay = 0;
        SPUtils.setSharedIntData(this,"loginWay",loginWay);

        long userId;
        String username = inputEmail.getText().toString();
        if (username.equals("") || !MyTextUtils.isNumber(username)) {
            UIUtils.showToast("请输入正确的用户名!");
            return;
        } else {
            userId = Long.parseLong(username);
        }

        String passwd = inputPassword.getText().toString();
        if (passwd.equals("")) {
            UIUtils.showToast("请输入密码!");
            return;
        }

        Api api = CourseNetwork.getInstance().getApi(this);
        Observable<LoginInfo> observable = api.login(userId, passwd).compose(CourseNetwork.schedulersTransformer);
        observable.subscribe(new Observer<LoginInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNext(LoginInfo loginInfo) {
                if (loginInfo.getReturnCode() == 1) {

                    UserManager.loginInfo = loginInfo;

                    UserManager.create(LoginActivity.this).setToken(loginInfo.getToken());
                    UserManager.create(LoginActivity.this).setUser(loginInfo.getUser());

                    SPUtils.setSharedBooleanData(LoginActivity.this,"isLogin",true);
                    SPUtils.setSharedlongData(LoginActivity.this,"deadline", System.currentTimeMillis()+7*24*60*60*1000);

                    Intent intent = new Intent(new Intent(LoginActivity.this, MainActivity.class));
                    intent.putExtra("loginWay",0);
                    LoginActivity.this.startActivity(intent);
                    LoginActivity.this.finish();

                } else if (loginInfo.getReturnCode() == 2) {
                    UIUtils.showToast("用户名或密码错误!");
                    inputPassword.setText("");
                } else {
                    UIUtils.showToast("用户不存在!");
                    inputPassword.setText("");
                }
            }

            @Override
            public void onError(Throwable e) {
                UIUtils.showToast("网络异常!");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(loginWay == 1){

            // SSO 授权回调
            // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
            if (mSsoHandler != null) {
                mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            }
        } else if(loginWay == 2){
            //百度
        } else if(loginWay == 3){
            //qq
            Tencent.onActivityResultData(requestCode,resultCode,data,listener);
        }

    }
}
