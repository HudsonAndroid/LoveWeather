package com.hudson.loveweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.hudson.loveweather.global.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(4000, TimeUnit.SECONDS).build();

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

    /**
     * 初始化每日一句的语录
     */
    public static void initializeDailyWords(){
        final SharedPreferenceUtils instance = SharedPreferenceUtils.getInstance();
        if(instance.getDailyWordsUpdateDate() != TimeUtils.getDayNumberOfDate()){
            HttpUtils.requestNetData(Constants.DAILY_WORD_URL, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String words = response.body().string();
                    if(!TextUtils.isEmpty(words)&&words.length()>15){
                        instance.saveDailyWords(words);
                        instance.saveDailyWordsUpdateDate(TimeUtils.getDayNumberOfDate());
                    }else{//不符合要求，重新获取
                        initializeDailyWords();
                    }
                }
            });
        }
    }
}
