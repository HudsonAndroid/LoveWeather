package com.hudson.loveweather.utils.LogUtils;

import android.text.TextUtils;

import com.hudson.loveweather.utils.LogUtils.parser.Parser;

import java.util.List;

/**
 * Created by Hudson on 2017/11/25.
 * log配置
 */

public class LogConfig {
    private boolean mEnable = true;
    private int mLogLevel = Constants.TYPE_VERBOSE;
    private String mTag = Constants.DEFAULT_TAG;
    private List<Parser> mParseList;
    {//初始化默认值
        configParserClass(Constants.DEFAULT_PARSE_CLASS);
    }

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

    public LogConfig configParserClass(Class<? extends Parser>... classes) {
        for (Class<? extends Parser> cla : classes) {
            try {
                mParseList.add(0, cla.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public List<Parser> getParseList() {
        return mParseList;
    }
}
