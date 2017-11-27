package com.hudson.loveweather.utils.log.parser;

import com.hudson.loveweather.utils.log.Constants;

/**
 * 格式化对象
 */
public interface Parser<T> {

    String LINE_SEPARATOR = Constants.BR;

    Class<T> parseClassType();

    String parseString(T t);
}
