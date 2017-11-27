package com.hudson.loveweather.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Hudson on 2017/11/27.
 */

public class IOUtils {

    public static void close(Closeable io){
        if(io!=null){
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
