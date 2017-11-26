package com.hudson.loveweather.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.hudson.loveweather.utils.DataBaseLoader;

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
        DataBaseLoader.loadDatabaseToLocal();
    }

}
