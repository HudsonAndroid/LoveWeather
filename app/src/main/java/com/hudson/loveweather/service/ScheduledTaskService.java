package com.hudson.loveweather.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.Weather6;
import com.hudson.loveweather.db.DatabaseUtils;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.ui.activity.WeatherActivity;
import com.hudson.loveweather.utils.DataBaseLoader;
import com.hudson.loveweather.utils.DeviceUtils;
import com.hudson.loveweather.utils.HttpUtils;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.UIUtils;
import com.hudson.loveweather.utils.WeatherChooseUtils;
import com.hudson.loveweather.utils.location.LocationUtils;
import com.hudson.loveweather.utils.log.LogUtils;
import com.hudson.loveweather.utils.update.UpdateUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.hudson.loveweather.utils.AlarmClockUtils.scheduleTask;

/**
 * Created by Hudson on 2017/11/26.
 * 长期后台运行的定时任务，包括更新天气，更新背景
 */

public class ScheduledTaskService extends Service {
    public static final int DEFAULT_WEATHER_TRIGGER_TIME = 5;//5个小时更新一次天气
    public static final int DEFAULT_BACKGROUND_PIC_TRIG_TIME = 5;//5分钟更新背景
    private static final int DATABASE_SYNCHRONIZED_TIME = 120*1000;//120s检查数据库更新状态
    private static final int ACQUIRE_WEATHER_DATA_TIME = 10*1000;//在weatherId获取失败的情况下，10s重新获取weatherId
    private SharedPreferenceUtils mSharedPreferenceUtils;
    public static final int TYPE_UPDATE_WEATHER = 0;
    public static final int TYPE_UPDATE_PIC = 1;
    public static final int TYPE_ACQUIRE_WEATHER_ID = 2;
    public static final int TYPE_CHECK_DATABASE_SYNCHRONIZED = 3;//数据库是否同步成功
    public static final int TYPE_SHOW_NOTIFICATION = 4;
    public static final int TYPE_CANCEL_NOTIFICATION = 5;
    public static final int TYPE_CHANGE_BACKGROUND_CATEGORY = 6;
    private int mAcquireCount = 0;
    private static final int ACQUIRE_MAX_COUNT = 10;//最多请求十次
    public static int mPicIndex = -1;//请求图片的index
    private String mPicUrl;
    private UpdateUtils mUpdateUtils;


    //通知栏
    public static final int NOTIFICATION_ID = 0x01;
    private boolean mIsNotificationShow = false;

    @Override
    public void onCreate() {
        LogUtils.e("服务启动了");
        mSharedPreferenceUtils = SharedPreferenceUtils.getInstance();
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
        LogUtils.e("intent为空吗"+(intent == null));
        if(intent!=null){
            int type = intent.getIntExtra("type",-1);
            if(type == TYPE_UPDATE_WEATHER){
                String lastSelectedWeatherId = mSharedPreferenceUtils.getSelectedLocationWeatherId();
                if(!TextUtils.isEmpty(lastSelectedWeatherId)){
                    updateWeather(lastSelectedWeatherId);
                    scheduleUpdateWeather();
                }
            }else if(type == TYPE_UPDATE_PIC){
                updateBackgroundPic();
                scheduleUpdatePic();
            }else if(type == TYPE_ACQUIRE_WEATHER_ID){
                acquireWeatherIdAndUpdateWeather(intent.getStringExtra("province"),
                        intent.getStringExtra("city"),intent.getStringExtra("country"));
            }else if(type == TYPE_CHECK_DATABASE_SYNCHRONIZED){
                checkDatabaseSynchronizedStatus();
            }else if(type == TYPE_SHOW_NOTIFICATION){
                initNotification();
            }else if(type == TYPE_CANCEL_NOTIFICATION){
                cancelNotification();
            }else if(type == TYPE_CHANGE_BACKGROUND_CATEGORY){
                changeBackgroundCategory();
            }else{
                initData();
            }
        }else{
            initData();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initData(){
        //更新每日一句
        updateDailyWords();
        //更新天气
        startLocation();
        //通知栏
        initNotification();
        initBackgroundPicUrl();
        scheduleUpdateWeather();
        scheduleUpdatePic();
        scheduleCheckDatabaseSynchronizedStatus();
        //如果桌面存在widget，那么需要把widgetUpdateService开启
        if(mSharedPreferenceUtils.isCircleWidgetExist()||mSharedPreferenceUtils.isDefaultWidgetExist()){
            startService(new Intent(this,WidgetUpdateService.class));
        }
    }

    private void startLocation(){
        new Thread(){
            @Override
            public void run(){
                LogUtils.e("开始定位");
                EventBus.getDefault().post(Constants.EVENT_START_LOCATE);
                LocationUtils locationUtils = new LocationUtils();
                locationUtils.startLocation();
                locationUtils.setLocationInfoGetListener(new LocationUtils.LocationInfoGetListener() {
                    @Override
                    public void onLocationGet(boolean success, String addr, String province, String city, String district, String street) {
                        if(success){
                            acquireWeatherIdAndUpdateWeather(province, city, district);
                        }else{
                            LogUtils.e("百度定位失败");
                            EventBus.getDefault().post(Constants.EVENT_LOCATE_FAILED);
                        }
                    }
                });
            }
        }.start();
    }

    private void acquireWeatherIdAndUpdateWeather(String province, String city, String district) {
        LogUtils.e("定位的信息是"+province+" "+city+district);
        mAcquireCount ++;
        LogUtils.e("尝试获取天气id"+mAcquireCount);
        province = province.replace("省","");
        city = city.replace("市","");
        String weatherId = DatabaseUtils.queryWeatherId(province,city,district);
        LogUtils.e("地址是"+weatherId);
        if(!TextUtils.isEmpty(weatherId)){
            mAcquireCount =0;
            mSharedPreferenceUtils.saveLastLocationWeatherId(weatherId);
            mSharedPreferenceUtils.saveLastLocationInfo(WeatherChooseUtils.buildLocationInfo(city,district));
            WeatherChooseUtils.getInstance().chooseCountry(weatherId,city,district);
            updateWeather(weatherId);
        }else{//可能是本地数据库还没有创建好，尝试过30s后再次从数据库中查找，不过请求次数不超过最大值
            if(mAcquireCount>ACQUIRE_MAX_COUNT){
                EventBus.getDefault().post(Constants.EVENT_WEATHER_ID_NOT_FOUND);
            }else{
                Intent acquireWeatherIntent = new Intent(this,ScheduledTaskService.class);
                acquireWeatherIntent.putExtra("province",province);
                acquireWeatherIntent.putExtra("city",city);
                acquireWeatherIntent.putExtra("country",district);
                acquireWeatherIntent.putExtra("type",TYPE_ACQUIRE_WEATHER_ID);
                scheduleTask(this,ACQUIRE_WEATHER_DATA_TIME,acquireWeatherIntent);
            }
        }
    }


    private void scheduleUpdateWeather(){
        scheduleTask(TYPE_UPDATE_WEATHER,mSharedPreferenceUtils.getUpdateWeatherTriggerTime()*60*60*1000);
    }

    private void scheduleUpdatePic(){
        scheduleTask(TYPE_UPDATE_PIC,mSharedPreferenceUtils.getUpdateBackgroundPicTriggerTime()*60*1000);
    }

    private void updateWeather(String weatherId){
        LogUtils.e("更新数据了天气===============");
        mUpdateUtils.updateWeather(UpdateUtils.generateWeatherForecastUrl(weatherId),weatherId);
    }

    /**
     * 仅仅是把图片下载到缓存目录中，更新操作还得外部自行完成
     * 这里通过广播的方式通知界面更新
     */
    private void updateBackgroundPic(){
        if(TextUtils.isEmpty(mPicUrl)){
            initBackgroundPicUrl();
        }
        mPicUrl += (++ mPicIndex);
        LogUtils.e("服务请求更新图片了"+mPicIndex);
        mUpdateUtils.updateBackgroundPic(mPicUrl,mPicIndex);
    }

    private void updateDailyWords(){
        HttpUtils.initializeDailyWords();
    }

    private void checkDatabaseSynchronizedStatus(){
        LogUtils.e("这边检查数据库同步状态====================================");
        if(!DataBaseLoader.checkDataLoadStatus()){
            LogUtils.e("数据库同步失败");
            //数据库同步失败，尝试再次同步
//            startService(new Intent(this, DataInitializeService.class));
            //过会再次检查数据库是否同步成功
            scheduleCheckDatabaseSynchronizedStatus();
        }
    }

    private void scheduleCheckDatabaseSynchronizedStatus(){
        if(!mSharedPreferenceUtils.isLocalDatabaseLoaded()){
            LogUtils.e("数据库并没有同步成功，所以需要过会检测结果");
            scheduleTask(TYPE_CHECK_DATABASE_SYNCHRONIZED,DATABASE_SYNCHRONIZED_TIME);
        }
    }



    //通知栏(一旦播放，我们只显示一个通知（在init中new通知），如果需要每首歌曲都显示，可以在update中new通知)
    private void initNotification(){
        if(mSharedPreferenceUtils.isShowNotification()){
            Notification notification = new Notification();
            notification.icon = R.drawable.icon_round;
            notification.tickerText = getString(R.string.app_name);
            // 指定通知栏转到的activity是HomeActivity
            Intent intent = new Intent(UIUtils.getContext(), WeatherActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            notification.contentIntent = PendingIntent.getActivity(UIUtils.getContext(), 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);// 第二次操作会把第一次的覆盖掉
            //解析自定义的通知栏布局
            RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification);
            notification.contentView = remoteViews;
            Weather6 weatherCache = UpdateUtils.getInstance().getWeatherCache(mSharedPreferenceUtils.getLastLocationWeatherId());
            if(weatherCache!=null){
                remoteViews.setTextViewText(R.id.tv_location,
                        WeatherChooseUtils.clipLocationInfo(
                                mSharedPreferenceUtils.getLastLocationInfo()));
                List<Weather6.HeWeather6Bean> heWeather = weatherCache.getHeWeather6();
                if(heWeather!=null){
                    Weather6.HeWeather6Bean heWeather6Bean = heWeather.get(0);
                    if(heWeather6Bean !=null){
                        Weather6.HeWeather6Bean.NowBean now = heWeather6Bean.getNow();
                        if(now!=null){
                            remoteViews.setTextViewText(R.id.tv_temp, now.getTmp()+"℃");
                            remoteViews.setTextViewText(R.id.tv_desc,now.getCond_txt());
                        }
                        remoteViews.setTextViewText(R.id.tv_update_time,heWeather6Bean.getUpdate().getLoc());
                    }
                }
            }
            //必须执行这个后才会更新到通知栏
            startForeground(NOTIFICATION_ID, notification);
            mIsNotificationShow = true;
        }
    }

    private void cancelNotification(){
        if(mIsNotificationShow){
            LogUtils.e("取消通知了");
            stopForeground(true);
            ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).cancel(NOTIFICATION_ID);
        }
    }


    private void initBackgroundPicUrl(){
        String picCategory = mSharedPreferenceUtils.getBackgroundPicCategory();
        if(!picCategory.equals(Constants.CUSTOM_CATEGORY)){
            int[] pixels = new int[2];
            DeviceUtils.getScreen(pixels);
            mPicUrl = new StringBuilder(Constants.NET_PIC_URL).append("/")
                    .append(pixels[0]).append("/").append(pixels[1]).append("/")
                    .append(picCategory).append("/").toString();
        }else{//使用的是自定义的图片，所以不用
            mPicUrl = "";
        }
    }

    private void changeBackgroundCategory(){
        initBackgroundPicUrl();
        updateBackgroundPic();//立刻生效
        scheduleUpdatePic();//覆盖，避免两者重合
    }
}
