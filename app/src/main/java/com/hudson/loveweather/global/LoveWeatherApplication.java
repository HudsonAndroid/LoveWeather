package com.hudson.loveweather.global;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.hudson.loveweather.utils.DataBaseLoader;
import com.hudson.loveweather.utils.SharedPreferenceUtils;

import org.litepal.LitePal;

import java.util.Stack;

/**
 * Created by Hudson on 2017/11/24.
 */

public class LoveWeatherApplication extends Application {
    private static Context sContext;
    private static Handler sHandler;
    private static int sMainThreadId;
    private static LoveWeatherApplication sWeatherApplication = null;
    private static Stack<Activity> sActivityStack = new Stack<>();

    public static LoveWeatherApplication getMyApplication() {
        return sWeatherApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sWeatherApplication = this;
        sContext = getApplicationContext();
        LitePal.initialize(sContext);//初始化litepal
        sHandler = new Handler();
        sMainThreadId = android.os.Process.myTid();
    }

    public static Context getContext() {
        return sContext;
    }

    public static Handler getHandler() {
        return sHandler;
    }

    public static int getMainThreadId() {
        return sMainThreadId;
    }

    //activity统一管理
    public static void addActivity(Activity activity){
        sActivityStack.push(activity);
    }

    public static Activity getTopActivity(){
        //注意，不能回调pop，因为pop是包含了出栈操作的
        return sActivityStack.lastElement();
    }

    public static void finishTopActivity(){
        Activity activity = sActivityStack.pop();
        if(activity!=null){
            activity.finish();
        }
    }

    public static void finishActivity(Activity activity){
        if(activity!=null){
            sActivityStack.remove(activity);
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }

    /**
     * 结束此类activity
     * @param cls
     */
    public static void finishActivity(Class<?> cls){
        for (Activity activity : sActivityStack) {
            if(activity.getClass().equals(cls)){
                finishActivity(activity);
            }
        }
    }

    public static void finishAll(){
        for (Activity activity : sActivityStack) {
            if(activity!=null){
                activity.finish();
            }
        }
        sActivityStack.clear();
    }

    public static void exitApp(){
        checkDataLoadStatus();
        finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void checkDataLoadStatus(){
        if(!SharedPreferenceUtils.getInstance().isLocalDatabaseLoaded()){
            SharedPreferenceUtils.getInstance().saveLocalDatabaseFlag(DataBaseLoader.loadStatus);
        }
    }

}
