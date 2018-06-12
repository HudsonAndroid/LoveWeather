package com.hudson.loveweather.utils;

import android.widget.Toast;

/**
 * Created by Hudson on 2017/3/19.
 * 让整体只有一个Toast
 */

public class ToastUtils {

    private static Toast sToast = Toast.makeText(UIUtils.getContext(),"",Toast.LENGTH_SHORT);

    public static void showToast(String info){
        sToast.setText(info);
        sToast.show();
    }

    public static void showToast(int strId){
        sToast.setText(UIUtils.getString(strId));
        sToast.show();
    }

}
