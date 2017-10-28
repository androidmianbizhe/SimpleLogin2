package com.example.lihao.mylogin.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;

import com.example.lihao.mylogin.app.MyApplication;

/**
 * Created by lihao on 17-10-22.
 */

public class UIUtils {

    public static Context getContext(){
        return MyApplication.getContext();
    }

    public static Handler getHandler(){
        return MyApplication.getHandler();
    }

    public static int getMainTHreadId(){
        return MyApplication.getThreadId();
    }

    public static boolean isRunOnUIThread(){

        int threadId = Process.myTid();
        if(threadId == getMainTHreadId()){
            return true;
        }
        return false;
    }

    public static void runUIThread(Runnable r){
        if(isRunOnUIThread()){
            r.run();
        }else {
            getHandler().post(r);
        }
    }

    public static void showToast(String text){
        ToastUtils.showToast(getContext(),text);
    }

    ////////////////加载资源文件///////////////
    public static String getString(int id){

        return getContext().getResources().getString(id);
    }

    public static String[] getStringArray(int id){

        return getContext().getResources().getStringArray(id);
    }

    public static Drawable getDrawable(int id){

        return getContext().getResources().getDrawable(id);
    }

    public static int getColor(int id){

        return getContext().getResources().getColor(id);
    }

}
