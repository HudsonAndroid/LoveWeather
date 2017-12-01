package com.hudson.loveweather.ui.activity;

import android.widget.ImageView;

import com.hudson.loveweather.R;

public class SettingsActivity extends BaseActivity {


    @Override
    public void setContentViewAndInit() {
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void initView() {
        final ImageView test = (ImageView) findViewById(R.id.iv_test);
        test.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void recycle() {

    }
}
