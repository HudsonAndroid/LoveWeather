package com.hudson.loveweather.utils.log;

import android.text.TextUtils;

/**
 * Created by Hudson on 2017/11/25.
 */

public class LogUtils {
    private static LogConfig sLogConfig = LogConfig.getInstance();
    private static Printer sPrinter = Printer.getInstance();

    public static LogConfig getConfig(){
        return sLogConfig;
    }

    /**
     * 以使用处的类作为tag，最终tag是appName+类名；如果没有设置使用appName
     * @param cls
     */
    public static void setTag(Class<?> cls){
        if(cls != null){
            String className = cls.getName();
            String tag = new StringBuilder(sLogConfig.getTag())
                    .append("&")
                    .append(className.substring(className.lastIndexOf(".")+1))
                    .toString();
            if(!TextUtils.isEmpty(tag)){
                sPrinter.setTag(tag);
            }
        }
    }

    /**
     * 自定义tag
     * @param customTag tag
     */
    public static void setTag(String customTag){
        if(!TextUtils.isEmpty(customTag)){
            sPrinter.setTag(customTag);
        }
    }

    /**
     * 打印json
     * 日志级别是您配置的日志级别，如果没有配置，那么就是默认的日志级别
     * @param json json字符串
     */
    public static void json(String json) {
        sPrinter.json(json);
    }

    /**
     * 输出xml
     * 日志级别是您配置的日志级别，如果没有配置，那么就是默认的日志级别
     * @param xml xml字符串
     */
    public static void xml(String xml) {
        sPrinter.xml(xml);
    }

    /**
     * verbose输出
     *
     * @param additionalInfo
     * @param args
     */
    public static void v(String additionalInfo, Object... args) {
        sPrinter.v(additionalInfo, args);
    }

    public static void v(Object object) {
        sPrinter.v(object);
    }


    /**
     * debug输出
     *
     * @param additionalInfo
     * @param args
     */
    public static void d(String additionalInfo, Object... args) {
        sPrinter.d(additionalInfo, args);
    }

    public static void d(Object object) {
        sPrinter.d(object);
    }

    /**
     * info输出
     *
     * @param additionalInfo
     * @param args
     */
    public static void i(String additionalInfo, Object... args) {
        sPrinter.i(additionalInfo, args);
    }

    public static void i(Object object) {
        sPrinter.i(object);
    }

    /**
     * warn输出
     *
     * @param additionalInfo
     * @param args
     */
    public static void w(String additionalInfo, Object... args) {
        sPrinter.w(additionalInfo, args);
    }

    public static void w(Object object) {
        sPrinter.w(object);
    }

    /**
     * error输出
     *
     * @param additionalInfo
     * @param args
     */
    public static void e(String additionalInfo, Object... args) {
        sPrinter.e(additionalInfo, args);
    }

    public static void e(Object object) {
        sPrinter.e(object);
    }

    public static void log(String additionalInfo, Object... args){
        sPrinter.log(additionalInfo,args);
    }

    public static void log(Object object){
        sPrinter.log(object);
    }

}
