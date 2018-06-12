package com.hudson.loveweather.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.hudson.loveweather.service.ScheduledTaskService;
import com.hudson.loveweather.service.WidgetUpdateService;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Hudson on 2017/12/4.
 */

public class AlarmClockUtils {

    /**
     *
     * @param context
     * @param triggerOffset
     * @param intent
     * @param requestCode 用于区分PendingIntent，如果requestCode相同，FLAG_UPDATE_CURRENT会覆盖消息
     */
    public static void scheduleTask(Context context,long triggerOffset, Intent intent,int requestCode){
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        long triggerAtTime = System.currentTimeMillis() + triggerOffset;
        PendingIntent pendingIntent = PendingIntent.getService(context,requestCode,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP,triggerAtTime,pendingIntent);
    }


    public static void scheduleTask(int taskType,long triggerOffset){
        Context context = UIUtils.getContext();
        Intent intent = new Intent(context,ScheduledTaskService.class);
        intent.putExtra("type",taskType);
        scheduleTask(context,triggerOffset,intent,taskType);
    }

    public static void scheduleTimeUpdateTask(long triggerOffset){
        Context context = UIUtils.getContext();
        Intent intent = new Intent(context,WidgetUpdateService.class);
        intent.putExtra("type", WidgetUpdateService.TYPE_UPDATE_TIME);
        scheduleExactlyTask(context,triggerOffset,intent,WidgetUpdateService.TYPE_UPDATE_TIME);
    }

    public static void scheduleExactlyTask(Context context,long triggerOffset, Intent intent,int requestCode){
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        long triggerAtTime = System.currentTimeMillis() + triggerOffset;
        PendingIntent pendingIntent = PendingIntent.getService(context,requestCode,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setExact(AlarmManager.RTC_WAKEUP,triggerAtTime,pendingIntent);
    }
}
