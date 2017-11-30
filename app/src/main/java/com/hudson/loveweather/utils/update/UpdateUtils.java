package com.hudson.loveweather.utils.update;

import com.hudson.loveweather.bean.Weather;

/**
 * Created by Hudson on 2017/11/27.
 * 不宜使用策略模式，因为策略模式需要外界new一个对象
 * 而这里我们需要对外界隐藏实现，外界使用本工具类即可
 */

public class UpdateUtils {
    private BackgroundPicUpdater mBackgroundPicUpdater;
    private WeatherDataUpdater mWeatherDataUpdater;

    private UpdateUtils(){
        mBackgroundPicUpdater = new BackgroundPicUpdater();
        mWeatherDataUpdater = new WeatherDataUpdater();
    }

    private static class UpdateUtilsHelper{
        static final UpdateUtils sInstance = new UpdateUtils();
    }

    public static UpdateUtils getInstance(){
        return UpdateUtilsHelper.sInstance;
    }

    /**
     * @param url
     * @param objects
     */
    public void updateWeather(String url,Object... objects){
        mWeatherDataUpdater.update(url,objects);
    }

    public Weather parseWeatherFromJson(String json){
        return mWeatherDataUpdater.parseWeatherFromJson(json);
    }

    public void registerWeatherObserver(WeatherObserver observer){
        mWeatherDataUpdater.registerObserver(observer);
    }

    public void unRegisterWeatherObserver(WeatherObserver observer){
        mWeatherDataUpdater.unRegisterObserver(observer);
    }

//    /**
//     * use in activity
//     * @param url
//     * @param callback
//     */
//    public void updateWeather(String url, Callback callback){
//        mWeatherDataUpdater.updateWeather(url,callback);
//    }

    public void updateBackgroundPic(String url,Object... objects){
        mBackgroundPicUpdater.update(url,objects);
    }
}
