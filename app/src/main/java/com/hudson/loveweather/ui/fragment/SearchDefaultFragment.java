package com.hudson.loveweather.ui.fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.ui.view.customview.AutoAdapterLayout;
import com.hudson.loveweather.utils.ToastUtils;
import com.hudson.loveweather.utils.UIUtils;

/**
 * Created by Hudson on 2017/12/1.
 */

public class SearchDefaultFragment extends BaseFragment implements View.OnClickListener {
    private TextView mLocationText;
    private AutoAdapterLayout mHistoryContainer,mHotCityContainer;

    @Override
    public View initView(LayoutInflater inflater) {
        View root = inflater.inflate(R.layout.fragment_search_default, null);
        mLocationText = (TextView) root.findViewById(R.id.tv_current_location);
        root.findViewById(R.id.ll_current_locate).setOnClickListener(this);
        mHistoryContainer = (AutoAdapterLayout) root.findViewById(R.id.aal_history_container);
        mHotCityContainer = (AutoAdapterLayout) root.findViewById(R.id.aal_hot_city_container);
        return root;
    }

    @Override
    public void initData() {
        TextView child = new TextView(getActivity());
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = 15;
        layoutParams.topMargin = 15;
        child.setText("厦门");
        child.setTextColor(UIUtils.getColor(R.color.white));
        child.setGravity(Gravity.CENTER);
        child.setBackgroundResource(R.drawable.selector_textview_city);
        mHistoryContainer.addView(child,layoutParams);

        child = new TextView(getActivity());
        layoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = 15;
        layoutParams.topMargin = 15;
        child.setText("上海");
        child.setTextColor(UIUtils.getColor(R.color.white));
        child.setGravity(Gravity.CENTER);
        child.setBackgroundResource(R.drawable.selector_textview_city);
        mHotCityContainer.addView(child,layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_current_locate:
                ToastUtils.showToast("重新定位");
                break;
//            case :
//
//                break;
//            case :
//
//                break;
            default:
                break;
        }
    }
}
