package com.hudson.loveweather.ui.dialog.params;

/**
 * Created by Hudson on 2017/12/11.
 */

public class InformationParams implements Params {

    public String msg;
    public ParamsRunnable sureRunnable,cancelRunnable;

    public InformationParams(String msg, ParamsRunnable sureRunnable, ParamsRunnable cancelRunnable) {
        this.msg = msg;
        this.sureRunnable = sureRunnable;
        this.cancelRunnable = cancelRunnable;
    }
}
