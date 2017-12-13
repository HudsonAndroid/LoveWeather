package com.hudson.loveweather.global;

/**
 * Created by Hudson on 2017/11/26.
 */

public class Constants {

    public static final int SERVER_WEATHER_UPDATE_OFFSET = 1*60;//服务器一个小时更新一次

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
    public static final String CUSTOM_CATEGORY = "自定义";
    public static final String PIC_CACHE_NAME = "loveWeather";//图片结果是loveWeatherx.jpg
    public static final int PIC_CACHE_COUNT = 10;//图片缓存个数


    public static final String DAILY_WORD_URL = "https://api.xiaolin.in/hitokoto";//每日一句api

    public static final String HE_WEATHER_BASE_URL = "http://guolin.tech/api/weather?cityid=";
    public static final String APP_KEY = "&key=e4927826553043d09e216e1d38c910a5";


    public static final String SHAREDPREFERENCE_NAME = "love_weather";

    public static final int EVENT_EXIT_APP = 0;

    public static final int PERMISSION_REQUEST_CODE = 1;
    public static final int DEFAULT_BACKGROUND_TRANSITION_DURATION = 600;
    public static final String[] HOT_CITIES = new String[]{
            "上海","北京","广州","深圳","南京","武汉","杭州","厦门","成都","长沙"
    };
//    public static final int WEATHER_NETWORK_UPDATE_SUCCESS = 1;//天气信息网络获取成功
//    public static final int WEATHER_NETWORK_UPDATE_FAILED = 2;//天气信息网络获取失败

    public static final String EVENT_START_LOCATE = "start_locate";
    public static final String EVENT_LOCATE_FAILED = "locate_failed";
    public static final String EVENT_WEATHER_ID_NOT_FOUND = "weather_id_not_found";
    public static final String BROADCAST_UPDATE_PIC = "com.hudson.loveweather.update_pic";
//    public static final String BROADCAST_UPDATE_WEATHER = "com.hudson.loveweather.update_weather";

}
