package com.hudson.loveweather.utils;

import com.hudson.loveweather.global.Constants;

/**
 * Created by Hudson on 2017/12/13.
 */

public class StringUtils {

    /**
     * 解析文件名中的数字
     * loveWeatherx.jpg 解析出x
     * @param fileName
     * @return
     */
    public static int decodeFileNameNumber(String fileName){
        try{
            return Integer.valueOf(fileName.replace(Constants.PIC_CACHE_NAME,"").replace(".jpg",""));
        }catch (Exception e){
            return -1;
        }
    }
}
