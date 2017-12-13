package com.hudson.loveweather.ui.view.weatherpage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hudson.loveweather.bean.Weather;
import com.hudson.loveweather.utils.DeviceUtils;

/**
 * Created by Hudson on 2017/11/28.
 */

public abstract class PageViewHelperImpl implements WeatherPageViewHelper<Weather> {


    /**
     * 第三个参数必须是actionBar高度
     */
    @Override
    public View inflateView(Context context,ViewGroup parent, Object... objects) {
        View v = inflate(context,parent);
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        int[] pixels = new int[2];
        DeviceUtils.getScreen(pixels);
        layoutParams.width = pixels[0];
        //由于scrollbar高度由子view确定，所以只能动态添加高度为屏幕高的页面
        layoutParams.height = getViewHeight(pixels[1],(int)objects[0]);
        parent.addView(v,layoutParams);
        initView(v);
        return v;
    }

    int getViewHeight(int screenHeight,int actionbarHeight){
        return screenHeight - actionbarHeight
                - DeviceUtils.getStatusBarHeight();
    }

    abstract View inflate(Context context,ViewGroup parent);

    abstract void initView(View root);


    @Override
    public abstract void refreshView(Weather object,Object... objects);
}
