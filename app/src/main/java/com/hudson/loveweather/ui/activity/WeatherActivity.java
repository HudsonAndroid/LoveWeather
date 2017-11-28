package com.hudson.loveweather.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.global.LoveWeatherApplication;
import com.hudson.loveweather.service.DataInitializeService;
import com.hudson.loveweather.service.ScheduledTaskService;
import com.hudson.loveweather.utils.BitmapUtils;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.TimeUtils;
import com.hudson.loveweather.utils.ToastUtils;
import com.hudson.loveweather.utils.log.LogUtils;

import java.util.ArrayList;

public class WeatherActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTextView;
    private TextView mCalendar;
    private View mRoot;
    private WeatherBroadCastReceiver mReceiver;

    @Override
    public void setContentViewAndInit() {
        setContentView(R.layout.activity_weather);
        requestPermission();
        mReceiver = new WeatherBroadCastReceiver();
        IntentFilter filter = new IntentFilter(Constants.BROADCAST_UPDATE_PIC);
        registerReceiver(mReceiver,filter);
        startService(new Intent(this, ScheduledTaskService.class));
    }

    @Override
    public void initView() {
        mRoot = findViewById(R.id.ll_weather);
        mTextView = (TextView) this.findViewById(R.id.tv_city);
        mTextView.setOnClickListener(this);
        findViewById(R.id.rl_calendar).setOnClickListener(this);
        mCalendar = (TextView) this.findViewById(R.id.tv_calendar);
        mCalendar.setText(TimeUtils.getDayNumberOfDate());
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
        unregisterReceiver(mReceiver);
        LoveWeatherApplication.exitApp();
    }

    private void initializeDatabase() {
        if(!SharedPreferenceUtils.getInstance().isLocalDatabaseLoaded()){
            LogUtils.log("没有初始化过，所以开始启动服务加载");
            startService(new Intent(this, DataInitializeService.class));
        }else{
            LogUtils.log("已经初始化过了");
        }
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
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        String[] tmp = new String [permissions.size()];
        permissions.toArray(tmp);
        if(tmp!=null&&tmp.length>0){
            ActivityCompat.requestPermissions(this,tmp, Constants.PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constants.PERMISSION_REQUEST_CODE:
                if(grantResults.length>0){
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        initializeDatabase();
                    }else if(grantResults[1] == PackageManager.PERMISSION_GRANTED){
                        //文件权限被允许
                    }
                }else{
                    ToastUtils.showToast("权限被拒绝，应用无法被正常使用，应用将在2s之后自动退出");
                    mHandler.sendEmptyMessageDelayed(Constants.EVENT_EXIT_APP,2000);
                }
                break;
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_city:
//                ToastUtils.showToast("切换城市了");
//                BitmapUtils.backgroundAnimation(mRoot);
                break;
            case R.id.rl_calendar:
                startActivity(new Intent(this,DailyWordActivity.class));
                overridePendingTransition(-1,-1);
                break;
//            case :
//
//                break;
            default:
                break;
        }
    }

    private void toggleBackgroundPic(String localPath){
        try{
            BitmapUtils.backgroundBitmapTransition(mRoot,
                    BitmapFactory.decodeFile(localPath));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        //在页面不可见的情况下，不需要自动更新背景
        SharedPreferenceUtils.getInstance().saveShouldUpdatePic(false);
        super.onStop();
    }

    @Override
    protected void onStart() {
        SharedPreferenceUtils.getInstance().saveShouldUpdatePic(true);
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
}
