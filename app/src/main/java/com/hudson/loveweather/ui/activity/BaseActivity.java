package com.hudson.loveweather.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hudson.loveweather.global.LoveWeatherApplication;

/**
 * Created by Hudson on 2017/11/26.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoveWeatherApplication.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoveWeatherApplication.finishActivity(this);
    }
}
