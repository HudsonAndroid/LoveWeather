package com.hudson.loveweather.ui.view.weatherpage;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.Weather6;
import com.hudson.loveweather.ui.view.customview.ItemDailyForecastViewHelper;
import com.hudson.loveweather.ui.view.customview.ItemHourForecastViewHelper;
import com.hudson.loveweather.ui.view.customview.ItemSuggestionViewHelper;
import com.hudson.loveweather.utils.BitmapUtils;

import java.util.List;

/**
 * Created by Hudson on 2017/12/4.
 */

public class SecondPageViewHelper extends PageViewHelperImpl {
    private View mContainer;
    private Context mContext;
    private View mHourForecastContainer, mDailyForecastContainer, mLifeStyleContainer;
    private LinearLayout mHour, mDaily, mSuggestions;

    @Override
    View inflate(Context context, ViewGroup parent) {
        mContext = context;
        return LayoutInflater.from(context).inflate(R.layout.weather_second_page, parent, false);
    }

    @Override
    int getViewHeight(int screenHeight, int actionbarHeight) {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    void initView(View root) {
        mContainer = root.findViewById(R.id.ll_second);
        mHourForecastContainer = root.findViewById(R.id.ll_hour_forecast);
        mDailyForecastContainer = root.findViewById(R.id.ll_daily_forecast);
        mLifeStyleContainer = root.findViewById(R.id.ll_suggestions);
        mDaily = (LinearLayout) root.findViewById(R.id.ll_daily);
        mHour = (LinearLayout) root.findViewById(R.id.ll_hour);
        mSuggestions = (LinearLayout) root.findViewById(R.id.ll_suggestions_container);
        mContainer.post(new Runnable() {
            @Override
            public void run() {
                mContainer.setBackground(new BitmapDrawable(BitmapUtils.createDarkTransitionBitmap(
                        mContainer.getWidth(), mContainer.getHeight(),
                        new int[]{0x00000000, 0xaa000000, 0xaa000000, 0x00000000},
                        new float[]{0.0f, 0.12f, 0.88f, 1.0f})));
            }
        });

    }

    @Override
    public void refreshView(Weather6 object, Object... objects) {
        if (object != null) {
            List<Weather6.HeWeather6Bean> heWeather = object.getHeWeather6();
            if (heWeather != null && heWeather.size() > 0) {
                Weather6.HeWeather6Bean heWeather6Bean = heWeather.get(0);
                if (heWeather6Bean != null && heWeather6Bean.getStatus().equals("ok")) {
                    List<Weather6.HeWeather6Bean.HourlyBean> hourlyForecast = heWeather6Bean.getHourly();
                    mHour.removeAllViews();
                    if(hourlyForecast!=null&&hourlyForecast.size()>0){
                        inflateHourForecast(hourlyForecast);
                    }

                    List<Weather6.HeWeather6Bean.DailyForecastBean> daily_forecast = heWeather6Bean.getDaily_forecast();
                    mDaily.removeAllViews();
                    if(daily_forecast!=null&&daily_forecast.size()>0){
                        inflateDailyForecast(daily_forecast);
                    }

                    mSuggestions.removeAllViews();
                    itemSuggestionInflate(heWeather6Bean.getLifestyle());
                }
            }
        }
    }


    /**
     * 很奇怪，如果使用inflate的自动加入功能，会发现结果不正确，某一个元素多次添加
     * @param lifeStyleList
     */
    private void itemSuggestionInflate(List<Weather6.HeWeather6Bean.LifestyleBean> lifeStyleList){
        if(lifeStyleList == null|(lifeStyleList != null && lifeStyleList.size() == 0)){
            mLifeStyleContainer.setVisibility(View.GONE);
            return ;
        }else{
            mLifeStyleContainer.setVisibility(View.VISIBLE);
        }
        ItemSuggestionViewHelper helper;
        Weather6.HeWeather6Bean.LifestyleBean lifestyleBean;
        for (int i = 0; i < lifeStyleList.size(); i++) {
            lifestyleBean = lifeStyleList.get(i);
            helper = new ItemSuggestionViewHelper(mContext,mSuggestions);
            String type = generateTypeStr(lifestyleBean.getType());
            if(lifestyleBean!=null){
                helper.refreshView(type,lifestyleBean.getBrf(),lifestyleBean.getTxt());
                mSuggestions.addView(helper.mRoot);
                View.inflate(mContext,R.layout.line_sub,mSuggestions);
            }
        }
    }

    private String generateTypeStr(String type){
        if(type.equals("comf")){
            return "舒适";
        }else if(type.equals("drsg")){
            return "穿衣";
        }else if(type.equals("flu")){
            return "感冒";
        }else if(type.equals("sport")){
            return "运动";
        }else if(type.equals("trav")){
            return "旅行";
        }else if(type.equals("uv")){
            return "紫外";
        }else if(type.equals("cw")){
            return "洗车";
        }else if(type.equals("air")){
            return "空气";
        }else{
            return "没有了";
        }
    }

    private void inflateDailyForecast(List<Weather6.HeWeather6Bean.DailyForecastBean> daily_forecast){
        if(daily_forecast == null|(daily_forecast != null && daily_forecast.size() == 0)){
            mDailyForecastContainer.setVisibility(View.GONE);
            return;
        }else{
            mDailyForecastContainer.setVisibility(View.VISIBLE);
        }
        ItemDailyForecastViewHelper helper;
        Weather6.HeWeather6Bean.DailyForecastBean bean;
        for (int i = 0; i < daily_forecast.size(); i++) {
            bean = daily_forecast.get(i);
            helper = new ItemDailyForecastViewHelper(mContext,mDaily);
            helper.refreshView(bean);
            mDaily.addView(helper.mRoot);
        }
    }


    private void inflateHourForecast(List<Weather6.HeWeather6Bean.HourlyBean> hourlyForecast){
        if(hourlyForecast == null|(hourlyForecast != null && hourlyForecast.size() == 0)){
            mHourForecastContainer.setVisibility(View.GONE);
        }else{
            mHourForecastContainer.setVisibility(View.VISIBLE);
        }
        ItemHourForecastViewHelper hourForecastViewHelper;
        Weather6.HeWeather6Bean.HourlyBean hourlyForecastBean;
        for (int i = 0; i < hourlyForecast.size(); i++) {
            hourlyForecastBean = hourlyForecast.get(i);
            hourForecastViewHelper = new ItemHourForecastViewHelper(mContext,mHour);
            hourForecastViewHelper.refreshView(i==0,hourlyForecastBean.getTmp(),
                    hourlyForecastBean.getCond_txt(),
                    "相对湿度"+hourlyForecastBean.getHum()+"%",
                    "降水概率"+hourlyForecastBean.getPop()+"%",
                    "气压 "+hourlyForecastBean.getPres(),
                    hourlyForecastBean.getWind_dir()+"-"+hourlyForecastBean.getWind_sc(),
                    hourlyForecastBean.getTime());
            mHour.addView(hourForecastViewHelper.mRoot);
        }
    }
}
