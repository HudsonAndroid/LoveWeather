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
    public static final String PIC_CACHE_NAME = "loveWeather.jpg";


    public static final String SHAREDPREFERENCE_NAME = "love_weather";

    public static final int EVENT_EXIT_APP = 0;

    public static final int PERMISSION_REQUEST_CODE = 1;
    public static final int DEFAULT_BACKGROUND_TRANSITION_DURATION = 1000;



    public static final String BROADCAST_UPDATE_PIC = "com.hudson.loveweather.update_pic";

}
