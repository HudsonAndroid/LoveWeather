package com.hudson.loveweather.ui.view.weatherpage;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hudson.loveweather.R;
import com.hudson.loveweather.bean.Weather;
import com.hudson.loveweather.utils.BitmapUtils;

/**
 * Created by Hudson on 2017/12/4.
 */

public class SecondPageViewHelper extends PageViewHelperImpl {
    private View mContainer;

    @Override
    View inflate(Context context, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.weather_second_page,parent,false);
    }

    @Override
    void initView(View root) {
        mContainer = root.findViewById(R.id.ll_second);
        mContainer.post(new Runnable() {
            @Override
            public void run() {
                mContainer.setBackground(new BitmapDrawable(BitmapUtils.createDarkTransitionBitmap(
                        mContainer.getWidth(),mContainer.getHeight(),
                        new int[]{0x00000000,0x77000000,0x77000000,0x00000000},
                        new float[]{0.0f,0.2f,0.95f,1.0f})));
            }
        });

    }

    @Override
    public void refreshView(Weather object, Object... objects) {

    }
}
