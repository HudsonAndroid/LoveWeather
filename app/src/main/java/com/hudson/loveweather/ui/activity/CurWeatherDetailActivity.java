package com.hudson.loveweather.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.Weather6;
import com.hudson.loveweather.bean.WeatherDetailItem;
import com.hudson.loveweather.global.WeatherIconCode;
import com.hudson.loveweather.utils.UIUtils;
import com.hudson.loveweather.utils.WeatherChooseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hudson on 2018/5/27.
 */

public class CurWeatherDetailActivity extends BaseSubActivity {
    private Weather6.HeWeather6Bean.NowBean mNowBean;
    private String mCity;
    private TextView tvCity,tvTmp,tvWeatherDesc;
    private ImageView mWeatherIcon;
    private List<WeatherDetailItem> mDatas;
    private GridView mGridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View setContent() {
        View root = LayoutInflater.from(this).inflate(R.layout.activity_cur_weather_detail, null);
        tvCity = (TextView) root.findViewById(R.id.tv_city);
        tvTmp = (TextView) root.findViewById(R.id.tv_temp);
        tvWeatherDesc = (TextView) root.findViewById(R.id.tv_weather_desc);
        mWeatherIcon = (ImageView) root.findViewById(R.id.iv_weather_icon);
        mGridView = (GridView) root.findViewById(R.id.gv_content);
        return root;
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        if(intent == null||intent.getExtras()==null){
            finish();
            return ;
        }
        mCity = WeatherChooseUtils.clipLocationInfo(intent.getStringExtra(BUNDLE_CITY));
        mNowBean = intent.getParcelableExtra(BUNDLE_NOW);
        tvCity.setText(mCity);
        tvTmp.setText(mNowBean.getTmp());
        tvWeatherDesc.setText(mNowBean.getCond_txt());
        mWeatherIcon.setImageResource(WeatherIconCode.getIconByCode(mNowBean.getCond_code()));
        initDatas();
    }

    private void initDatas() {
        if(mDatas == null){
            mDatas = new ArrayList<>();
        }
        mDatas.add(new WeatherDetailItem(UIUtils.getString(R.string.cloud_count),mNowBean.getCloud()));
        mDatas.add(new WeatherDetailItem(UIUtils.getString(R.string.rain_count),mNowBean.getPcpn()));
        mDatas.add(new WeatherDetailItem(UIUtils.getString(R.string.body_tmp),mNowBean.getFl()+"℃"));
        mDatas.add(new WeatherDetailItem(UIUtils.getString(R.string.air_hum),mNowBean.getHum()+"%"));
        mDatas.add(new WeatherDetailItem(UIUtils.getString(R.string.wind_direction),mNowBean.getWind_dir()));
        mDatas.add(new WeatherDetailItem(UIUtils.getString(R.string.wind_power),mNowBean.getWind_sc()+UIUtils.getString(R.string.wind_level)));
        mDatas.add(new WeatherDetailItem(UIUtils.getString(R.string.wind_speed),mNowBean.getWind_spd()+" km/h"));
        mDatas.add(new WeatherDetailItem(UIUtils.getString(R.string.wind_degree),mNowBean.getWind_deg()+"°"));
        mDatas.add(new WeatherDetailItem(UIUtils.getString(R.string.visibility),mNowBean.getVis()+" km"));
        mDatas.add(new WeatherDetailItem(UIUtils.getString(R.string.air_press),mNowBean.getPres()+" hpa"));
        mGridView.setAdapter(new WeatherDetailAdapter());
    }

    @Override
    public String getActivityTitle() {
        return UIUtils.getString(R.string.title_cur_weather_detail);
    }

    @Override
    public void recycle() {

    }

    private static final String BUNDLE_NOW = "nowWeather";
    private static final String BUNDLE_CITY = "city";
    public static void start(Activity from, View transitionView, Weather6.HeWeather6Bean.NowBean now, String city){
        Intent intent = new Intent(from,CurWeatherDetailActivity.class);
        intent.putExtra(BUNDLE_CITY,city);
        intent.putExtra(BUNDLE_NOW,now);
        //需加上如下代码，否则会报错ClassNotFoundException when unmarshalling
        intent.setExtrasClassLoader(Weather6.HeWeather6Bean.NowBean.class.getClassLoader());
        from.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(from,transitionView,"weather_detail").toBundle());
    }


    private class WeatherDetailAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public WeatherDetailItem getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = LayoutInflater.from(CurWeatherDetailActivity.this).inflate(R.layout.item_weather_detail, parent,false);
                viewHolder = new ViewHolder();
                viewHolder.mTitle = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.mContent = (TextView) convertView.findViewById(R.id.tv_content);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            WeatherDetailItem item = mDatas.get(position);
            viewHolder.mTitle.setText(item.getTitle());
            viewHolder.mContent.setText(item.getContent());
            return convertView;
        }
    }

    private static final class ViewHolder{
        private TextView mTitle,mContent;
    }
}
