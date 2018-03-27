package com.hudson.loveweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hudson.loveweather.service.WidgetUpdateService;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.log.LogUtils;

/**
 * Created by Hudson on 2018/3/26 0026.
 * 为了避免长时间手机锁屏情况下，widget数据没有更新问题，在每次手机屏幕亮(用户使用)之后，尝试去更新数据
 */

public class WidgetUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!SharedPreferenceUtils.getInstance().isUserActive()){//用户就是在本应用上面操作的，所以不需要更新
            LogUtils.e("不在本应用上操作,用户使用了哦");
            context.startService(new Intent(context,WidgetUpdateService.class));
        }
    }
}
