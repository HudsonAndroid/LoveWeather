package com.hudson.loveweather.utils.log;

import com.hudson.loveweather.R;
import com.hudson.loveweather.utils.log.parser.BundleParse;
import com.hudson.loveweather.utils.log.parser.CollectionParse;
import com.hudson.loveweather.utils.log.parser.IntentParse;
import com.hudson.loveweather.utils.log.parser.MapParse;
import com.hudson.loveweather.utils.log.parser.MessageParse;
import com.hudson.loveweather.utils.log.parser.Parser;
import com.hudson.loveweather.utils.log.parser.ReferenceParse;
import com.hudson.loveweather.utils.log.parser.ThrowableParse;
import com.hudson.loveweather.utils.UIUtils;

/**
 * Created by Hudson on 2017/11/25.
 */

public final class Constants {
    //日志级别
    public static final int TYPE_VERBOSE = 1;
    public static final int TYPE_DEBUG = 2;
    public static final int TYPE_INFO = 3;
    public static final int TYPE_WARN = 4;
    public static final int TYPE_ERROR = 5;

    static final String DEFAULT_TAG = UIUtils.getContext().getString(R.string.app_name);

    static final String STRING_OBJECT_NULL = "Object[object is null]";
    // 默认支持解析库
    public static final Class<? extends Parser>[] DEFAULT_PARSE_CLASS = new Class[]{
            BundleParse.class, IntentParse.class, CollectionParse.class,
            MapParse.class, ThrowableParse.class, ReferenceParse.class, MessageParse.class
    };

    // 解析属性最大层级
    public static final int MAX_CHILD_LEVEL = 2;

    // 换行符
    public static final String BR = System.getProperty("line.separator");

    // 空格
    public static final String SPACE = "\t";


}
