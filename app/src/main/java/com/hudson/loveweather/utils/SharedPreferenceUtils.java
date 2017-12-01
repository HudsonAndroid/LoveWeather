package com.hudson.loveweather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.service.ScheduledTaskService;

/**
 * Created by Hudson on 2017/11/26.
 */

public class SharedPreferenceUtils {
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;

    private SharedPreferenceUtils(){
        mSp = UIUtils.getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_NAME,
                Context.MODE_PRIVATE);
        mEditor = mSp.edit();
    }

    static class SharedPreferenceHelper{
        static final SharedPreferenceUtils sInstance = new SharedPreferenceUtils();
    }

    public static SharedPreferenceUtils getInstance(){
        return SharedPreferenceHelper.sInstance;
    }


    public void saveLocalDatabaseFlag(boolean isLoaded){
        mEditor.putBoolean("loaded", isLoaded);
        mEditor.commit();
    }

    public boolean isLocalDatabaseLoaded(){
        return mSp.getBoolean("loaded",false);
    }

    public void saveUpdateWeatherTriggerTime(int triggerTime){
        mEditor.putInt("weather_update_trigger_time",triggerTime);
        mEditor.commit();
    }

    public int getUpdateWeatherTriggerTime(){
        return mSp.getInt("weather_update_trigger_time",
                ScheduledTaskService.DEFAULT_WEATHER_TRIGGER_TIME);
    }

    public void saveUpdateBackgroundPicTriggerTime(int triggerTime){
        mEditor.putInt("pic_update_trigger_time",triggerTime);
        mEditor.commit();
    }

    public int getUpdateBackgroundPicTriggerTime(){
        return mSp.getInt("pic_update_trigger_time",
                ScheduledTaskService.DEFAULT_BACKGROUND_PIC_TRIG_TIME);
    }

    public void saveBackgroundPicCategory(String category){
        mEditor.putString("pic_category",category);
        mEditor.commit();
    }

    public String getBackgroundPicCategory(){
        return mSp.getString("pic_category",Constants.PIC_CATEGORY[0]);
    }

    public void saveShouldUpdatePic(boolean flag){
        mEditor.putBoolean("should_update_pic",flag);
        mEditor.commit();
    }

    public boolean getShouldUpdatePic(){
        return mSp.getBoolean("should_update_pic",true);
    }

    public void saveOnlyWifiAccessNetUpdatePic(boolean flag){
        mEditor.putBoolean("only_wifi_update_network_pic",flag);
        mEditor.commit();
    }

    public boolean getOnlyWifiAccessNetUpdatePic(){
        return mSp.getBoolean("only_wifi_update_network_pic",false);
    }

    public void saveLastLocationWeatherId(String weatherId){
        mEditor.putString("default_weather_id",weatherId);
        mEditor.commit();
    }

    public String getLastLocationWeatherId(){
        return mSp.getString("default_weather_id",null);
    }

    public void saveLastLocationInfo(String locationInfo){
        mEditor.putString("default_location_info",locationInfo);
        mEditor.commit();
    }

    public String getLastLocationInfo(){
        return mSp.getString("default_location_info",null);
    }


    /**
     *
     * @param weather 天气的json格式字符串
     */
    public void saveWeatherInfo(String key,String weather){
        mEditor.putString(key,weather);
        mEditor.commit();
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public String getWeatherInfo(String key){
        return mSp.getString(key,null);
    }

    /**
     * 移除缓存
     * @param key
     */
    public void removeWeatherInfo(String key){
        mEditor.remove(key);
        mEditor.commit();
    }

    /**
     * 每日一句语句
     * @param words
     */
    public void saveDailyWords(String words){
        mEditor.putString("daily_words_content",words);
        mEditor.commit();
    }

    /**
     * 获取每日一句
     * @return
     */
    public String getDailyWords(){
        return mSp.getString("daily_words_content","爱在左，同情在右，走在生命的两旁，随时播种，随时开花，将这一径长途，点缀得香花弥漫，使穿枝拂叶的行人，踏着荆棘，不觉得痛苦，有泪可落，却不是悲凉");
    }

    /**
     * 保存每日一句更新的日期
     * @param date 日期
     */
    public void saveDailyWordsUpdateDate(int date){
        mEditor.putInt("daily_words_date",date);
        mEditor.commit();
    }

    public int getDailyWordsUpdateDate(){
        return mSp.getInt("daily_words_date",-1);
    }


}
