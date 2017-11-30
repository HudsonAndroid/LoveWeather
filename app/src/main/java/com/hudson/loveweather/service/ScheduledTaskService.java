package com.hudson.loveweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hudson.loveweather.db.DatabaseUtils;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.utils.DeviceUtils;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.location.LocationUtils;
import com.hudson.loveweather.utils.log.LogUtils;
import com.hudson.loveweather.utils.update.UpdateUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Hudson on 2017/11/26.
 * 长期后台运行的定时任务，包括更新天气，更新背景
 */

public class ScheduledTaskService extends Service {
    public static final int DEFAULT_WEATHER_TRIGGER_TIME = 5*60*60*1000;//5个小时更新一次天气
    public static final int DEFAULT_BACKGROUND_PIC_TRIG_TIME = 5*60*1000;//5分钟更新背景
    private SharedPreferenceUtils mSharedPreferenceUtils;
    public static final int TYPE_UPDATE_WEATHER = 0;
    public static final int TYPE_UPDATE_PIC = 1;
    public static final int TYPE_ACQUIRE_WEATHER_ID = 2;
    private int mAcquireCount = 0;
    private static final int ACQUIRE_MAX_COUNT = 4;//最多请求三次
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
            String lastLocationWeatherId = mSharedPreferenceUtils.getLastLocationWeatherId();
            if(!TextUtils.isEmpty(lastLocationWeatherId)){
                updateWeather(lastLocationWeatherId);
                scheduleUpdateWeather();
            }
        }else if(type == TYPE_UPDATE_PIC){
            updateBackgroundPic();
            scheduleUpdatePic();
        }else if(type == TYPE_ACQUIRE_WEATHER_ID){
            acquireWeatherIdAndUpdateWeather(intent.getStringExtra("province"),
                    intent.getStringExtra("city"),intent.getStringExtra("country"));
        }else{
            startLocation();
            scheduleUpdateWeather();
            scheduleUpdatePic();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startLocation(){
        new Thread(){
            @Override
            public void run(){
                LogUtils.e("子线程开始搜索数据库");
                LocationUtils locationUtils = new LocationUtils();
                locationUtils.startLocation();
                locationUtils.setLocationInfoGetListener(new LocationUtils.LocationInfoGetListener() {
                    @Override
                    public void onLocationGet(boolean success, String addr, String province, String city, String district, String street) {
                        if(success){
                            acquireWeatherIdAndUpdateWeather(province, city, district);
                        }else{
                            LogUtils.e("百度定位失败");
                            EventBus.getDefault().post("定位失败，请检查您的设置！");
                        }
                    }
                });
            }
        }.start();
    }

    private void acquireWeatherIdAndUpdateWeather(String province, String city, String district) {
        mAcquireCount ++;
        LogUtils.e("尝试获取天气id"+mAcquireCount);
        province = province.replace("省","");
        city = city.replace("市","");
        String weatherId = DatabaseUtils.queryWeatherId(province,city,district);
        LogUtils.e("地址是"+weatherId);
        if(!TextUtils.isEmpty(weatherId)){
            mAcquireCount =0;
            mSharedPreferenceUtils.saveLastLocationWeatherId(weatherId);
            mSharedPreferenceUtils.saveLastLocationInfo(city+" "+district);
            updateWeather(weatherId);
        }else{//可能是本地数据库还没有创建好，尝试过1分钟后再次从数据库中查找，不过请求次数不超过最大值
            if(mAcquireCount>ACQUIRE_MAX_COUNT){
                EventBus.getDefault().post("您所在的区域好像无法获取天气信息！");
            }else{
                AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                long triggerAtTime = System.currentTimeMillis() + 1*60*1000;
                Intent updateWeatherIntent = new Intent(this,ScheduledTaskService.class);
                updateWeatherIntent.putExtra("province",province);
                updateWeatherIntent.putExtra("city",city);
                updateWeatherIntent.putExtra("country",district);
                updateWeatherIntent.putExtra("type",TYPE_ACQUIRE_WEATHER_ID);
                PendingIntent pendingIntent = PendingIntent.getService(this,0,updateWeatherIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                manager.set(AlarmManager.RTC_WAKEUP,triggerAtTime,pendingIntent);
            }
        }
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
        long triggerAtTime = System.currentTimeMillis()
                + mSharedPreferenceUtils.getUpdateBackgroundPicTriggerTime();
        Intent updatePicIntent = new Intent(this,ScheduledTaskService.class);
        updatePicIntent.putExtra("type",TYPE_UPDATE_PIC);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,updatePicIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP,triggerAtTime,pendingIntent);
    }

    private void updateWeather(String weatherId){
        mUpdateUtils.updateWeather(Constants.HE_WEATHER_BASE_URL
                + weatherId + Constants.APP_KEY,weatherId);
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
