package com.hudson.loveweather.utils.update;

import com.hudson.loveweather.bean.AirQualityBean;
import com.hudson.loveweather.bean.Weather6;
import com.hudson.loveweather.global.Constants;

/**
 * Created by Hudson on 2017/11/27.
 * 不宜使用策略模式，因为策略模式需要外界new一个对象
 * 而这里我们需要对外界隐藏实现，外界使用本工具类即可
 */

public class UpdateUtils {
    private BackgroundPicUpdater mBackgroundPicUpdater;
    private WeatherDataUpdater mWeatherDataUpdater;
    private AirQualityUpdater mAirQualityUpdater;

    private UpdateUtils(){
        mBackgroundPicUpdater = new BackgroundPicUpdater();
        mWeatherDataUpdater = new WeatherDataUpdater();
        mAirQualityUpdater = new AirQualityUpdater();
    }

    private static class UpdateUtilsHelper{
        static final UpdateUtils sInstance = new UpdateUtils();
    }

    public static UpdateUtils getInstance(){
        return UpdateUtilsHelper.sInstance;
    }

    /**
     * 更新天气和天气质量数据
     * @param url
     * @param objects
     */
    public void updateWeather(String url,Object... objects){
        mWeatherDataUpdater.update(url,objects);//天气数据
        mAirQualityUpdater.update(null,objects);//天气质量数据
    }

    public void registerWeatherObserver(WeatherObserver observer,AirQualityObserver airQualityObserver){
        mWeatherDataUpdater.registerObserver(observer);
        mAirQualityUpdater.registerObserver(airQualityObserver);
    }

    public void unRegisterWeatherObserver(WeatherObserver observer,AirQualityObserver airQualityObserver){
        mWeatherDataUpdater.unRegisterObserver(observer);
        mAirQualityUpdater.unRegisterObserver(airQualityObserver);
    }

    public void registerLocateWeatherObserver(WeatherObserver observer){
        mWeatherDataUpdater.registerLocateObserver(observer);
    }

    public void unRegisterLocateWeatherObserver(WeatherObserver observer){
        mWeatherDataUpdater.unRegisterLocateObserver(observer);
    }

    public Weather6 getWeatherCache(String weatherId){
        return mWeatherDataUpdater.getWeatherCache(weatherId);
    }

    public AirQualityBean getAirQualityCache(String weatherId){
        return mAirQualityUpdater.getAirQualityCache(weatherId);
    }

    public Weather6 getWeatherInstance(String json){
        return mWeatherDataUpdater.getWeatherInstance(json);
    }

    public AirQualityBean getAirQualityInstance(String json){
        return mAirQualityUpdater.getAirQualityInstance(json);
    }

    public void updateBackgroundPic(String url,Object... objects){
        mBackgroundPicUpdater.update(url,objects);
    }

    public static String generateWeatherForecastUrl(String weatherId){
        return Constants.HE_WEATHER_FORECAST_URL + weatherId + Constants.APP_KEY;
    }
}
