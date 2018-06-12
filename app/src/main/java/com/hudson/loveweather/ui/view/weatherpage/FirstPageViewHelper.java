package com.hudson.loveweather.ui.view.weatherpage;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.AirQualityBean;
import com.hudson.loveweather.bean.Weather6;
import com.hudson.loveweather.ui.activity.AirQualityActivity;
import com.hudson.loveweather.ui.activity.CurWeatherDetailActivity;
import com.hudson.loveweather.utils.DeviceUtils;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.ToastUtils;
import com.hudson.loveweather.utils.UIUtils;
import com.hudson.loveweather.utils.WeatherMsgFactory;
import com.hudson.loveweather.utils.voice.BgMusic;
import com.hudson.loveweather.utils.voice.VoiceSpeechUtils;
import com.iflytek.cloud.SpeechError;

import java.util.List;

/**
 * Created by Hudson on 2017/11/28.
 */

public class FirstPageViewHelper extends PageViewHelperImpl {

    private TextView mUpdateTime, mAirDesc, mTemp, mWeatherDesc, mBodyTemp, mHum, mWind;
    private ImageView mAirLevel;
    private Activity mContext;
    private Weather6 mWeather6;
    private BgMusic mBgMusic;
    private View mVoice;
    private ObjectAnimator mRotate;
    private VoiceSpeechUtils mSpeakUtils;
    private ProgressDialog mDialog;

    @Override
    View inflate(Activity context, ViewGroup parent) {
        mContext = context;
        mBgMusic = BgMusic.getInstance();
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(UIUtils.getString(R.string.loading_voice));
        return LayoutInflater.from(context).inflate(R.layout.weather_first_page,
                parent, false);
    }

    @Override
    void initView(final View root) {
        mUpdateTime = (TextView) root.findViewById(R.id.tv_update_time);
        mAirDesc = (TextView) root.findViewById(R.id.tv_air_desc);
        mTemp = (TextView) root.findViewById(R.id.tv_temp);
        mWeatherDesc = (TextView) root.findViewById(R.id.tv_weather_desc);
        mBodyTemp = (TextView) root.findViewById(R.id.tv_body_temp);
        mHum = (TextView) root.findViewById(R.id.tv_hum);
        mWind = (TextView) root.findViewById(R.id.tv_wind);
        mAirLevel = (ImageView) root.findViewById(R.id.iv_air_img);
        mSpeakUtils = VoiceSpeechUtils.getInstance();
        final View transitionView = root.findViewById(R.id.ll_transition);
        root.findViewById(R.id.ll_weather_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了温度一块，显示实时天气详情
                try{
                    Weather6.HeWeather6Bean.NowBean now = mWeather6.getHeWeather6().get(0).getNow();
                    if(now != null){
                        CurWeatherDetailActivity.start(mContext,transitionView,
                                now,
                                SharedPreferenceUtils.getInstance().getLastSelectedLocationInfo());
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
        root.findViewById(R.id.ll_air).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了空气，显示空气详情
                AirQualityActivity.start(mContext,mAirQualityBean);
            }
        });
        mVoice = root.findViewById(R.id.iv_voice);
        mVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //isRunning是否正在运行 isStarted是否已经运行  isPaused
                if(mRotate == null){
                    startVoice();
                }else if(mRotate.isPaused()){
                    continueVoice();
                }else{
                    pauseVoice();
                }
            }
        });
        mVoice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mRotate!=null){
                    DeviceUtils.Vibrate(mContext,100);
                    stopVoice();
                    mSpeakUtils.stop();
                    ToastUtils.showToast(R.string.voice_stop_success);
                }
                return true;
            }
        });

    }

    private void startVoice() {
        String readContent = WeatherMsgFactory.generateVoiceMsg(mWeather6);
        if (!TextUtils.isEmpty(readContent)) {
            mRotate = ObjectAnimator.ofFloat(mVoice, "rotation", 0f, 360f);
            mRotate.setRepeatCount(ValueAnimator.INFINITE);
            mRotate.setDuration(5000);
            mRotate.setInterpolator(new LinearInterpolator());
            if(!mDialog.isShowing()){
                mDialog.show();
            }
            mSpeakUtils.startSpeakText(mContext, readContent, new VoiceSpeechUtils.SpeakListener() {
                @Override
                public void onStart() {
                    mDialog.dismiss();
                    mRotate.start();
                    mBgMusic.play();
                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    if(speechError!=null){
                        mDialog.dismiss();
                        ToastUtils.showToast(R.string.error_load_failed);
                    }
                    stopVoice();
                }
            });
        } else {
            ToastUtils.showToast(R.string.error_data_not_exist);
        }
    }

    private void stopVoice(){
        mRotate.end();
        mRotate = null;
        mBgMusic.stop();
    }

    private void pauseVoice(){
        mRotate.pause();
        mBgMusic.pause();
        mSpeakUtils.pause();
    }

    private void continueVoice(){
        mRotate.resume();
        mBgMusic.play();
        mSpeakUtils.continueSpeak();
    }

    @Override
    public void refreshView(Weather6 object, Object... objects) {
        if (object != null) {
            List<Weather6.HeWeather6Bean> heWeather = object.getHeWeather6();
            if (heWeather != null && heWeather.size() > 0) {
                mWeather6 = object;
                Weather6.HeWeather6Bean heWeather6Bean = heWeather.get(0);
                if (heWeather6Bean != null && heWeather6Bean.getStatus().equals("ok")) {
                    mUpdateTime.setText(heWeather6Bean.getUpdate().getLoc());
//                    Weather.HeWeather6Bean.AqiBean aqi = heWeather6Bean.getAqi();
//                    if(aqi!=null) {
//                        mAirDesc.setText("空气  " + aqi.getCity().getQlty());
//                    }
                    Weather6.HeWeather6Bean.NowBean now = heWeather6Bean.getNow();
                    if (now != null) {
                        mTemp.setText(now.getTmp());
                        mWeatherDesc.setText(now.getCond_txt());
                        mBodyTemp.setText("体感温度" + now.getFl() + "℃");
                        mHum.setText("空气湿度" + now.getHum() + "%");
                        mWind.setText(now.getWind_dir());
                    }
                } else {//可能是url错误，导致json格式不正确，或者访问次数达到限制
                    ToastUtils.showToast("服务器数据异常！");
                }
            }
        }
    }

    private AirQualityBean mAirQualityBean;
    public void updateAirQuality(AirQualityBean bean) {
        if (bean != null) {
            try {
                String qlty = bean.getHeWeather6().get(0).getAir_now_city().getQlty();
                mAirDesc.setText(qlty);
                mAirLevel.setVisibility(View.VISIBLE);
                mAirLevel.setImageResource(WeatherMsgFactory.getAirQualityImg(qlty));
                mAirQualityBean = bean;
            } catch (NullPointerException e) {
                e.printStackTrace();
                mAirLevel.setVisibility(View.INVISIBLE);
                mAirDesc.setText("空气质量数据获取失败");
            }
        }
    }

    public void cleanViewData() {
        mUpdateTime.setText("数据获取失败");
        mAirDesc.setText("");
        mTemp.setText("0");
        mWeatherDesc.setText("");
        mBodyTemp.setText("");
        mHum.setText("");
        mWind.setText("");
        mAirLevel.setVisibility(View.INVISIBLE);
    }
}
