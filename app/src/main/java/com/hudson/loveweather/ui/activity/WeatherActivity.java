package com.hudson.loveweather.ui.activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.Weather;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.global.LoveWeatherApplication;
import com.hudson.loveweather.service.DataInitializeService;
import com.hudson.loveweather.service.ScheduledTaskService;
import com.hudson.loveweather.ui.view.weatherpage.FirstPageViewHelper;
import com.hudson.loveweather.ui.view.weatherpage.SecondPageViewHelper;
import com.hudson.loveweather.utils.BitmapUtils;
import com.hudson.loveweather.utils.DataBaseLoader;
import com.hudson.loveweather.utils.HttpUtils;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.TimeUtils;
import com.hudson.loveweather.utils.ToastUtils;
import com.hudson.loveweather.utils.UIUtils;
import com.hudson.loveweather.utils.WeatherChooseUtils;
import com.hudson.loveweather.utils.log.LogUtils;
import com.hudson.loveweather.utils.update.UpdateUtils;
import com.hudson.loveweather.utils.update.WeatherObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class WeatherActivity extends BaseActivity implements View.OnClickListener, WeatherObserver, SwipeRefreshLayout.OnRefreshListener {
    private TextView mCity;
    private TextView mCalendar;
    private View mRoot;
    private WeatherBroadCastReceiver mReceiver;
    private LinearLayout mWeatherContainer;
    private View mActionBar;
    private FirstPageViewHelper mFirstViewHelper;
    private SecondPageViewHelper mSecondPageViewHelper;
    private boolean mIsDatabaseInit = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SharedPreferenceUtils mInstance;
    private UpdateUtils mUpdateUtils;


    @Override
    public void setContentViewAndInit() {
        setContentView(R.layout.activity_weather);
        mInstance = SharedPreferenceUtils.getInstance();
        requestPermission();
        mReceiver = new WeatherBroadCastReceiver();
        IntentFilter filter = new IntentFilter(Constants.BROADCAST_UPDATE_PIC);
        registerReceiver(mReceiver,filter);
        mUpdateUtils = UpdateUtils.getInstance();
        mUpdateUtils.registerWeatherObserver(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.srl);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRoot = findViewById(R.id.ll_weather);
        mActionBar = this.findViewById(R.id.rl_actionbar);
        mCity = (TextView) this.findViewById(R.id.tv_city);
        mCity.setOnClickListener(this);
        findViewById(R.id.iv_settings).setOnClickListener(this);
        findViewById(R.id.rl_calendar).setOnClickListener(this);
        mWeatherContainer = (LinearLayout) this.findViewById(R.id.ll_weather_container);
        mFirstViewHelper = new FirstPageViewHelper();
        mSecondPageViewHelper = new SecondPageViewHelper();
        mWeatherContainer.post(new Runnable() {
            @Override
            public void run() {
                //第三个参数是actionbar高度
                mFirstViewHelper.inflateView(WeatherActivity.this
                        ,mWeatherContainer,mActionBar.getHeight());
                mSecondPageViewHelper.inflateView(WeatherActivity.this,
                        mWeatherContainer,mActionBar.getHeight());
                //读取缓存的天气信息
                WeatherChooseUtils instance = WeatherChooseUtils.getInstance();
                instance.chooseDefaultCountry();
                updateWeather(mUpdateUtils.getWeatherCache(mInstance.getSelectedLocationWeatherId()));
            }
        });
        mCalendar = (TextView) this.findViewById(R.id.tv_calendar);
        mCalendar.setText(TimeUtils.getDayNumberStringOfDate());
        initializeBackgroundFromCache();
    }


    /**
     * 从缓存中读取图片
     */
    private void initializeBackgroundFromCache() {
        Bitmap showPic = BitmapUtils.getShowPic();
        if(showPic!=null){
            mRoot.setBackground(new BitmapDrawable(showPic));
        }
    }

    @Override
    public void recycle() {
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mReceiver);
        mUpdateUtils.unRegisterWeatherObserver(this);
    }

    private void initializeDatabase() {
        if(HttpUtils.isNetworkAvailable()){
            mIsDatabaseInit = mInstance.isLocalDatabaseLoaded();
            if(!mIsDatabaseInit){
                LogUtils.log("没有初始化过，所以开始启动服务加载");
                startService(new Intent(this, DataInitializeService.class));
            }else{
                LogUtils.log("已经初始化过了");
            }
        }else{
            ToastUtils.showToast("网络不可用！");
        }
    }


    @Override
    public void onRefresh() {
        if(HttpUtils.isNetworkAvailable()){
            if(!DataBaseLoader.loadStatus&&!mInstance.isLocalDatabaseLoaded()){
                initializeDatabase();
                startBackgroundService();
            }else{
                //更新天气（我们更新的是选中的地点的天气，即当前页面显示的城市的天气）
                String selectedLocationWeatherId = mInstance.getSelectedLocationWeatherId();
                mUpdateUtils.updateWeather(
                        UpdateUtils.generateWeatherUrl(selectedLocationWeatherId),
                        selectedLocationWeatherId);
            }
        }else{
            ToastUtils.showToast("网络不可用！");
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void startBackgroundService(){
        startService(new Intent(this, ScheduledTaskService.class));
    }

    /**
     * 请求权限并初始化数据库
     */
    public void requestPermission(){
        ArrayList<String> permissions = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.INTERNET);
        }else{
            initializeDatabase();
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            mLocatePermissionFlag = false;
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }else{
            mLocatePermissionFlag = true;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            mLocatePermissionFlag = false;
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }else{
            mLocatePermissionFlag = true;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            mLocatePermissionFlag = false;
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }else{
            mLocatePermissionFlag = true;
        }
        if(mLocatePermissionFlag){
            startBackgroundService();
        }
        String[] tmp = new String [permissions.size()];
        permissions.toArray(tmp);
        if(tmp.length>0){
            ActivityCompat.requestPermissions(this,tmp, Constants.PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constants.PERMISSION_REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    permissionResult(permissions[i],grantResults[i]);
                }
                if(mLocatePermissionFlag){
                    startBackgroundService();
                }else{
                    permissionDeny();
                }
                break;
            default:
                break;
        }
    }

    private boolean mLocatePermissionFlag = false;
    private void permissionResult(String permission,int resultCode){
        if(permission.equals(Manifest.permission.INTERNET)){
            if(resultCode == PackageManager.PERMISSION_GRANTED){
                initializeDatabase();
            }else{
                permissionDeny();
            }
        }else if(permission.equals(Manifest.permission.READ_PHONE_STATE)){
            if(resultCode == PackageManager.PERMISSION_GRANTED){
                mLocatePermissionFlag = true;
            }else{
                mLocatePermissionFlag = false;
            }
        }else if(permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)){
            if(resultCode == PackageManager.PERMISSION_GRANTED){
                mLocatePermissionFlag = true;
            }else{
                mLocatePermissionFlag = false;
            }
        }else if(permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            if(resultCode == PackageManager.PERMISSION_GRANTED){
                mLocatePermissionFlag = true;
            }else{
                mLocatePermissionFlag = false;
            }
        }
    }

    private void permissionDeny() {
        ToastUtils.showToast("权限被拒绝，应用无法被正常使用，应用将在2s之后自动退出");
        mHandler.sendEmptyMessageDelayed(Constants.EVENT_EXIT_APP,2000);
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.EVENT_EXIT_APP:
                    LoveWeatherApplication.exitApp();
                    break;
//                case 1:
//                    break;
//                case :
//
//                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_city:
                startActivity(new Intent(this,CountryManagerActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(this,mCity,"location").toBundle());
                break;
            case R.id.rl_calendar:
                startActivity(new Intent(this,DailyWordActivity.class));
                overridePendingTransition(-1,-1);
                break;
            case R.id.iv_settings:
                startActivity(new Intent(this,SettingsActivity.class));
                break;
            default:
                break;
        }
    }

    private void toggleBackgroundPic(String localPath){
        try{
            BitmapUtils.backgroundBitmapTransition(mRoot,localPath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onStop() {
        //在页面不可见的情况下，不需要自动更新背景
        mInstance.saveShouldUpdatePic(false);
        super.onStop();
    }

    @Override
    protected void onStart() {
        mInstance.saveShouldUpdatePic(true);
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }


    class WeatherBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.BROADCAST_UPDATE_PIC)){
                LogUtils.e("收到广播，开始更新图片");
                String path = intent.getStringExtra("path");
                if(!TextUtils.isEmpty(path)){
                    toggleBackgroundPic(path);
                }
            }
        }
    }

    @Override
    public void onWeatherUpdateSuccess(final Weather weather) {
        UIUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                updateWeather(weather);
                if(HttpUtils.isNetworkAvailable()){
                    //因为有可能是从本地缓存读取的，所以造点假数据，哈哈哈哈，迷之微笑
                    ToastUtils.showToast("天气更新成功");
                }
            }
        });
    }

    private void updateWeather(Weather weather) {
        String locationInfo = WeatherChooseUtils.clipLocationInfo(
                mInstance.getLastSelectedLocationInfo());
        if(!TextUtils.isEmpty(locationInfo)){
            mCity.setText(locationInfo);
        }
        if(weather != null){
            mFirstViewHelper.refreshView(weather);
            mSecondPageViewHelper.refreshView(weather);
        }else{//天气数据为空
            mFirstViewHelper.cleanViewData();
        }
        if(mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onWeatherUpdateFailed(Exception e) {
        UIUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                //从缓存中读取
                ToastUtils.showToast("天气更新失败，请检查网络设置！");
                updateWeather(mUpdateUtils.getWeatherCache(mInstance.getSelectedLocationWeatherId()));
            }
        });
    }


    /**
     * 这里用eventBus代替了系统的广播机制
     * 定位消息：
     *      1.开始定位
     *      2.定位失败
     *      3.定位成功，但是数据库没有找到该地点
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewInformation(String event){
        if(event.equals(Constants.EVENT_START_LOCATE)){
            mCity.setText(UIUtils.getString(R.string.locating));
        }else if(event.equals(Constants.EVENT_LOCATE_FAILED)){
            String locationInfo = WeatherChooseUtils.clipLocationInfo(
                    mInstance.getLastSelectedLocationInfo());
            if(!TextUtils.isEmpty(locationInfo)){
                mCity.setText(locationInfo);
            }else{
                mCity.setText(UIUtils.getString(R.string.locate_failed));
            }
            ToastUtils.showToast("定位失败，请检查您的设置！");
        }else if(event.equals(Constants.EVENT_WEATHER_ID_NOT_FOUND)){
            String locationInfo = WeatherChooseUtils.clipLocationInfo(
                    mInstance.getLastSelectedLocationInfo());
            if(!TextUtils.isEmpty(locationInfo)){
                mCity.setText(locationInfo);
            }else{
                mCity.setText(UIUtils.getString(R.string.server_not_support));
            }
            ToastUtils.showToast("您所在的区域好像无法获取天气信息！");
        }
        if(mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }
        LogUtils.e("在主页面收到");
    }

}
