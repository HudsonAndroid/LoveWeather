package com.hudson.loveweather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hudson.loveweather.R;
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

    /**
     *
     * @return 小时数
     */
    public int getUpdateWeatherTriggerTime(){
        return mSp.getInt("weather_update_trigger_time",
                ScheduledTaskService.DEFAULT_WEATHER_TRIGGER_TIME);
    }

    /**
     *
     * @param triggerTime 小时
     */
    public void saveUpdateBackgroundPicTriggerTime(int triggerTime){
        mEditor.putInt("pic_update_trigger_time",triggerTime);
        mEditor.commit();
    }

    /**
     *
     * @return 分钟
     */
    public int getUpdateBackgroundPicTriggerTime(){
        return mSp.getInt("pic_update_trigger_time",
                ScheduledTaskService.DEFAULT_BACKGROUND_PIC_TRIG_TIME);
    }

    /**
     *
     * @param category 分钟
     */
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

    /**
     * 保存应用运行时选中的地点的weatherId
     * @param weatherId
     */
    public void saveSelectedLocationWeatherId(String weatherId){
        mEditor.putString("selected_weather_id",weatherId);
        mEditor.commit();
    }

    /**
     * 获取应用运行时选中的地点的weatherId
     * @return
     */
    public String getSelectedLocationWeatherId(){
        return mSp.getString("selected_weather_id",null);
    }

    /**
     * 保存设备当前的位置信息
     * @param locationInfo
     */
    public void saveLastLocationInfo(String locationInfo){
        mEditor.putString("default_location_info",locationInfo);
        mEditor.commit();
    }

    /**
     * 获取设备当前的位置信息
     * @return
     */
    public String getLastLocationInfo(){
        return mSp.getString("default_location_info",null);
    }

    public void saveLastSelectedLocationInfo(String locationInfo){
        mEditor.putString("selected_location_info",locationInfo);
        mEditor.commit();
    }

    public String getLastSelectedLocationInfo(){
        return mSp.getString("selected_location_info",null);
    }


//    /**
//     *
//     * @param weather 天气的json格式字符串
//     */
//    public void saveWeatherInfo(String key,String weather){
//        mEditor.putString(key,weather);
//        mEditor.commit();
//    }
//
//    /**
//     * 获取缓存
//     * @param key
//     * @return
//     */
//    public String getWeatherInfo(String key){
//        return mSp.getString(key,null);
//    }
//
//    /**
//     * 移除缓存
//     * @param key
//     */
//    public void removeWeatherInfo(String key){
//        mEditor.remove(key);
//        mEditor.commit();
//    }

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
        return mSp.getString("daily_words_content",UIUtils.getString(R.string.logo_words));
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


    public void saveHistory(String history){
        mEditor.putString("search_history",history);
        mEditor.commit();
    }

    public String getHistory(){
        return mSp.getString("search_history",null);
    }

    public void saveShowNotification(boolean show){
        mEditor.putBoolean("show_notification",show);
        mEditor.commit();
    }

    public boolean isShowNotification(){
        return mSp.getBoolean("show_notification",false);
    }

    public void saveDefaultWidgetExist(boolean exist){
        mEditor.putBoolean("default_widget_exist",exist);
        mEditor.commit();
    }

    public boolean isDefaultWidgetExist(){
        return mSp.getBoolean("default_widget_exist",false);
    }

    public void saveCircleWidgetExist(boolean exist){
        mEditor.putBoolean("circle_widget_exist",exist);
        mEditor.commit();
    }

    public boolean isCircleWidgetExist(){
        return mSp.getBoolean("circle_widget_exist",false);
    }

    /**
     * 用户是否在本应用上操作
     * @param active
     */
    public void saveUserIsActive(boolean active){
        mEditor.putBoolean("user_is_active",active);
        mEditor.commit();
    }

    public boolean isUserActive(){
        return mSp.getBoolean("user_is_active",false);
    }

    public void saveHasShownWidgetTipDialog(boolean hasShown){
        mEditor.putBoolean("has_shown_widget_tip",hasShown);
        mEditor.commit();
    }

    public boolean hasShownWidgetTipDialog(){
        return mSp.getBoolean("has_shown_widget_tip",false);
    }
}
