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
import com.hudson.loveweather.bean.Weather6;
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

import static com.hudson.loveweather.service.ScheduledTaskService.TYPE_UPDATE_WEATHER;

/**
 * Created by Hudson on 2017/12/6.
 * 说明：在android后续版本中，使用静态注册广播的方式已经无法保证应用在关闭状态下接受到了，所以
 * 如果应用被关闭，那么该服务也会被关闭。为了确保本服务能够正常显示数据，我们只能在主页面跳出对话框提示
 * 用户把我们的应用加入白名单中，否则桌面控件很可能在屏幕锁屏应用被杀之后无法实时更新，导致显示的时间是
 * 错误的。
 */

public class WidgetUpdateService extends Service implements WeatherObserver {
    public static final int TYPE_UPDATE_TIME = 1;
    public static final String BROADCAST_SHOW_WIDGET_TIPS = "com.hudson.love_weather.widget_tips";
    private AppWidgetManager mAppWidgetManager;
    private ComponentName mDefault,mCircle;
    private RemoteViews mDefaultRemoteViews,mCircleRemoteViews;
    private SharedPreferenceUtils mSharedPreferenceUtils;
    private UpdateUtils mUpdateUtils;
//    private ScreenOnBroadcastReceiver mReceiver;

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
        //启动一下更新的服务，并且刷新天气数据（如果服务以前是关闭的状态的）
        Intent startUpdateService = new Intent(this,ScheduledTaskService.class);
        startUpdateService.putExtra("type",TYPE_UPDATE_WEATHER);
        startService(startUpdateService);
        detectWidgetDialog();
        super.onCreate();
    }

    /**
     * 检测是否显示过提示对话框（提示用户，必须将应用加入白名单，否则无法
     * 正常更新widget)
     * 如果需要，那么这里将会发出一个广播通知WeatherActivity显示对话框
     */
    private void detectWidgetDialog(){
        if(!mSharedPreferenceUtils.hasShownWidgetTipDialog()){
            sendBroadcast(new Intent(BROADCAST_SHOW_WIDGET_TIPS));
        }
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        if(intent!=null){
            if(intent.getIntExtra("type",-1) == TYPE_UPDATE_TIME){
                scheduleUpdateTime();
            }else{
                update();
            }
        }
        return START_STICKY;
    }

    private void update() {
        scheduleUpdateTime();
        updateWeather(mUpdateUtils.getWeatherCache(mSharedPreferenceUtils.getSelectedLocationWeatherId()));
    }

    @Override
    public void onDestroy() {
        mUpdateUtils.unRegisterWeatherObserver(this);
//        unregisterReceiver(mReceiver);
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
//        //广播
//        mReceiver = new ScreenOnBroadcastReceiver();
//        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        registerReceiver(mReceiver, filter);
    }

    private void updateWeather(Weather6 weather){
        if(weather!=null){
            String curTime = TimeUtils.getCurTime();
            mDefaultRemoteViews.setTextViewText(R.id.tv_time, curTime);
            mCircleRemoteViews.setTextViewText(R.id.tv_time, curTime);

            Weather6.HeWeather6Bean heWeather6Bean = weather.getHeWeather6().get(0);
            Weather6.HeWeather6Bean.NowBean now = heWeather6Bean.getNow();
            mDefaultRemoteViews.setTextViewText(R.id.tv_weather_desc, now.getCond_txt());
            mDefaultRemoteViews.setTextViewText(R.id.tv_date, TimeUtils.getDayWeekOfDate()+"  "+TimeUtils.getDayNumberStringOfDate()+" "+TimeUtils.getMonthOfYear() );
            mDefaultRemoteViews.setTextViewText(R.id.tv_location,mSharedPreferenceUtils.getLastLocationInfo());
            mDefaultRemoteViews.setTextViewText(R.id.tv_temp,now.getTmp()+"℃");
            mAppWidgetManager.updateAppWidget(mDefault, mDefaultRemoteViews);

            mCircleRemoteViews.setTextViewText(R.id.tv_weather_desc, now.getCond_txt());
            mCircleRemoteViews.setTextViewText(R.id.tv_location,mSharedPreferenceUtils.getLastLocationInfo());
            mCircleRemoteViews.setTextViewText(R.id.tv_temp,now.getTmp()+"℃");
//            mCircleRemoteViews.setTextViewText(R.id.tv_air, heWeather6Bean.getAqi().getCity().getQlty());
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


    //=====观察者，监听天气数据的更新======
    @Override
    public void onWeatherUpdateSuccess(final Weather6 weather) {
        LogUtils.e("收到了新的天气数据");
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

//    //======屏幕亮起广播========
//    private class ScreenOnBroadcastReceiver extends BroadcastReceiver{
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            LogUtils.e("屏幕亮了哦");
//            update();
//        }
//    }

}
