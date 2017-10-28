package com.example.lihao.mylogin.app;

import android.accounts.AccountManager;
import android.content.Context;
import android.support.v4.app.INotificationSideChannel;

import com.example.lihao.mylogin.bean.LoginInfo;
import com.example.lihao.mylogin.bean.UserInfo;
import com.example.lihao.mylogin.utils.SPUtils;
import com.example.lihao.mylogin.utils.UIUtils;

/**
 * Created by lihao on 17-10-22.
 */

public class UserManager {

    private Context context;

    public static LoginInfo loginInfo;

    private UserManager(Context context){
        this.context = context;
    }

    public static UserManager create(Context context) {

        UserManager mUserManger = new UserManager(context);
        return mUserManger;
    }

    public void setToken(String token) {
        SPUtils.setSharedStringData(UIUtils.getContext(),"token",token);
    }

    public String getToken() {
       return SPUtils.getSharedStringData(context,"token","");
    }

    public void setUser(UserInfo info) {

        SPUtils.setSharedIntData(context,"userId", info.getUid());
        SPUtils.setSharedStringData(context,"userName", info.getNickname());
        SPUtils.setSharedStringData(context,"address",info.getAddress());
        SPUtils.setSharedlongData(context,"create_time",info.getCreate_time());
        SPUtils.setSharedIntData(context,"job",info.getJob());
        SPUtils.setSharedIntData(context,"rank_level",info.getRank_level());
        SPUtils.setSharedStringData(context,"sex",info.getSex());
        SPUtils.setSharedIntData(context,"age",info.getU_age());
        SPUtils.setSharedStringData(context,"u_icon",info.getU_icon());
        SPUtils.setSharedIntData(context,"user_type",info.getUser_type());
        SPUtils.setSharedIntData(context,"visitor_sum",info.getVisitorSum());

    }

    public UserInfo getUser() {

        UserInfo info = new UserInfo();
        info.setUid(SPUtils.getSharedIntData(context,"userId",0));
        info.setNickname(SPUtils.getSharedStringData(context,"userName",""));
        info.setAddress(SPUtils.getSharedStringData(context,"address",""));
        info.setCreate_time(SPUtils.getSharedlongData(context,"create_time",System.currentTimeMillis()));
        info.setJob(SPUtils.getSharedIntData(context,"job",-1));
        info.setRank_level(SPUtils.getSharedIntData(context,"rank_level",1));
        info.setSex(SPUtils.getSharedStringData(context,"sex","男"));
        info.setU_age(SPUtils.getSharedIntData(context,"age",0));
        info.setU_icon(SPUtils.getSharedStringData(context,"u_icon",""));
        info.setUser_type(SPUtils.getSharedIntData(context,"user_type",5));
        info.setVisitorSum(SPUtils.getSharedIntData(context,"visitor_sum",0));

        return info;
    }

    public void clearUser() {

        SPUtils.setSharedIntData(context,"userId", 0);
        SPUtils.setSharedStringData(context,"userName", "");
        SPUtils.setSharedStringData(context,"address","");
        SPUtils.setSharedlongData(context,"create_time",0);
        SPUtils.setSharedIntData(context,"job",0);
        SPUtils.setSharedIntData(context,"rank_level",0);
        SPUtils.setSharedStringData(context,"sex","");
        SPUtils.setSharedIntData(context,"age",0);
        SPUtils.setSharedStringData(context,"u_icon","");
        SPUtils.setSharedIntData(context,"user_type",0);
        SPUtils.setSharedIntData(context,"visitor_sum",0);

    }

}
