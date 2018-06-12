package com.hudson.loveweather.ui.dialog.params;

/**
 * Created by Hudson on 2017/12/11.
 */

public class ChoiceParams implements Params {

    public String msg;
    public ParamsRunnable sureRunnable,cancelRunnable;

    public ChoiceParams(String msg, ParamsRunnable sureRunnable, ParamsRunnable cancelRunnable) {
        this.msg = msg;
        this.sureRunnable = sureRunnable;
        this.cancelRunnable = cancelRunnable;
    }
}
