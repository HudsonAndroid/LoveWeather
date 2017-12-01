package com.hudson.loveweather.utils.update;

import android.content.Intent;

import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.utils.HttpUtils;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.UIUtils;
import com.hudson.loveweather.utils.storage.AppStorageUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Hudson on 2017/11/27.
 * 外界不能直接使用本类，需要通过UpdateUtils操作
 * 因而本类不是public,而是包内可以访问
 */

 class BackgroundPicUpdater implements Updater {

    @Override
    public void update(String url, Object... objects) {
        SharedPreferenceUtils instance = SharedPreferenceUtils.getInstance();
        if(instance.getShouldUpdatePic()){
            boolean onlyWifiAccessNetUpdatePic = instance
                    .getOnlyWifiAccessNetUpdatePic();
            String path = new StringBuilder(AppStorageUtils.getPicCachePath())
                    .append("/").append(Constants.PIC_CACHE_NAME)
                    .append(((int)objects[0]%Constants.PIC_CACHE_COUNT))
                    .append(".jpg").toString();
            if((HttpUtils.isNetworkAvailable()&&!onlyWifiAccessNetUpdatePic)
                    ||(HttpUtils.isWifi()&&onlyWifiAccessNetUpdatePic)){
                accessNetworkToUpdate(url,path);
            }else{//如果网络更新不可用，则直接使用本地缓存中的数据
                localUpdate(path);
            }
        }
    }


    private void accessNetworkToUpdate(String url, final String localPath){
        HttpUtils.requestNetData(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(AppStorageUtils.writeFile(localPath,response.body().byteStream())){
                    notifyUpdatePic(localPath);
                }
            }
        });
    }

    private void localUpdate(String path){
        if(new File(path).exists()){
            notifyUpdatePic(path);
        }
    }

    private void notifyUpdatePic(String localPath) {
        Intent intent = new Intent(Constants.BROADCAST_UPDATE_PIC);
        intent.putExtra("path",localPath);
        UIUtils.getContext().sendBroadcast(intent);
    }
}
