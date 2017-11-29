package com.hudson.loveweather.utils.update;

import com.hudson.loveweather.bean.Weather;

/**
 * Created by Hudson on 2017/11/29.
 */

public interface WeatherObserver{
    void onWeatherUpdateSuccess(Weather weather);
    void onWeatherUpdateFailed(Exception e);
}
