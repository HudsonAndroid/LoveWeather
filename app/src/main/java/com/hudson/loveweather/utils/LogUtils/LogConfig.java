package com.hudson.loveweather.utils.LogUtils;

import android.text.TextUtils;

/**
 * Created by Hudson on 2017/11/25.
 * log配置
 */

public class LogConfig {
    private boolean mEnable = true;
    private int mLogLevel = Constants.TYPE_VERBOSE;
    private String mTag = Constants.DEFAULT_TAG;

    static class ConfigHelper{
        static LogConfig sLogConfig = new LogConfig();
    }

     static LogConfig getInstance(){
        return ConfigHelper.sLogConfig;
    }

    public LogConfig configEnable(boolean enable){
        mEnable = enable;
        return this;
    }

    public LogConfig configLogLevel(int logLevel){
        if(logLevel<Constants.TYPE_VERBOSE||logLevel>Constants.TYPE_ERROR){
            throw new RuntimeException("the type of log is invalid!");
        }
        mLogLevel = logLevel;
        return this;
    }

    public LogConfig configTag(String tag){
        if(!TextUtils.isEmpty(tag)){
            mTag = tag;
        }
        return this;
    }

     boolean isEnable() {
        return mEnable;
    }

     int getLogLevel() {
        return mLogLevel;
    }

     String getTag() {
        return mTag;
    }
}
