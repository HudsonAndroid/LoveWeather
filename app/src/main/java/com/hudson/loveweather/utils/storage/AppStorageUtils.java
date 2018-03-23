package com.hudson.loveweather.utils.storage;

import android.content.Intent;

import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.service.ScheduledTaskService;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.ToastUtils;
import com.hudson.loveweather.utils.UIUtils;

import java.io.File;
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
     * 获取图片缓存目录
     * @return
     */
    public static String getPicCachePath(){
        return sAppStorage.getCustomPath(getCachePath() + "/pictures");
    }

    /**
     * 获取用户自定义背景缓存目录
     * @return
     */
    public static String getCustomPicCachePath(){
        return sAppStorage.getCustomPath(getPicCachePath() + "/customBg");
    }

    /**
     * 获取应用奔溃日志信息
     * @return
     */
    public static String getCaughtLogPath(){
        return sAppStorage.getCustomPath(getAppRootPath()+"/log");
    }

    /**
     * 获取应用城市数据库加载失败的列表文件
     * @return 列表文件的路径
     */
    public static String getCityDataLoadFailedFilePath(){
        return sAppStorage.getCustomPath(getCachePath()+"/load")+"/loadFailedList.txt";
    }

    public static String getAppBackgroundPicFilePath(int picIndex){
        SharedPreferenceUtils instance = SharedPreferenceUtils.getInstance();
        String backgroundPicCategory = instance.getBackgroundPicCategory();
        StringBuilder sb = new StringBuilder();
        int referCount = Constants.PIC_CACHE_COUNT;
        if(backgroundPicCategory.equals(Constants.CUSTOM_CATEGORY)){
            String customPicCachePath = getCustomPicCachePath();
            referCount = new File(customPicCachePath).listFiles().length;
            if(referCount>0){
                sb.append(customPicCachePath);
            }else{//文件夹中没有图片
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast("错误，自定义图片不存在！");
                    }
                });
                //还原原始使用
                instance.saveBackgroundPicCategory(Constants.PIC_CATEGORY[0]);
                Intent intent = new Intent(UIUtils.getContext(), ScheduledTaskService.class);
                intent.putExtra("type",ScheduledTaskService.TYPE_CHANGE_BACKGROUND_CATEGORY);
                UIUtils.getContext().startService(intent);
                sb.append(getPicCachePath());
                referCount = Constants.PIC_CACHE_COUNT;
            }
        }else{
            sb.append(getPicCachePath());
        }
        return sb.append("/").append(Constants.PIC_CACHE_NAME)
                .append((picIndex % referCount))
                .append(".jpg").toString();
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
