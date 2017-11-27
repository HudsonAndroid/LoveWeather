package com.hudson.loveweather.utils.storage.internal;

/**
 * Created by Hudson on 2017/11/27.
 */

public interface Storage {

    String getSdAbsolutePath();
    String getInternalAbsolutePath();
    String getInternalAppRootPath();
    String getExternalAppRootPath();

    /**
     * 获取android系统内部存储缓存路径，
     * 一般是Context.getCacheDir();  /data/data/app_package_name/cache
     * @return
     */
    String getInternalCacheAbsolutePath();

    /**
     * 获取android系统原生外部存储缓存路径
     * 一般是/storage/emulated/0/Android/data/app_package_name/cache
     * Context.getExternalCacheDir();
     * @return
     */
    String getExternalCacheAbsolutePath();

}
