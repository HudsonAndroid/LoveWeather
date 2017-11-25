package com.hudson.loveweather.utils.LogUtils;

import com.hudson.loveweather.R;
import com.hudson.loveweather.utils.UIUtils;

/**
 * Created by Hudson on 2017/11/25.
 */

public class Constants {
    //日志级别
     static final int TYPE_VERBOSE = 1;
     static final int TYPE_DEBUG = 2;
     static final int TYPE_INFO = 3;
     static final int TYPE_WARN = 4;
     static final int TYPE_ERROR = 5;

     static final String DEFAULT_TAG = UIUtils.getContext().getString(R.string.app_name);
}
