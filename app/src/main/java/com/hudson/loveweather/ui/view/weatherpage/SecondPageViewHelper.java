package com.hudson.loveweather.ui.view.weatherpage;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.Weather;
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
    private View mHourForecastContainer, mDailyForecastContainer, mSuggestionContainer;
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
        mSuggestionContainer = root.findViewById(R.id.ll_suggestions);
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
    public void refreshView(Weather object, Object... objects) {
        if (object != null) {
            List<Weather.HeWeatherBean> heWeather = object.getHeWeather();
            if (heWeather != null && heWeather.size() > 0) {
                Weather.HeWeatherBean heWeatherBean = heWeather.get(0);
                if (heWeatherBean != null && heWeatherBean.getStatus().equals("ok")) {
                    List<Weather.HeWeatherBean.HourlyForecastBean> hourlyForecast = heWeatherBean.getHourly_forecast();
                    mHour.removeAllViews();
                    inflateHourForecast(hourlyForecast);

                    List<Weather.HeWeatherBean.DailyForecastBean> daily_forecast = heWeatherBean.getDaily_forecast();
                    mDaily.removeAllViews();
                    inflateDailyForecast(daily_forecast);

                    mSuggestions.removeAllViews();
                    itemSuggestionInflate(heWeatherBean.getSuggestion());
                }
            }
        }
    }


    /**
     * 很奇怪，如果使用inflate的自动加入功能，会发现结果不正确，某一个元素多次添加
     * @param suggestion
     */
    private void itemSuggestionInflate(Weather.HeWeatherBean.SuggestionBean suggestion){
        if(suggestion==null){
            mSuggestionContainer.setVisibility(View.GONE);
            return ;
        }else{
            mSuggestionContainer.setVisibility(View.VISIBLE);
        }
        ItemSuggestionViewHelper helper = new ItemSuggestionViewHelper(mContext,mSuggestions);
        Weather.HeWeatherBean.SuggestionBean.AirBean air = suggestion.getAir();
        helper.refreshView("空气",air.getBrf(),air.getTxt());
        mSuggestions.addView(helper.mRoot);
        View.inflate(mContext,R.layout.line_sub,mSuggestions);

        helper = new ItemSuggestionViewHelper(mContext,mSuggestions);
        Weather.HeWeatherBean.SuggestionBean.ComfBean comf = suggestion.getComf();
        helper.refreshView("舒适",comf.getBrf(),comf.getTxt());
        mSuggestions.addView(helper.mRoot);
        View.inflate(mContext,R.layout.line_sub,mSuggestions);

        helper = new ItemSuggestionViewHelper(mContext,mSuggestions);
        Weather.HeWeatherBean.SuggestionBean.CwBean cw = suggestion.getCw();
        helper.refreshView("洗车",cw.getBrf(),cw.getTxt());
        mSuggestions.addView(helper.mRoot);
        View.inflate(mContext,R.layout.line_sub,mSuggestions);

        helper = new ItemSuggestionViewHelper(mContext,mSuggestions);
        Weather.HeWeatherBean.SuggestionBean.DrsgBean drsg = suggestion.getDrsg();
        helper.refreshView("穿衣",drsg.getBrf(),drsg.getTxt());
        mSuggestions.addView(helper.mRoot);
        View.inflate(mContext,R.layout.line_sub,mSuggestions);

        helper = new ItemSuggestionViewHelper(mContext,mSuggestions);
        Weather.HeWeatherBean.SuggestionBean.FluBean flu = suggestion.getFlu();
        helper.refreshView("感冒",flu.getBrf(),flu.getTxt());
        mSuggestions.addView(helper.mRoot);
        View.inflate(mContext,R.layout.line_sub,mSuggestions);

        helper = new ItemSuggestionViewHelper(mContext,mSuggestions);
        Weather.HeWeatherBean.SuggestionBean.SportBean sport = suggestion.getSport();
        helper.refreshView("运动",sport.getBrf(),sport.getTxt());
        mSuggestions.addView(helper.mRoot);
        View.inflate(mContext,R.layout.line_sub,mSuggestions);

        helper = new ItemSuggestionViewHelper(mContext,mSuggestions);
        Weather.HeWeatherBean.SuggestionBean.TravBean trav = suggestion.getTrav();
        helper.refreshView("旅游",trav.getBrf(),trav.getTxt());
        mSuggestions.addView(helper.mRoot);
        View.inflate(mContext,R.layout.line_sub,mSuggestions);

        helper = new ItemSuggestionViewHelper(mContext,mSuggestions);
        Weather.HeWeatherBean.SuggestionBean.UvBean uv = suggestion.getUv();
        helper.refreshView("紫外",uv.getBrf(),uv.getTxt());
        mSuggestions.addView(helper.mRoot);
        View.inflate(mContext,R.layout.line_sub,mSuggestions);
    }

    private void inflateDailyForecast(List<Weather.HeWeatherBean.DailyForecastBean> daily_forecast){
        if(daily_forecast == null|(daily_forecast != null && daily_forecast.size() == 0)){
            mDailyForecastContainer.setVisibility(View.GONE);
            return;
        }else{
            mDailyForecastContainer.setVisibility(View.VISIBLE);
        }
        ItemDailyForecastViewHelper helper;
        Weather.HeWeatherBean.DailyForecastBean bean;
        for (int i = 0; i < daily_forecast.size(); i++) {
            bean = daily_forecast.get(i);
            helper = new ItemDailyForecastViewHelper(mContext,mDaily);
            helper.refreshView(bean);
            mDaily.addView(helper.mRoot);
        }
    }


    private void inflateHourForecast(List<Weather.HeWeatherBean.HourlyForecastBean> hourlyForecast){
        if(hourlyForecast == null|(hourlyForecast != null && hourlyForecast.size() == 0)){
            mHourForecastContainer.setVisibility(View.GONE);
        }else{
            mHourForecastContainer.setVisibility(View.VISIBLE);
        }
        ItemHourForecastViewHelper hourForecastViewHelper;
        Weather.HeWeatherBean.HourlyForecastBean hourlyForecastBean;
        for (int i = 0; i < hourlyForecast.size(); i++) {
            hourlyForecastBean = hourlyForecast.get(i);
            hourForecastViewHelper = new ItemHourForecastViewHelper(mContext,mHour);
            Weather.HeWeatherBean.HourlyForecastBean.WindBeanXX wind = hourlyForecastBean.getWind();
            hourForecastViewHelper.refreshView(i==0,hourlyForecastBean.getTmp(),
                    hourlyForecastBean.getCond().getTxt(),
                    "相对湿度"+hourlyForecastBean.getHum()+"%",
                    "降水概率"+hourlyForecastBean.getPop()+"%",
                    "气压 "+hourlyForecastBean.getPres(),
                    wind.getDir()+"-"+wind.getSc(),
                    hourlyForecastBean.getDate());
            mHour.addView(hourForecastViewHelper.mRoot);
        }
    }
}
