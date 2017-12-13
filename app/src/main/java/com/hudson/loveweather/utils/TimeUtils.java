package com.hudson.loveweather.utils;

import android.text.TextUtils;

import java.util.Calendar;

/**
 * Created by Hudson on 2017/11/26.
 */

public class TimeUtils {

    /**
     * 获取几号
     * warning: if you want set it to TextView,please convert to String firstly.
     * @return
     */
    public static int getDayNumberOfDate(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取几号，与上面不同的是，如果是1-9，那么结果会加0，例如01
     * @return
     */
    public static String getDayNumberStringOfDate(){
        int date = getDayNumberOfDate();
        if(date>0&&date<=9){
            return "0"+date;
        }
        return date+"";
    }

    public static int getHourOfDay(){
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinuteOfHour(){
        return Calendar.getInstance().get(Calendar.MINUTE);
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

    /**
     * 从字符串中提取时间的分钟数
     * 例如 20:51 表示20*60+51分钟
     * @param time 包含时间的字符串
     * @param startIndex 起始下标
     * @return 分钟数,-1表示格式不正确
     */
    public static int parseMinuteTime(String time, int startIndex){
        if(!TextUtils.isEmpty(time)){
            int index = time.indexOf(":",startIndex);
            if(index != -1){
                try{
                    int hour = Integer.valueOf(time.substring(index-2,index));
                    int minute = Integer.valueOf(time.substring(index +1,index+3));
                    return hour*60 + minute;
                }catch (Exception e){
                    e.printStackTrace();
                    return parseMinuteTime(time,index);
                }
            }
        }
        return -1;
    }

    public static int parseCurrentMinuteTime(){
        return getHourOfDay()*60 + getMinuteOfHour();
    }

}
