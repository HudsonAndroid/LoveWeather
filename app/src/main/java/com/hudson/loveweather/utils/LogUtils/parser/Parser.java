package com.hudson.loveweather.utils.LogUtils.parser;

import com.hudson.loveweather.utils.LogUtils.Constants;

/**
 * 格式化对象
 */
public interface Parser<T> {

    String LINE_SEPARATOR = Constants.BR;

    Class<T> parseClassType();

    String parseString(T t);
}
