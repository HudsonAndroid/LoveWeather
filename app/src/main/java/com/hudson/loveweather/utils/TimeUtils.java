package com.hudson.loveweather.utils;

import java.util.Calendar;

/**
 * Created by Hudson on 2017/11/26.
 */

public class TimeUtils {

    /**
     * 获取几号
     * @return
     */
    public static String getDayNumberOfDate(){
        return String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }
}
