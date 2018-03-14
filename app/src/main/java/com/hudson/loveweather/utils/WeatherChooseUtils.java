package com.hudson.loveweather.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;

import com.hudson.loveweather.db.Country;
import com.hudson.loveweather.db.DatabaseUtils;
import com.hudson.loveweather.db.SelectedCountry;
import com.hudson.loveweather.ui.activity.WeatherActivity;
import com.hudson.loveweather.utils.log.LogUtils;
import com.hudson.loveweather.utils.update.UpdateUtils;

/**
 * Created by Hudson on 2017/12/2.
 */

public class WeatherChooseUtils {
    private SharedPreferenceUtils mSharedPreferenceUtils = SharedPreferenceUtils.getInstance();

    private static class ChooseHelper{
        static final WeatherChooseUtils sInstance = new WeatherChooseUtils();
    }

    public static WeatherChooseUtils getInstance(){
        return ChooseHelper.sInstance;
    }


    public void chooseCountry(Country country){
        mSharedPreferenceUtils.saveSelectedLocationWeatherId(country.getWeatherId());
        mSharedPreferenceUtils.saveLastSelectedLocationInfo(buildLocationInfo(country.getCityName()
                ,country.getCountryName()));
    }

    /**
     * 使用上次定位的地点作为选中地点
     */
    public void chooseDefaultCountry(){
        mSharedPreferenceUtils.saveSelectedLocationWeatherId(
                mSharedPreferenceUtils.getLastLocationWeatherId());
        mSharedPreferenceUtils.saveLastSelectedLocationInfo(
                mSharedPreferenceUtils.getLastLocationInfo());
    }

    /**
     * 设置当前选中的位置
     * @param weatherId
     * @param cityName
     * @param countryName
     */
    public void chooseCountry(String weatherId,String cityName,String countryName){
        mSharedPreferenceUtils.saveSelectedLocationWeatherId(weatherId);
        mSharedPreferenceUtils.saveLastSelectedLocationInfo(buildLocationInfo(cityName
                ,countryName));
    }

    /**
     * 把当前选中的地区的天气写入数据库
     * @param json
     */
    public void updateChooseCountryWeatherCache(String json){
        if(!TextUtils.isEmpty(json)){
            String[] results = parseLocationInfo(mSharedPreferenceUtils.getLastSelectedLocationInfo());
            if(results!=null){
                LogUtils.e("往数据库中写入缓存");
                SelectedCountry selectedCountry = new SelectedCountry();
                selectedCountry.setCityName(results[0]);
                selectedCountry.setCountryName(results[1]);
                String selectedLocationWeatherId = mSharedPreferenceUtils.getSelectedLocationWeatherId();
                selectedCountry.setWeatherId(selectedLocationWeatherId);
                selectedCountry.setWeatherJson(json);
                if(TextUtils.isEmpty(getWeatherJsonByWeatherId(selectedLocationWeatherId))){
                    selectedCountry.save();
                }else{//update如果数据库中不存在是不会自动创建的
                    selectedCountry.updateAll("weatherId = ?",selectedLocationWeatherId);//刷新
                }
            }
        }
    }

    /**
     * 切换城市
     * @param activity
     * @param country 目标地点
     */
    public void toggleSelectedCountry(Activity activity, Country country){
        if(activity == null||country == null){
            throw new AndroidRuntimeException("activity or country should not be null!");
        }
        chooseCountry(country);//首先选择区县
        String weatherId = country.getWeatherId();
        UpdateUtils.getInstance().updateWeather(UpdateUtils.generateWeatherUrl(weatherId),
                weatherId);//其次更新天气
        LogUtils.e("跳转页面");
        activity.startActivity(new Intent(activity, WeatherActivity.class));//跳转天气页面
        activity.finish();
    }

    public String getWeatherJsonByWeatherId(String weatherId){
        LogUtils.e("从数据库中获取缓存");
        return DatabaseUtils.queryWeatherJson(weatherId);
    }


    public static String buildLocationInfo(String cityName,String countryName){
        return cityName + " " + countryName;
    }

    private static String[] parseLocationInfo(String locationInfo){
        if(TextUtils.isEmpty(locationInfo)){
            return null;
        }
        if(!locationInfo.contains(" ")){
            return null;
        }
        String[] results = locationInfo.split(" ");
        if(results.length != 2){
            return null;
        }
        return results;
    }

    /**
     * 由于可能出现像 福建-福州-福州 的情况，
     * 所以对字符串进行修剪
     */
    public static String clipLocationInfo(String locationInfo){
        String[] locations = parseLocationInfo(locationInfo);
        if(locations!=null){
            if(locations[0].equals(locations[1])){
                return locations[0];
            }
        }
        return locationInfo;
    }

    /**
     * 由于可能出现像 福建-福州-福州 的情况，
     * 所以对字符串进行修剪
     */
    public static String clipLocationInfo(String cityName,String countryName){
        if(cityName.equals(countryName)){
            return cityName;
        }
        return buildLocationInfo(cityName,countryName);
    }



}
