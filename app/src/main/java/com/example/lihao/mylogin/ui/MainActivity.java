package com.example.lihao.mylogin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lihao.mylogin.R;
import com.example.lihao.mylogin.app.AppManager;
import com.example.lihao.mylogin.app.UserManager;
import com.example.lihao.mylogin.base.BaseActivity;
import com.example.lihao.mylogin.bean.BaiduLoginBean;
import com.example.lihao.mylogin.bean.LoginInfo;
import com.example.lihao.mylogin.bean.QQLoginBean;
import com.example.lihao.mylogin.http.Api;
import com.example.lihao.mylogin.http.CourseNetwork;
import com.example.lihao.mylogin.ui.login.LoginActivity;
import com.example.lihao.mylogin.utils.SPUtils;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_userinfo)
    TextView tvUserinfo;
    @BindView(R.id.progress)
    ProgressBar progress;

    //登录方式
    private int loginWay;

    private boolean sina;
    private BaiduLoginBean baidu;
    private QQLoginBean qq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

    }

    private void initView() {
        if(loginWay == 0 || loginWay == -1){
            progress.setVisibility(View.GONE);
            tvUserinfo.setText(UserManager.create(this).getUser().getNickname());

        }
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setPresenter() {

    }

    @Override
    protected void doBeforeSetContentView() {
        super.doBeforeSetContentView();

        loginWay = getIntent().getIntExtra("loginWay", -1);

        switch (loginWay) {
            case 0:
                break;
            case 1:
                sina = getIntent().getBooleanExtra("sina", false);
                break;
            case 2:
                baidu = (BaiduLoginBean) getIntent().getSerializableExtra("baidu");
                break;
            case 3:
                qq = (QQLoginBean) getIntent().getSerializableExtra("qq");
                break;
            case 4:
                break;
        }

        checkLogin();
    }


    private void checkLogin() {

        switch (loginWay) {
            case -1:
                long deadline = SPUtils.getSharedlongData(this, "deadline", 0);
                if (System.currentTimeMillis() > deadline) {
                    //过期
                    SPUtils.setSharedBooleanData(this, "isLogin", false);

                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                } else {
                    if (!SPUtils.getSharedBooleanData(this, "isLogin", false)) {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                }
                break;
            case 0:
                //密码登录
                break;
            case 1:
                //新浪
                Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this);
                String nickname = token.getUid();
                String opneId = token.getUid();
                String phone = token.getPhoneNum();
                register(nickname, opneId, loginWay, phone);
                break;
            case 2:
                //百度
                register(baidu.getUsername(), baidu.getUserid(), loginWay, "");
                break;
            case 3:
                //qq
                register(qq.getOpenid(), qq.getOpenid(), loginWay, "");
                break;
            case 4:
                //微信
//                register(qq.getOpenid(),qq.getOpenid(),loginWay,"");
                break;
        }
    }

    private void register(String nickname, String opneId, int loginWay, String phone) {

        Api api = CourseNetwork.getInstance().getApi(this);
        Observable<LoginInfo> register = api.register(nickname, opneId, loginWay, phone).compose(CourseNetwork.schedulersTransformer);
        register.subscribe(new Observer<LoginInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(LoginInfo loginInfo) {
                UserManager.loginInfo = loginInfo;
                UserManager.create(MainActivity.this).setUser(loginInfo.getUser());
                UserManager.create(MainActivity.this).setToken(loginInfo.getToken());
                tvUserinfo.setText(UserManager.loginInfo.getUser().getNickname());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                progress.setVisibility(View.GONE);
            }
        });
    }


}
