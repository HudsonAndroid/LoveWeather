package com.hudson.loveweather.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.hudson.loveweather.utils.DataBaseLoader;
import com.hudson.loveweather.utils.LocalDatabaseLoader;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.log.LogUtils;

/**
 * Created by Hudson on 2017/11/26.
 * 后台初始化数据库
 */

public class DataInitializeService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public DataInitializeService() {
        super("LoadDataThread");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(LocalDatabaseLoader.copyAssetsDatabaseToApp()){
            //加载本地数据库，直接成功
            SharedPreferenceUtils.getInstance().saveLocalDatabaseFlag(true);
            LogUtils.e("数据库复制成功！");
        }else{//如果本地数据库复制失败，那么访问网络请求数据库
            DataBaseLoader.loadDatabaseToLocal(); //网络访问方式容易丢失数据,所以只有本地的无法使用了，才访问
        }
    }

}
