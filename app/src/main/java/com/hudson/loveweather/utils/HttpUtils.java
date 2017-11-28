package com.hudson.loveweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Hudson on 2017/11/26.
 * 网络工具
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

    /**
     * 判断当前网络是否是wifi且可用网络
     *
     * @return
     */
    public static boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) UIUtils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null&& activeNetInfo.isConnected()
                &&activeNetInfo.getState() == NetworkInfo.State.CONNECTED
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态 是否可用
     *
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) UIUtils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
