package com.hudson.loveweather.utils.update;

/**
 * Created by Hudson on 2017/11/27.
 * 外界不能直接使用本类，需要通过UpdateUtils操作
 * 因而本类不是public,而是包内可以访问
 */

 class WeatherDataUpdater implements Updater {

    @Override
    public void update(String url,Object... objects) {

    }
}
