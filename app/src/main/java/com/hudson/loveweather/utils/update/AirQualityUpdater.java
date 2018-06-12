package com.hudson.loveweather.utils.update;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hudson.loveweather.bean.AirQualityBean;
import com.hudson.loveweather.db.Country;
import com.hudson.loveweather.db.DatabaseUtils;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.utils.HttpUtils;
import com.hudson.loveweather.utils.TimeUtils;
import com.hudson.loveweather.utils.WeatherChooseUtils;
import com.hudson.loveweather.utils.log.LogUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Hudson on 2018/4/1.
 * 空气质量更新器
 *  和风天气这个垃圾，把空气质量的收费了，只有城市才能获取到，城市下的区县无法获取
 */

public class AirQualityUpdater implements Updater{

    private ArrayList<AirQualityObserver> mObservers = new ArrayList<>();


    void registerObserver(AirQualityObserver observer){
        if(observer!=null&&!mObservers.contains(observer)){
            mObservers.add(observer);
        }
    }

    void unRegisterObserver(AirQualityObserver observer){
        if(mObservers.contains(observer)){
            mObservers.remove(observer);
        }
    }

    /**
     * 由于和风天气可以免费获取的接口中，只有城市才能，所以只能获取区县上一级的城市
     * @param weatherId
     * @return
     */
    private String getLastLevelCityWeatherId(String weatherId){
        Country country = DatabaseUtils.queryCountryByWeatherId(weatherId);
        if(country!=null){
            return DatabaseUtils.queryWeatherId(
                    country.getProvinceName(), country.getCityName(), country.getCityName());
        }
        return null;
    }

    @Override
    public void update(String url, Object... objects) {
        String weatherId = getLastLevelCityWeatherId((String) objects[0]);
        String airUrl = Constants.HE_WEATHER_AIR_URL + weatherId + Constants.APP_KEY;
        //获取缓存
        AirQualityBean airQualityCache = getAirQualityCache(weatherId);
        if(airQualityCache!=null){
            int lastUpdateTime = TimeUtils.parseMinuteTime(airQualityCache.getHeWeather6()
                    .get(0).getUpdate().getLoc(),0);
            if(lastUpdateTime!=-1&&(TimeUtils.parseCurrentMinuteTime() +24*60 - lastUpdateTime)%(24*60)
                    > Constants.SERVER_AIR_QUALITY_UPDATE_OFFSET){
                accessNetworkUpdateAirQuality(airUrl,airQualityCache);
            }else{//使用本地
                notifyAirUpdateSuccess(airQualityCache);
            }
        }else{
            accessNetworkUpdateAirQuality(airUrl,airQualityCache);
        }
    }


    private void accessNetworkUpdateAirQuality(String url, final AirQualityBean cache){
        HttpUtils.requestNetData(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                notifyAirQualityUpdateFailed(e,cache);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String string = response.body().string();
                    AirQualityBean airQualityBean = new Gson().fromJson(string, AirQualityBean.class);
                    notifyAirUpdateSuccess(airQualityBean);
                    //将当前选中的地区的天气质量信息保存到数据库
                    WeatherChooseUtils.getInstance().updateChooseCountryAirQualityCache(string);
                }catch (JsonSyntaxException e){
                    e.printStackTrace();
                    notifyAirQualityUpdateFailed(e,cache);
                }
            }
        });
    }


    /**
     * 获取空气质量缓存
     * @param weatherId
     * @return
     */
    public  AirQualityBean getAirQualityCache(String weatherId){
        try{
            LogUtils.e("获取天气数据缓存=");
            return new Gson().fromJson(
                    WeatherChooseUtils.getInstance().getAirQualityJsonByWeatherId(weatherId),
                    AirQualityBean.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public AirQualityBean getAirQualityInstance(String json){
        try{
            return new Gson().fromJson(json, AirQualityBean.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void notifyAirQualityUpdateFailed(Exception e,AirQualityBean cache) {
        for (int i = 0; i < mObservers.size(); i++) {
            mObservers.get(i).onAirQualityUpdateFailed(e,cache);
        }
    }

    private void notifyAirUpdateSuccess(AirQualityBean airQualityBean){
        for (int i = 0; i < mObservers.size(); i++) {
            mObservers.get(i).onAirQualityUpdateSuccess(airQualityBean);
        }
    }
}
