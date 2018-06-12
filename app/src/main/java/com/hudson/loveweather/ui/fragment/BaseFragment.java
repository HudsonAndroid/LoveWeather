package com.hudson.loveweather.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hudson.loveweather.ui.activity.SearchActivity;

/**
 * Created by Hudson on 2017/12/1.
 */

public abstract class BaseFragment extends Fragment {
    protected SearchActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (SearchActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //逍遥模拟器上没有执行onAttachs方法,这个就很奇葩了
        mActivity = (SearchActivity) getActivity();
        initData();
    }

    public abstract View initView(LayoutInflater inflater);
    public abstract void initData();
}
