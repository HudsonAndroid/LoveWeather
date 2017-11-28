package com.hudson.loveweather.utils.update;

/**
 * Created by Hudson on 2017/11/27.
 */

interface Updater {

    /**
     * 刷新
     * @param url 网络url
     * @param objects 额外参数
     */
    void update(String url,Object... objects);
}
