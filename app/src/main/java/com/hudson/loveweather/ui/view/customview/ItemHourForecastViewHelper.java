package com.hudson.loveweather.ui.view.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.utils.UIUtils;

/**
 * Created by Hudson on 2017/12/9.
 */

public class ItemHourForecastViewHelper {
    public View mRoot;
    private TextView mTemp,mTempDesc,mHum,mPop,mPress,mWind,mDate;

    public ItemHourForecastViewHelper(Context context, ViewGroup parent){
        mRoot = LayoutInflater.from(context).inflate(R.layout.item_hour_forecast,parent,false);
        mTemp = (TextView) mRoot.findViewById(R.id.tv_temp);
        mTempDesc = (TextView) mRoot.findViewById(R.id.tv_weather_desc);
        mHum = (TextView) mRoot.findViewById(R.id.tv_hum);
        mPop = (TextView) mRoot.findViewById(R.id.tv_pop);
        mPress = (TextView) mRoot.findViewById(R.id.tv_press);
        mWind = (TextView) mRoot.findViewById(R.id.tv_wind);
        mDate = (TextView) mRoot.findViewById(R.id.tv_date);
    }

    public void refreshView(boolean lightHight,String temp,String tempDesc,String hum,String pop,String press,
                            String wind,String date){
        if(!lightHight){
            mTemp.setTextColor(UIUtils.getColor(R.color.gray));
            mTempDesc.setTextColor(UIUtils.getColor(R.color.gray));
            mHum.setTextColor(UIUtils.getColor(R.color.gray));
            mPress.setTextColor(UIUtils.getColor(R.color.gray));
            mPop.setTextColor(UIUtils.getColor(R.color.gray));
            mWind.setTextColor(UIUtils.getColor(R.color.gray));
            mDate.setTextColor(UIUtils.getColor(R.color.gray));
        }
        mTemp.setText(temp);
        mTempDesc.setText(tempDesc);
        mHum.setText(hum);
        mPop.setText(pop);
        mPress.setText(press);
        mWind.setText(wind);
        mDate.setText(getHourTime(date));
    }


    /**
     * 如果有2017-12-9 19:00 那么去掉2017-12-9
     * @param sourceStr
     * @return
     */
    private String getHourTime(String sourceStr){
        if(sourceStr.contains(" ")){
            return sourceStr.substring(sourceStr.lastIndexOf(" "));
        }
        return sourceStr;
    }

}
