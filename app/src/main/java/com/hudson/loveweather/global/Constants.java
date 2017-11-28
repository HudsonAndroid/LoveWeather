package com.hudson.loveweather.global;

/**
 * Created by Hudson on 2017/11/26.
 */

public class Constants {

    public static final String DB_BASE_URL = "http://guolin.tech/api/china";
    public static final String NET_PIC_URL = "http://placeimg.com/";
    public static final String[] PIC_CATEGORY = new String[]{//背景图分类
            "any",
            "animals",
            "nature",
            "architecture",
            "people",
            "tech"
    };
    public static final String PIC_CACHE_NAME = "loveWeather";//图片结果是loveWeatherx.jpg
    public static final int PIC_CACHE_COUNT = 10;//图片缓存个数


    public static final String DAILY_WORD_URL = "https://api.xiaolin.in/hitokoto";//每日一句api


    public static final String SHAREDPREFERENCE_NAME = "love_weather";

    public static final int EVENT_EXIT_APP = 0;

    public static final int PERMISSION_REQUEST_CODE = 1;
    public static final int DEFAULT_BACKGROUND_TRANSITION_DURATION = 1000;



    public static final String BROADCAST_UPDATE_PIC = "com.hudson.loveweather.update_pic";

}
