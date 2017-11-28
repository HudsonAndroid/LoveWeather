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

    public static String getDayWeekOfDate(){
        int dayNo = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);//获取是一个星期的第几天
        switch (dayNo){
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
            default:
                break;
        }
        return "Monday";
    }
    
    public static String getMonthOfYear(){
        int monthNo = Calendar.getInstance().get(Calendar.MONTH);
        switch (monthNo){
            case Calendar.JANUARY:
                return "Jan.";
            case Calendar.FEBRUARY:
                return "Feb.";
            case Calendar.MARCH:
                return "Mar.";
            case Calendar.APRIL:
                return "Apr.";
            case Calendar.MAY:
                return "May.";
            case Calendar.JUNE:
                return "June.";
            case Calendar.JULY:
                return "July.";
            case Calendar.AUGUST:
                return "Aug.";
            case Calendar.SEPTEMBER:
                return "Sept.";
            case Calendar.OCTOBER:
                return "Oct.";
            case Calendar.NOVEMBER:
                return "Nov.";
            case Calendar.DECEMBER:
                return "Dec.";
        }
        return "Jan.";
    }
}
