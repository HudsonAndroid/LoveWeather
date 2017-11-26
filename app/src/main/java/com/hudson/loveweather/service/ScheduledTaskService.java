package com.hudson.loveweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.hudson.loveweather.utils.SharedPreferenceUtils;

/**
 * Created by Hudson on 2017/11/26.
 * 长期后台运行的定时任务，包括更新天气，更新背景
 */

public class ScheduledTaskService extends Service {
    public static final int DEFAULT_WEATHER_TRIGGER_TIME = 1*60*60*1000;//一个小时更新天气
    public static final int DEFAULT_BACKGROUND_PIC_TRIG_TIME = 10*60*1000;//10分钟更新背景
    private SharedPreferenceUtils mSharedPreferenceUtils;
    public static final int TYPE_UPDATE_WEATHER = 0;
    public static final int TYPE_UPDATE_PIC = 1;

    @Override
    public void onCreate() {
        mSharedPreferenceUtils = SharedPreferenceUtils.getInstance();
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
            updateBackgroundPic();
            scheduleUpdateWeather();
            scheduleUpdatePic();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void scheduleUpdateWeather(){
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() +
                mSharedPreferenceUtils.getUpdateWeatherTriggerTime();
        Intent updateWeatherIntent = new Intent(this,ScheduledTaskService.class);
        updateWeatherIntent.putExtra("type",TYPE_UPDATE_WEATHER);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,updateWeatherIntent,0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
    }

    private void scheduleUpdatePic(){
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() +
                mSharedPreferenceUtils.getUpdateBackgroundPicTriggerTime();
        Intent updateWeatherIntent = new Intent(this,ScheduledTaskService.class);
        updateWeatherIntent.putExtra("type",TYPE_UPDATE_PIC);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,updateWeatherIntent,0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
    }

    private void updateWeather(){

    }

    private void updateBackgroundPic(){

    }
}
