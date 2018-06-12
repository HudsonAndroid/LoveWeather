package com.hudson.loveweather.ui.view.weatherpage;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Hudson on 2017/11/28.
 */

public interface WeatherPageViewHelper<T> {

    View inflateView(Activity context, ViewGroup parent, Object... objects);

    void refreshView(T object,Object... objects);
}
