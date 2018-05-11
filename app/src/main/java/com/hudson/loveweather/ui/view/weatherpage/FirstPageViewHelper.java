package com.hudson.loveweather.ui.view.weatherpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.AirQualityBean;
import com.hudson.loveweather.bean.Weather6;
import com.hudson.loveweather.utils.ToastUtils;
import com.hudson.loveweather.utils.log.LogUtils;

import java.util.List;

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
    public void refreshView(Weather6 object, Object... objects) {
        if(object!=null){
            List<Weather6.HeWeather6Bean> heWeather = object.getHeWeather6();
            if(heWeather!=null&&heWeather.size()>0){
                Weather6.HeWeather6Bean heWeather6Bean = heWeather.get(0);
                if(heWeather6Bean !=null&& heWeather6Bean.getStatus().equals("ok")){
                    mUpdateTime.setText(heWeather6Bean.getUpdate().getLoc());
//                    Weather.HeWeather6Bean.AqiBean aqi = heWeather6Bean.getAqi();
//                    if(aqi!=null) {
//                        mAirDesc.setText("空气  " + aqi.getCity().getQlty());
//                    }
                    Weather6.HeWeather6Bean.NowBean now = heWeather6Bean.getNow();
                    if(now!=null){
                        mTemp.setText(now.getTmp());
                        mWeatherDesc.setText(now.getCond_txt());
                        mBodyTemp.setText("体感温度"+now.getFl()+"℃");
                        mHum.setText("空气湿度"+now.getHum()+"%");
                        mWind.setText(now.getWind_dir());
                        //air图片
                    }
                }else{//可能是url错误，导致json格式不正确，或者访问次数达到限制
                    ToastUtils.showToast("服务器数据异常！");
                }
            }
        }
    }

    public void updateAirQuality(AirQualityBean bean){
        if(bean!=null){
            try{
                LogUtils.e("在这里了");
                mAirDesc.setText("空气 "+bean.getHeWeather6().get(0).getAir_now_city().getQlty());
            }catch (NullPointerException e){
                e.printStackTrace();
                LogUtils.e("异常了");
                mAirDesc.setText("空气质量数据获取失败");
            }
        }
    }

    public void cleanViewData(){
        mUpdateTime.setText("数据获取失败");
        mAirDesc.setText("");
        mTemp.setText("0");
        mWeatherDesc.setText("");
        mBodyTemp.setText("");
        mHum.setText("");
        mWind.setText("");
        //air图片
    }
}
