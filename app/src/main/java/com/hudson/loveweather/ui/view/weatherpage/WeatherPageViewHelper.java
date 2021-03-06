package com.hudson.loveweather.ui.view.weatherpage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Hudson on 2017/11/28.
 */

public interface WeatherPageViewHelper<T> {

    View inflateView(Context context,ViewGroup parent,Object... objects);

    void refreshView(T object,Object... objects);
}
