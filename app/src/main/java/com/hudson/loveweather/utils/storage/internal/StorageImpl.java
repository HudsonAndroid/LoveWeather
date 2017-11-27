package com.hudson.loveweather.utils.storage.internal;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.hudson.loveweather.utils.IOUtils;
import com.hudson.loveweather.utils.UIUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Hudson on 2017/11/27.
 * 这里封装了基本的一些数据访问的操作，其他由子类去拓展
 */

public abstract class StorageImpl implements Storage {


    String SD_ABSOLUTE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    String INTERNAL_ABSOLUTE_PATH = Environment.getDataDirectory().getAbsolutePath();

    @Override
    public String getSdAbsolutePath() {
        return SD_ABSOLUTE_PATH;
    }

    @Override
    public String getInternalAbsolutePath() {
        return INTERNAL_ABSOLUTE_PATH;
    }

    @Override
    public String getInternalAppRootPath() {
        return getInternalAbsolutePath();
    }

    @Override
    public abstract String getExternalAppRootPath();

    @Override
    public String getInternalCacheAbsolutePath() {
        return UIUtils.getContext().getCacheDir().getAbsolutePath();
    }

    @Override
    public @Nullable String getExternalCacheAbsolutePath()throws NullPointerException{
        return UIUtils.getContext().getExternalCacheDir().getAbsolutePath();
    }

    /**
     * 检测sd卡是否可用
     *
     * @return
     */
    public boolean isSdCardAvailable() {
        //检测SD卡是否可用,注意是equals，因为这个是字符串
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    public synchronized boolean writeFile(String path,String data){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(path));
            fos.write(data.getBytes());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            IOUtils.close(fos);
        }
        return false;
    }

    public synchronized String readFile(String path){
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fis = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(new File(path));
            br = new BufferedReader(new InputStreamReader(fis));
            String lineString;
            while ((lineString = br.readLine()) != null) {
                stringBuilder.append(lineString).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            IOUtils.close(br);
            IOUtils.close(fis);
        }
        return stringBuilder.toString();
    }

    public String readSdCardFile(String path){
        if(isSdCardAvailable()) {
            return readFile(path);
        }
        return null;
    }

    public boolean writeSdCardFile(String path,String data){
        if(isSdCardAvailable()) {
            writeFile(path,data);
        }
        return false;
    }

}
