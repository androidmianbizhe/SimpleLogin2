package com.example.lihao.mylogin.bean;

/**
 * Created by lihao on 17-10-22.
 */

public class LoginInfo {

    /**
     * returnCode : 1
     * user : {}
     * token : eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ==.eyJ1aWQiOjEyMzQ1Niwibmlja25hbWUiOiIxIiwiZGVhZGxpbmUiOjIxMTM0ODMxODR9
     */

    private int returnCode;
    private UserInfo user;
    private String token;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
