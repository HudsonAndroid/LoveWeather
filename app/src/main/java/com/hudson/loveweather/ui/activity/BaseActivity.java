package com.hudson.loveweather.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hudson.loveweather.global.LoveWeatherApplication;

/**
 * Created by Hudson on 2017/11/26.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewAndInit();
        LoveWeatherApplication.addActivity(this);
        initView();
    }

    //保证initView必须在setContentView之后执行
    public abstract void setContentViewAndInit();

    public abstract void initView();

    /**
     * this method is invoked by onDestroy
     */
    public abstract void recycle();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoveWeatherApplication.finishActivity(this);
        recycle();
    }
}
