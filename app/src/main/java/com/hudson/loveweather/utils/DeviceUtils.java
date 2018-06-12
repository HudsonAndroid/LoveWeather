package com.hudson.loveweather.utils;

import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.os.Vibrator;
import android.util.DisplayMetrics;

/**
 * Created by Hudson on 2017/11/27.
 */

public class DeviceUtils {

    public static void getScreen(int[] pixels){
        DisplayMetrics dm = UIUtils.getContext().getResources().getDisplayMetrics();
        pixels[0] = dm.widthPixels;
        pixels[1] = dm.heightPixels;
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(
                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }

    /**
     * 手机振动
     * @param context
     * @param milliseconds 振动时长
     */
    public static void Vibrate(final Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context
                .getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }
}
