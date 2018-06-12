package com.hudson.loveweather.bean;

/**
 * Created by Hudson on 2018/5/27.
 */

public class WeatherDetailItem {
    private String mTitle;
    private String mContent;

    public WeatherDetailItem(String title, String content) {
        mTitle = title;
        mContent = content;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
