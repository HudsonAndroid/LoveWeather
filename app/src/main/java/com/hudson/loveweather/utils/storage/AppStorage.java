package com.hudson.loveweather.utils.storage;

import com.hudson.loveweather.R;
import com.hudson.loveweather.utils.storage.internal.StorageImpl;
import com.hudson.loveweather.utils.UIUtils;

import java.io.File;

/**
 * Created by Hudson on 2017/11/27.
 * @hide
 */
public class AppStorage extends StorageImpl{

    static class StorageHelper{
        static final AppStorage sInstance = new AppStorage();
    }

    public static AppStorage getInstance(){
        return StorageHelper.sInstance;
    }

    @Override
    public String getExternalAppRootPath() {
        return mkDirs(getSdAbsolutePath() + "/" + UIUtils.getString(R.string.app_english_name));
    }

    public String mkDirs(String path){
        File file = new File(path);
        if(!file.exists()){
            if(!file.mkdirs()){
                return null;
            }
        }
        return path;
    }

    /**
     * 有三种选择：
     * 1）外部存储系统默认缓存路径
     * 2）外部存储，在appRootPath中自定义缓存目录（系统清理缓存时不会被清除）
     * 3）内部存储系统默认缓存路径
     * 这里选择方案二
     * @return
     */
    public String getCacheAbsolutePath(){
        return mkDirs(getExternalAppRootPath() + "/" + ".cache");
    }

    public String getPicDownloadAbsolutePath(){
        return mkDirs(getExternalAppRootPath() + "/" + "pic");
    }
}
