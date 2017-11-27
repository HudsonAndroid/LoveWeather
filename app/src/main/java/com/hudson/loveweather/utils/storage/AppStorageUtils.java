package com.hudson.loveweather.utils.storage;

import java.io.InputStream;

/**
 * Created by Hudson on 2017/11/27.
 * 开放给用户使用的类
 */

public class AppStorageUtils  {
    private static AppStorage sAppStorage = AppStorage.getInstance();

    /**
     * 获取应用sd卡的根目录
     * @return
     */
    public static String getAppRootPath(){
        return sAppStorage.getExternalAppRootPath();
    }

    /**
     * 获取应用缓存路径
     * @return
     */
    public static String getCachePath(){
        return sAppStorage.getCacheAbsolutePath();
    }

    /**
     * 获取应用图片路径
     * @return
     */
    public static String getPicPath(){
        return sAppStorage.getPicDownloadAbsolutePath();
    }

    /**
     * 获取sd存储路径
     * @return
     */
    public static String getSdCardPath(){
        return sAppStorage.getSdAbsolutePath();
    }

    /**
     * 获取内部存储路径
     * @return
     */
    public static String getDataPath(){
        return sAppStorage.getInternalAbsolutePath();
    }

    public static String readFile(String path){
        return sAppStorage.readFile(path);
    }

    public static boolean writeFile(String path,String data){
        return sAppStorage.writeFile(path,data);
    }

    public static boolean writeFile(String path, InputStream inputStream){
        return sAppStorage.writeFile(path,inputStream);
    }

    public static String readSd(String path){
        return sAppStorage.readSdCardFile(path);
    }

    public static boolean writeSd(String path,String data){
        return sAppStorage.writeSdCardFile(path,data);
    }

}
