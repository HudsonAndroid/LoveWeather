package com.hudson.loveweather.ui.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Hudson on 2017/12/3.
 * 本来想做成能够里面外面都能响应滑动的，但是发现问题，
 * 问题：onIntercept与onTouchListener哪个更先执行？
 */

public class HorizontalTouchEventLinearLayout extends LinearLayout {
    public HorizontalTouchEventLinearLayout(Context context) {
        super(context);
    }

    public HorizontalTouchEventLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalTouchEventLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int mLastX;
    //用于管理本父View和其子View的事件分发
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mMaxScroll==0){
            mMaxScroll = getChildAt(getChildCount()-1).getWidth();
        }
        boolean intercept = true;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) ev.getX();
                return false;//第一次不能拦截，否则后面不会再进入这个方法了
            case MotionEvent.ACTION_MOVE:
                //如果scrollX是我们的最后一个控件（也就是mDelete控件）的宽度并且继续向左滑动，那么我们将事件交给子view执行
                if(getScrollX()==mMaxScroll&&(ev.getX() - mLastX)<0){
                    intercept = false;
                }
                break;
            default:
                break;
        }
        return intercept;
    }

    private int mMaxScroll;



    //    private float mLastX,mLastY;
//    /**
//     * 结合requestDisallowInterceptTouchEvent方法来管理本View及其父View的事件分发
//     * 在不同的情况下禁用父View的拦截事件
//     * @param ev
//     * @return
//     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        LogUtils.e("事件分发了");
//        getParent().requestDisallowInterceptTouchEvent(true);//先禁用父View拦截
//        //后面判断是否拦截
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                mLastX =  ev.getX();
//                mLastY =  ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if(Math.abs((ev.getX() - mLastX))<Math.abs((ev.getY() - mLastY))){
//                    LogUtils.e("上下滑动，父View拦截吧");
//                    //上下滑动，允许拦截
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }else{
//                    LogUtils.e("左右滑动，交给我吧");
//                    //左右滑动，交给我处理
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }
}
