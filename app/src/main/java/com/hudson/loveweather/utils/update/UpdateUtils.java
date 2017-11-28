package com.hudson.loveweather.utils.update;

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

    public void updateWeather(String url,Object... objects){
        mWeatherDataUpdater.update(url,objects);
    }

    public void updateBackgroundPic(String url,Object... objects){
        mBackgroundPicUpdater.update(url,objects);
    }
}
