package com.hudson.loveweather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hudson.loveweather.global.Constants;

/**
 * Created by Hudson on 2017/11/26.
 */

public class SharedPreferenceUtils {
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;

    private SharedPreferenceUtils(){
        mSp = UIUtils.getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_NAME,
                Context.MODE_PRIVATE);
        mEditor = mSp.edit();
    }

    static class SharedPreferenceHelper{
        static final SharedPreferenceUtils sInstance = new SharedPreferenceUtils();
    }

    public static SharedPreferenceUtils getInstance(){
        return SharedPreferenceHelper.sInstance;
    }


    public void saveLocalDatabaseFlag(boolean isLoaded){
        mEditor.putBoolean("loaded", isLoaded);
        mEditor.commit();
    }

    public boolean isLocalDatabaseLoaded(){
        return mSp.getBoolean("loaded",false);
    }

}
