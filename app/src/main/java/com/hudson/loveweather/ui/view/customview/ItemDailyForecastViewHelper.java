package com.hudson.loveweather.ui.view.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.Weather;

/**
 * Created by Hudson on 2017/12/9.
 */

public class ItemDailyForecastViewHelper {
    public View mRoot;
    private TextView mSunrise,mSunset,mMoonrise,mMoonSet, mDayWeather,mNightWeather,mMaxMin,
        mHum,mPress,mPop,mPcpn,mVu,mVis,mWindDir,mWindSc,mDate;

    public ItemDailyForecastViewHelper(Context context, ViewGroup parent){
        mRoot = LayoutInflater.from(context).inflate(R.layout.item_daily_forecast,parent,false);
        mSunrise = (TextView) mRoot.findViewById(R.id.tv_sunrise);
        mSunset = (TextView) mRoot.findViewById(R.id.tv_sunset);
        mMoonrise = (TextView) mRoot.findViewById(R.id.tv_moonrise);
        mMoonSet = (TextView) mRoot.findViewById(R.id.tv_moonset);
        mDayWeather = (TextView) mRoot.findViewById(R.id.tv_day_weather);
        mNightWeather = (TextView) mRoot.findViewById(R.id.tv_night_weather);
        mMaxMin = (TextView) mRoot.findViewById(R.id.tv_max_min);
        mHum = (TextView) mRoot.findViewById(R.id.tv_hum);
        mPress = (TextView) mRoot.findViewById(R.id.tv_press);
        mPop = (TextView) mRoot.findViewById(R.id.tv_pop);
        mPcpn = (TextView) mRoot.findViewById(R.id.tv_pcpn);
        mVis = (TextView) mRoot.findViewById(R.id.tv_vis);
        mVu = (TextView) mRoot.findViewById(R.id.tv_uv);
        mWindDir = (TextView) mRoot.findViewById(R.id.tv_dir);
        mWindSc = (TextView) mRoot.findViewById(R.id.tv_sc);
        mDate = (TextView) mRoot.findViewById(R.id.tv_date);
    }


    public void refreshView(Weather.HeWeatherBean.DailyForecastBean bean){
        Weather.HeWeatherBean.DailyForecastBean.AstroBean astro = bean.getAstro();
        mSunrise.setText(astro.getSr());
        mSunset.setText(astro.getSs());
        mMoonrise.setText(astro.getMr());
        mMoonSet.setText(astro.getMs());
        Weather.HeWeatherBean.DailyForecastBean.TmpBean tmp = bean.getTmp();
        mMaxMin.setText(tmp.getMin()+"~"+tmp.getMax()+"℃");
        Weather.HeWeatherBean.DailyForecastBean.CondBeanX cond = bean.getCond();
        mDayWeather.setText(cond.getTxt_d());
        mNightWeather.setText(cond.getTxt_n());
        mHum.setText("相对湿度"+bean.getHum()+"%");
        mPop.setText("降水概率 "+bean.getPop()+"%");
        mPcpn.setText("降水量 "+bean.getPcpn());
        mPress.setText("气压 "+bean.getPres());
        mVis.setText("能见度 "+bean.getVis());
        mVu.setText("紫外线强度 "+bean.getUv());
        mDate.setText(bean.getDate());
        Weather.HeWeatherBean.DailyForecastBean.WindBeanX wind = bean.getWind();
        mWindSc.setText(wind.getSc());
        mWindDir.setText(wind.getDir());
    }

}
