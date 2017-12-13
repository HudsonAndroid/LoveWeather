package com.hudson.loveweather.ui.dialog.params;

import java.util.List;

/**
 * Created by Hudson on 2017/12/11.
 */

public class SelectParams implements Params {

    public List<String> datas;
    public String preSelectedItem;
    public ParamsRunnable itemSelectedRunnable;

    public SelectParams(List<String> datas, String preSelectedItem, ParamsRunnable itemSelectedRunnable) {
        this.datas = datas;
        this.preSelectedItem = preSelectedItem;
        this.itemSelectedRunnable = itemSelectedRunnable;
    }
}
