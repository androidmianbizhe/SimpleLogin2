package com.example.lihao.mylogin.http;

import com.example.lihao.mylogin.bean.LoginInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ang on 2017/8/9.
 */

public interface Api {

    @POST("login/login")
    Observable<LoginInfo> login(@Query("username") long user,
                                @Query("password") String passwd);

    @POST("login/register")
    Observable<LoginInfo> register(@Query("nickname")String nickname,
                                   @Query("openID")String openId,
                                   @Query("loginWay")int loginWay,
                                   @Query("phone")String phone);

}
