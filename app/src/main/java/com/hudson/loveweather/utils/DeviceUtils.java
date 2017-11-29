package com.hudson.loveweather.utils;

import android.content.res.Resources;
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
     * 获取通知栏高度
     * @return
     */
    public static int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(
                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }
}
