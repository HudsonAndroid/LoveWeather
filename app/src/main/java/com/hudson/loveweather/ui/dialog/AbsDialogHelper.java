package com.hudson.loveweather.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hudson.loveweather.R;

/**
 * Created by Hudson on 2017/12/10.
 * 代理模式
 */

public abstract class AbsDialogHelper<T> {
    protected AlertDialog.Builder mBuilder;
    protected AlertDialog mDialog;
    protected LinearLayout mDialogContainer;

    public AbsDialogHelper(Context context, T params){
        mBuilder = new AlertDialog.Builder(context);
        mDialog = mBuilder.create();
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_base_layout,null,false);
        mDialogContainer = (LinearLayout) root.findViewById(R.id.ll_dialog_container);
        mDialogContainer.addView(initContentView(context,mDialogContainer,params));
        mDialog.setView(root,0,0,0,0);
    }

    public void setCancelable(boolean cancelable){
        mDialog.setCancelable(cancelable);
    }

    /**
     * 初始化对话框的内容
     * 注意：在子类实现本方法时，不要使用true的方式添加到parent中
     * @param context
     * @param parent
     * @param params
     * @return
     */
    protected abstract View initContentView(Context context, ViewGroup parent,T params);

    public void show(){
        mDialog.show();
    }

    public void dismiss(){
        mDialog.dismiss();
    }

}
