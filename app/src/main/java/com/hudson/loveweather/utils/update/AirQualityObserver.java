package com.hudson.loveweather.utils.update;

import com.hudson.loveweather.bean.AirQualityBean;

/**
 * Created by Hudson on 2018/4/1.
 */

public interface AirQualityObserver {
    void onAirQualityUpdateSuccess(AirQualityBean airQualityBean);
    void onAirQualityUpdateFailed(Exception e,AirQualityBean cache);
}
