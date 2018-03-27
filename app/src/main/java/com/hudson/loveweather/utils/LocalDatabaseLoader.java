package com.hudson.loveweather.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Hudson on 2018/3/23 0023.
 * 由于网络城市数据库需要大量的访问网络去请求数据，所以提供一个本地数据库进行操作
 *  将本地数据库（assets目录下）复制到应用的databases目录中使用即可
 *
 *  注意：外界的数据库文件在创建的时候，使用的bean实例必须与我们访问的时候所使用的bean实例一样
 */

public class LocalDatabaseLoader {

    /**
     * 拷贝assets目录下的数据库到应用数据库目录下
     * @return
     */
    public static boolean copyAssetsDatabaseToApp(){
        //1.首先判断该数据库文件在/data/data/package/databases目录下是否存在
        Context context = UIUtils.getContext();
        String DATABASE_PATH = "/data/data/"+ context.getPackageName()+"/databases/";
        String DATABASE_NAME = "love_weather.db";
        if(!new File(DATABASE_PATH+DATABASE_NAME).exists()){
            //如果文件不存在，再判断目录databases是否存在
            File file = new File(DATABASE_PATH);
            if(!file.exists()){
                file.mkdir();
            }
        }
        //2.开始复制
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = context.getAssets().open(DATABASE_NAME);
            fileOutputStream = new FileOutputStream(DATABASE_PATH+DATABASE_NAME);
            byte[] buffer = new byte[1024];
            int length;
            while((length = inputStream.read(buffer))>0){
                fileOutputStream.write(buffer,0,length);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            IOUtils.close(inputStream);
            IOUtils.close(fileOutputStream);
        }
        return true;
    }
}
