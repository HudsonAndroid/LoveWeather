package com.hudson.loveweather.utils.update;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hudson.loveweather.bean.Weather6;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.utils.HttpUtils;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.TimeUtils;
import com.hudson.loveweather.utils.WeatherChooseUtils;

import org.greenrobot.eventbus.EventBus;

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
    //定位城市天气改变监听
    private ArrayList<WeatherObserver> mLocationObservers = new ArrayList<>();


    void registerObserver(WeatherObserver observer){
        if(observer!=null&&!mObservers.contains(observer)){
            mObservers.add(observer);
        }
    }

    void registerLocateObserver(WeatherObserver observer){
        if(observer!=null&&!mLocationObservers.contains(observer)){
            mLocationObservers.add(observer);
        }
    }

    void unRegisterObserver(WeatherObserver observer){
        if(mObservers.contains(observer)){
            mObservers.remove(observer);
        }
    }

    void unRegisterLocateObserver(WeatherObserver observer){
        if(mLocationObservers.contains(observer)){
            mLocationObservers.remove(observer);
        }
    }


    @Override
    public void update(String url,Object... objects) {
        String weatherId = (String) objects[0];
        Weather6 weatherCache = getWeatherCache(weatherId);
        if(weatherCache!=null){
            int lastUpdateTime = TimeUtils.parseMinuteTime(weatherCache.getHeWeather6()
                    .get(0).getUpdate().getLoc(),0);
            if(lastUpdateTime!=-1&&(TimeUtils.parseCurrentMinuteTime() +24*60 - lastUpdateTime)%(24*60)
                    > Constants.SERVER_WEATHER_UPDATE_OFFSET){
                updateWeather(url,weatherId);
            }else{//使用本地
                notifyUpdateSuccess(weatherCache);
            }
        }else{
            updateWeather(url,weatherId);
        }
    }

    /**
     * 获取缓存数据
     * @param weatherId
     * @return
     */
    public Weather6 getWeatherCache(String weatherId){
        try{
            return new Gson().fromJson(
                    WeatherChooseUtils.getInstance().getWeatherJsonByWeatherId(weatherId),
                    Weather6.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Weather6 getWeatherInstance(String json){
        try{
            return new Gson().fromJson(json, Weather6.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 真正的天气更新在这里
     * @param url
     */
    void updateWeather(String url,final String weatherId){
        EventBus.getDefault().post("正在刷新...");
        HttpUtils.requestNetData(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                notifyUpdateFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String string = response.body().string();
                    Weather6 weather = new Gson().fromJson(string,
                            Weather6.class);
                    notifyUpdateSuccess(weather);
                    checkIfLocate(weatherId,weather);
                    //将当前选中的地区的天气信息保存到数据库
                    WeatherChooseUtils.getInstance().updateChooseCountryWeatherCache(string);
                }catch (JsonSyntaxException e){
                    e.printStackTrace();
                    notifyUpdateFailed(e);
                }
            }
        });
    }

    private void checkIfLocate(String weatherId,Weather6 weather){
        if(SharedPreferenceUtils.getInstance().getLastLocationWeatherId().equals(weatherId)){
            notifyLocateUpdate(weather);
        }
    }

    private void notifyUpdateFailed(Exception e) {
        e.printStackTrace();
        for (int i = 0; i < mObservers.size(); i++) {
            mObservers.get(i).onWeatherUpdateFailed(e);
        }
    }

    private void notifyUpdateSuccess(Weather6 weather){
        for (int i = 0; i < mObservers.size(); i++) {
            mObservers.get(i).onWeatherUpdateSuccess(weather);
        }
    }

    private void notifyLocateUpdate(Weather6 weather){
        for (int i = 0; i < mLocationObservers.size(); i++) {
            mLocationObservers.get(i).onWeatherUpdateSuccess(weather);
        }
    }

}
