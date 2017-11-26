package com.hudson.loveweather.utils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Hudson on 2017/11/26.
 * 网络请求工具
 */

public class HttpUtils {

    /**
     * 网络请求
     * @param url
     * @param callback 结果回调
     */
    public static void requestNetData(String url, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        //enqueue自动开启子线程（异步）
        okHttpClient.newCall(new Request.Builder().url(url).build()).enqueue(callback);
    }
}
