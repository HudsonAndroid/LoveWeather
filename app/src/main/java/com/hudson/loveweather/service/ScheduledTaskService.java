package com.hudson.loveweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.utils.DeviceUtils;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.log.LogUtils;
import com.hudson.loveweather.utils.update.UpdateUtils;

/**
 * Created by Hudson on 2017/11/26.
 * 长期后台运行的定时任务，包括更新天气，更新背景
 */

public class ScheduledTaskService extends Service {
    public static final int DEFAULT_WEATHER_TRIGGER_TIME = 1*60*60*1000;//一个小时更新天气
    public static final int DEFAULT_BACKGROUND_PIC_TRIG_TIME = 5*60*1000;//5分钟更新背景
    private SharedPreferenceUtils mSharedPreferenceUtils;
    public static final int TYPE_UPDATE_WEATHER = 0;
    public static final int TYPE_UPDATE_PIC = 1;
    private int mPicIndex = -1;//请求图片的index
    private String mPicCategory;
    private String mPicUrl;
    private UpdateUtils mUpdateUtils;

    @Override
    public void onCreate() {
        LogUtils.e("服务启动了");
        mSharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        mPicCategory = mSharedPreferenceUtils.getBackgroundPicCategory();
        int[] pixels = new int[2];
        DeviceUtils.getScreen(pixels);
        mPicUrl = new StringBuilder(Constants.NET_PIC_URL).append("/")
                .append(pixels[0]).append("/").append(pixels[1]).append("/")
                .append(mPicCategory).append("/").toString();
        mUpdateUtils = UpdateUtils.getInstance();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        int type = intent.getIntExtra("type",-1);
        if(type == TYPE_UPDATE_WEATHER){
            updateWeather();
            scheduleUpdateWeather();
        }else if(type == TYPE_UPDATE_PIC){
            updateBackgroundPic();
            scheduleUpdatePic();
        }else{
            updateWeather();
//            updateBackgroundPic();
            scheduleUpdateWeather();
            scheduleUpdatePic();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void scheduleUpdateWeather(){
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = System.currentTimeMillis() +
                mSharedPreferenceUtils.getUpdateWeatherTriggerTime();
        Intent updateWeatherIntent = new Intent(this,ScheduledTaskService.class);
        updateWeatherIntent.putExtra("type",TYPE_UPDATE_WEATHER);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,updateWeatherIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP,triggerAtTime,pendingIntent);
    }

    private void scheduleUpdatePic(){
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int timeOffset = mSharedPreferenceUtils.getUpdateBackgroundPicTriggerTime();
        if(!mSharedPreferenceUtils.getShouldUpdatePic()){
            //如果页面不可见，那么更新是无意义的，将时间间隔扩大为原来的2倍
            LogUtils.e("页面不可见，更新周期扩大");
            timeOffset *= 2;
        }
        long triggerAtTime = System.currentTimeMillis() + timeOffset;
        Intent updatePicIntent = new Intent(this,ScheduledTaskService.class);
        updatePicIntent.putExtra("type",TYPE_UPDATE_PIC);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,updatePicIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP,triggerAtTime,pendingIntent);
    }

    private void updateWeather(){
        mUpdateUtils.updateWeather("");
    }

    /**
     * 仅仅是把图片下载到缓存目录中，更新操作还得外部自行完成
     * 这里通过广播的方式通知界面更新
     */
    private void updateBackgroundPic(){
        mPicUrl += (++ mPicIndex);
        LogUtils.e("更新图片了"+mPicIndex);
        mUpdateUtils.updateBackgroundPic(mPicUrl,mPicIndex);
    }


}
