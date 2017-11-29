package com.hudson.loveweather.ui.view.weatherpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.Weather;

/**
 * Created by Hudson on 2017/11/28.
 */

public class FirstPageViewHelper extends PageViewHelperImpl {

    private TextView mUpdateTime,mAirDesc
            ,mTemp,mWeatherDesc,mBodyTemp,mHum,mWind;
    private ImageView mAirLevel;

    @Override
    View inflate(Context context, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.weather_first_page,
                parent,false);
    }

    @Override
     void initView(View root) {
        mUpdateTime = (TextView) root.findViewById(R.id.tv_update_time);
        mAirDesc = (TextView) root.findViewById(R.id.tv_air_desc);
        mTemp = (TextView) root.findViewById(R.id.tv_temp);
        mWeatherDesc = (TextView) root.findViewById(R.id.tv_weather_desc);
        mBodyTemp = (TextView) root.findViewById(R.id.tv_body_temp);
        mHum = (TextView) root.findViewById(R.id.tv_hum);
        mWind = (TextView) root.findViewById(R.id.tv_wind);
        mAirLevel = (ImageView) root.findViewById(R.id.iv_air_img);
        root.findViewById(R.id.ll_weather_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了温度一块，显示实时天气详情

            }
        });
        root.findViewById(R.id.ll_air).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了空气，显示空气详情
            }
        });

    }

    @Override
    public void refreshView(Weather object, Object... objects) {
        if(object!=null){
            Weather.HeWeatherBean heWeatherBean = object.getHeWeather().get(0);
            mUpdateTime.setText(heWeatherBean.getBasic().getUpdate().getLoc());
            mAirDesc.setText("空气  "+heWeatherBean.getAqi().getCity().getQlty());
            Weather.HeWeatherBean.NowBean now = heWeatherBean.getNow();
            mTemp.setText(now.getTmp());
            mWeatherDesc.setText(now.getCond().getTxt().trim());
            mBodyTemp.setText("体感温度"+now.getFl()+"℃");
            mHum.setText("空气湿度"+now.getHum()+"%");
            mWind.setText(now.getWind().getSc());
            //air图片
        }
    }
}
