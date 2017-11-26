package com.hudson.loveweather.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.hudson.loveweather.R;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.global.LoveWeatherApplication;
import com.hudson.loveweather.service.DataInitializeService;
import com.hudson.loveweather.utils.LogUtils.LogUtils;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.ToastUtils;

public class WeatherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        requestPermission();
    }

    private void initializeDatabase() {
        if(!SharedPreferenceUtils.getInstance().isLocalDatabaseLoaded()){
            LogUtils.log("没有初始化过，所以开始启动服务加载");
            startService(new Intent(this, DataInitializeService.class));
        }else{
            LogUtils.log("已经初始化过了");
        }
    }

    public void requestPermission(){
        LogUtils.log("请求权限");
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED){
            LogUtils.log("没有权限");
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, Constants.PERMISSION_INTERNET_CODE);
        }else{
            LogUtils.log("有权限");
            initializeDatabase();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constants.PERMISSION_INTERNET_CODE:
                if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initializeDatabase();
                }else{
                    ToastUtils.showToast("权限被拒绝，应用无法被正常使用，应用将在2s之后自动退出");
                    mHandler.sendEmptyMessageDelayed(Constants.EVENT_EXIT_APP,2000);
                }
                break;
//            case :
//
//                break;
//            case :
//
//                break;
            default:
                break;
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.EVENT_EXIT_APP:
                    LoveWeatherApplication.exitApp();
                    break;
//                case :
//
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
    protected void onDestroy() {
        super.onDestroy();
        LoveWeatherApplication.exitApp();
    }
}
