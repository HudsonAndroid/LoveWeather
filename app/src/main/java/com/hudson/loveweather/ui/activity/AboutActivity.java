package com.hudson.loveweather.ui.activity;

import android.view.View;

import com.hudson.loveweather.R;

public class AboutActivity extends BaseSubActivity {


    @Override
    public View setContent() {
        View root = View.inflate(this, R.layout.activity_about,null);
        return root;
    }

    @Override
    public void init() {

    }

    @Override
    public String getActivityTitle() {
        return "关于";
    }

    @Override
    public void recycle() {

    }
}
