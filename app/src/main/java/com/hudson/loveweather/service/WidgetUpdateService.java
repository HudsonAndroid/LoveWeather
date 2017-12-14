package com.hudson.loveweather.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.Weather;
import com.hudson.loveweather.ui.activity.WeatherActivity;
import com.hudson.loveweather.ui.widget.WidgetCircleProvider;
import com.hudson.loveweather.ui.widget.WidgetDefaultProvider;
import com.hudson.loveweather.utils.AlarmClockUtils;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.TimeUtils;
import com.hudson.loveweather.utils.UIUtils;
import com.hudson.loveweather.utils.log.LogUtils;
import com.hudson.loveweather.utils.update.UpdateUtils;
import com.hudson.loveweather.utils.update.WeatherObserver;

import static com.hudson.loveweather.service.ScheduledTaskService.TYPE_UPDATE_PIC;

/**
 * Created by Hudson on 2017/12/6.
 */

public class WidgetUpdateService extends Service implements WeatherObserver {
    public static final int TYPE_UPDATE_TIME = 1;
    private AppWidgetManager mAppWidgetManager;
    private ComponentName mDefault,mCircle;
    private RemoteViews mDefaultRemoteViews,mCircleRemoteViews;
    private SharedPreferenceUtils mSharedPreferenceUtils;
    private UpdateUtils mUpdateUtils;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mAppWidgetManager = AppWidgetManager.getInstance(WidgetUpdateService.this);
        mSharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        init();
        mUpdateUtils = UpdateUtils.getInstance();
        mUpdateUtils.registerWeatherObserver(this);
        //启动一下更新的服务
        Intent startUpdateService = new Intent(this,ScheduledTaskService.class);
        startUpdateService.putExtra("type",TYPE_UPDATE_PIC);
        startService(startUpdateService);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        if(intent!=null){
            if(intent.getIntExtra("type",-1) == TYPE_UPDATE_TIME){
                scheduleUpdateTime();
            }else{
                scheduleUpdateTime();
                updateWeather(mUpdateUtils.getWeatherCache(mSharedPreferenceUtils.getSelectedLocationWeatherId()));
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mUpdateUtils.unRegisterWeatherObserver(this);
        super.onDestroy();
    }

    private void init(){
        // 设置更新的组件
        mDefault = new ComponentName(WidgetUpdateService.this, WidgetDefaultProvider.class);
        mDefaultRemoteViews = new RemoteViews(getPackageName(),R.layout.widget_default_layout);
        Intent intent = new Intent(UIUtils.getContext(), WeatherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mDefaultRemoteViews.setOnClickPendingIntent(R.id.ll_widget_container,pendingIntent);

        mCircle = new ComponentName(this, WidgetCircleProvider.class);
        mCircleRemoteViews = new RemoteViews(getPackageName(),R.layout.widget_circle);
        mCircleRemoteViews.setOnClickPendingIntent(R.id.ll_widget_container,pendingIntent);
    }

    private void updateWeather(Weather weather){
        if(weather!=null){
            String curTime = TimeUtils.getCurTime();
            mDefaultRemoteViews.setTextViewText(R.id.tv_time, curTime);
            mCircleRemoteViews.setTextViewText(R.id.tv_time, curTime);

            Weather.HeWeatherBean heWeatherBean = weather.getHeWeather().get(0);
            Weather.HeWeatherBean.NowBean now = heWeatherBean.getNow();
            mDefaultRemoteViews.setTextViewText(R.id.tv_weather_desc, now.getCond().getTxt());
            mDefaultRemoteViews.setTextViewText(R.id.tv_date, TimeUtils.getDayWeekOfDate()+"  "+TimeUtils.getDayNumberStringOfDate()+" "+TimeUtils.getMonthOfYear() );
            mDefaultRemoteViews.setTextViewText(R.id.tv_location,mSharedPreferenceUtils.getLastLocationInfo());
            mDefaultRemoteViews.setTextViewText(R.id.tv_temp,now.getTmp()+"℃");
            mAppWidgetManager.updateAppWidget(mDefault, mDefaultRemoteViews);

            mCircleRemoteViews.setTextViewText(R.id.tv_weather_desc, now.getCond().getTxt());
            mCircleRemoteViews.setTextViewText(R.id.tv_location,mSharedPreferenceUtils.getLastLocationInfo());
            mCircleRemoteViews.setTextViewText(R.id.tv_temp,now.getTmp()+"℃");
            mCircleRemoteViews.setTextViewText(R.id.tv_air,heWeatherBean.getAqi().getCity().getQlty());
            mAppWidgetManager.updateAppWidget(mCircle, mCircleRemoteViews);
        }
    }

    private void updateTime(){
        String curTime = TimeUtils.getCurTime();
        mDefaultRemoteViews.setTextViewText(R.id.tv_time, curTime);
        mCircleRemoteViews.setTextViewText(R.id.tv_time, curTime);
        mAppWidgetManager.updateAppWidget(mDefault, mDefaultRemoteViews);
        mAppWidgetManager.updateAppWidget(mCircle, mCircleRemoteViews);
    }

    private void scheduleUpdateTime(){
        updateTime();
        LogUtils.e("计划任务开始了"+(60 - TimeUtils.getSecondOfMinute()));
        AlarmClockUtils.scheduleTimeUpdateTask((60 - TimeUtils.getSecondOfMinute())*1000);
    }


    @Override
    public void onWeatherUpdateSuccess(final Weather weather) {
        UIUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                updateWeather(weather);
            }
        });
    }

    @Override
    public void onWeatherUpdateFailed(Exception e) {

    }
}
