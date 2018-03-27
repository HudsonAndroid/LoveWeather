package com.hudson.loveweather.ui.widget;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.hudson.loveweather.service.WidgetUpdateService;
import com.hudson.loveweather.utils.SharedPreferenceUtils;

/**
 * Created by Hudson on 2017/12/6.
 */

public class WidgetDefaultProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, WidgetUpdateService.class));
        super.onReceive(context, intent);
    }
//
//
//    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
//    }



    @Override
    public void onEnabled(Context context) {
        context.startService(new Intent(context, WidgetUpdateService.class));
        SharedPreferenceUtils.getInstance().saveDefaultWidgetExist(true);
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        context.stopService(new Intent(context, WidgetUpdateService.class));
        SharedPreferenceUtils.getInstance().saveDefaultWidgetExist(false);
        super.onDisabled(context);
    }
}
