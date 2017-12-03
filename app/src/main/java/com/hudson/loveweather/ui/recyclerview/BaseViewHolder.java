package com.hudson.loveweather.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Hudson on 2017/12/2.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void refreshView(T data,Object... objects);
}
