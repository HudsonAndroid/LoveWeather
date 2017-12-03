package com.hudson.loveweather.ui.fragment;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.db.Country;
import com.hudson.loveweather.db.DatabaseUtils;
import com.hudson.loveweather.service.ScheduledTaskService;
import com.hudson.loveweather.ui.activity.WeatherActivity;
import com.hudson.loveweather.ui.view.customview.AutoAdapterLayout;
import com.hudson.loveweather.utils.ArrayIteratorHelper;
import com.hudson.loveweather.utils.SearchHistoryUtils;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.ToastUtils;
import com.hudson.loveweather.utils.UIUtils;
import com.hudson.loveweather.utils.log.Constants;
import com.hudson.loveweather.utils.log.LogUtils;

import java.util.Iterator;

/**
 * Created by Hudson on 2017/12/1.
 */

public class SearchDefaultFragment extends BaseFragment implements View.OnClickListener {
    private static final int CAKE_MARGIN = 15;
    private TextView mLocationText;
    private AutoAdapterLayout mHistoryContainer,mHotCityContainer;
    private SearchHistoryUtils mHistoryUtils;
    private ViewStub mHistory;

    @Override
    public View initView(LayoutInflater inflater) {
        View root = inflater.inflate(R.layout.fragment_search_default, null);
        mLocationText = (TextView) root.findViewById(R.id.tv_current_location);
        mLocationText.setText(SharedPreferenceUtils.getInstance().getLastLocationInfo());
        root.findViewById(R.id.ll_current_locate).setOnClickListener(this);
        mHotCityContainer = (AutoAdapterLayout) root.findViewById(R.id.aal_hot_city_container);
        mHistory = (ViewStub) root.findViewById(R.id.vs_history);
        return root;
    }

    @Override
    public void initData() {
        mHistoryUtils = SearchHistoryUtils.getInstance();
        initializeHistory();
        generateHotCity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_current_locate:
                mActivity.startService(new Intent(mActivity, ScheduledTaskService.class));
                mActivity.startActivity(new Intent(mActivity, WeatherActivity.class));
                mActivity.finish();
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

    @Override
    public void onDestroy() {
        mHistoryUtils.commit();
        super.onDestroy();
    }

    private void initializeHistory(){
        int size = mHistoryUtils.getSize();
        if(size>0){
            View historyRoot = mHistory.inflate();
            mHistoryContainer = (AutoAdapterLayout) historyRoot.findViewById(R.id.aal_history_container);
            generateListCountry(mHistoryUtils,mHistoryContainer);
            //需要重置iterator，因为我们的historyUtils是一个单例
            mHistoryUtils.resetPosition();
        }
    }


    private void generateHotCity(){
        String[] hotCities = com.hudson.loveweather.global.Constants.HOT_CITIES;
        generateListCountry(new ArrayIteratorHelper<>(hotCities),mHotCityContainer);
    }

    /**
     * 产生一个列表，可以是历史搜索、热门城市
     * @param iterator 迭代器
     * @param container view容器
     */
    private void generateListCountry(Iterator iterator,ViewGroup container){
        while (iterator.hasNext()){
            TextView child = new TextView(getActivity());
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = CAKE_MARGIN;
            layoutParams.topMargin = CAKE_MARGIN;
            final String country = iterator.next().toString();
            child.setText(country);
            child.setTextColor(UIUtils.getColor(R.color.white));
            child.setGravity(Gravity.CENTER);
            child.setBackgroundResource(R.drawable.selector_textview_city);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSelectedCountry(country);
                    SearchHistoryUtils instance = SearchHistoryUtils.getInstance();
                    instance.addHistory(country);//作为历史记录存入
                    instance.commit();
                }
            });
            container.addView(child,layoutParams);
        }
    }

    private void toggleSelectedCountry(String countryName){
        Country country = DatabaseUtils.queryWeatherId(countryName);
        if(country!=null){
            mActivity.toggleSelectedCountry(country);
        }
    }

}
