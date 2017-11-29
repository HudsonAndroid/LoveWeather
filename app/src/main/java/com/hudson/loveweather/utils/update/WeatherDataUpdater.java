package com.hudson.loveweather.utils.update;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hudson.loveweather.bean.Weather;
import com.hudson.loveweather.utils.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Hudson on 2017/11/27.
 * 外界不能直接使用本类，需要通过UpdateUtils操作
 * 因而本类不是public,而是包内可以访问
 */

 class WeatherDataUpdater implements Updater {
    private ArrayList<WeatherObserver> mObservers = new ArrayList<>();


    void registerObserver(WeatherObserver observer){
        if(observer!=null&&!mObservers.contains(observer)){
            mObservers.add(observer);
        }
    }

    void unRegisterObserver(WeatherObserver observer){
        if(mObservers.contains(observer)){
            mObservers.remove(observer);
        }
    }


    @Override
    public void update(String url,Object... objects) {
        //由于Weather数据需要从网上获取并实时反馈到天气页面，所以这里只是发一个广播告知天气页面该回调
        //updateWeather来更新天气了
//        UIUtils.getContext().sendBroadcast(new Intent(Constants.BROADCAST_UPDATE_WEATHER));
        updateWeather(url);
    }

    /**
     * 真正的天气更新在这里
     * @param url
     */
    void updateWeather(String url){
        HttpUtils.requestNetData(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                notifyUpdateFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    Weather weather = new Gson().fromJson(response.body().string(),
                            Weather.class);
                    notifyUpdateSuccess(weather);
                }catch (JsonSyntaxException e){
                    e.printStackTrace();
                    notifyUpdateFailed(e);
                }
            }
        });
    }

    private void notifyUpdateFailed(Exception e) {
        for (int i = 0; i < mObservers.size(); i++) {
            mObservers.get(i).onWeatherUpdateFailed(e);
        }
    }

    private void notifyUpdateSuccess(Weather weather){
        for (int i = 0; i < mObservers.size(); i++) {
            mObservers.get(i).onWeatherUpdateSuccess(weather);
        }
    }



}
