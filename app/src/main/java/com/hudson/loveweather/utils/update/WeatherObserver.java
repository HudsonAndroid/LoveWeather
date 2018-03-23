package com.hudson.loveweather.utils.update;

import com.hudson.loveweather.bean.Weather6;

/**
 * Created by Hudson on 2017/11/29.
 */

public interface WeatherObserver{
    void onWeatherUpdateSuccess(Weather6 weather);
    void onWeatherUpdateFailed(Exception e);
}
