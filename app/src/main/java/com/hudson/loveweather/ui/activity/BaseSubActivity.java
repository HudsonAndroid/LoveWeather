package com.hudson.loveweather.ui.activity;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.utils.BitmapUtils;

/**
 * Created by Hudson on 2017/12/1.
 */

public abstract class BaseSubActivity extends BaseActivity {
    public TextView mExtendTextView;

    @Override
    public final void setContentViewAndInit() {
        setContentView(R.layout.activity_sub);
    }

    @Override
    public final void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseSubActivity.this.finish();
            }
        });
        ((TextView) this.findViewById(R.id.tv_title)).setText(getActivityTitle());
        mExtendTextView = (TextView) this.findViewById(R.id.tv_extend);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ((LinearLayout) this.findViewById(R.id.ll_content_container)).addView(setContent(),
                layoutParams);
        findViewById(R.id.ll_root).setBackground(
                new BitmapDrawable(BitmapUtils.gaussianBlur(25,BitmapUtils.getShowPic(
                        new int[]{0xaa000000,0x77000000,0x77000000,0xaa000000},
                        new float[]{0.0f,0.4f,0.6f,1.0f}))));
        init();
    }

    /**
     * 子类需要通过本方法来添加内容
     * 最好通过View.inflate(context,layoutId,xx)来加载
     * @return 内容布局
     */
    public abstract View setContent();

    /**
     * 初始化操作
     */
    public abstract void init();

    /**
     * 获取activity的title
     * @return
     */
    public abstract String getActivityTitle();

    @Override
    public abstract void recycle();
}
