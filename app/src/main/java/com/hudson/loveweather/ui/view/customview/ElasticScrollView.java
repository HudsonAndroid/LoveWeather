package com.hudson.loveweather.ui.view.customview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * ElasticScrollView有弹性的ScrollView
 */
public class ElasticScrollView extends ScrollView {

    private View inner;
    private float mLastY;
    private Rect normal = new Rect();;
    private int mOringalTop;
    private int mOringalBottom;
    private boolean isDragUp = false;
    private boolean isDragDown = false;

    public ElasticScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setVerticalFadingEdgeEnabled(false);
        this.setVerticalScrollBarEnabled(false);
    }

    public ElasticScrollView(Context context) {
        super(context);
        this.setVerticalFadingEdgeEnabled(false);
        this.setVerticalScrollBarEnabled(false);
    }

    public ElasticScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setVerticalFadingEdgeEnabled(false);
        this.setVerticalScrollBarEnabled(false);
    }

    private float mLastX;
    //我们虽然重写了scrollView内部的控件的dispatchTouchEvent方法，并结合
    //requestDisallowInterceptTouchEvent来告诉scrollView是否该拦截事件
    //但是，scrollView比较老实，发现scrollView并没有拦截事件，所以还得这里处理
    //我们需要把子view方式的注释掉
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //注意啦，注意啦，这个一定要写上，不然scrollView无法正常滑动，
        // 并会出现错误是ScrollView: Invalid pointerId=-1 in onTouchEvent
        // 就这个问题，搞了我多长时间，我一度怀疑scrollView不是通过scrollY来实现滑动的！！
        // 坑爹啊，抓狂，抓狂!!!
        super.onInterceptTouchEvent(ev);
        boolean intercept = false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX = ev.getX();
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(Math.abs(ev.getX() - mLastX)<Math.abs(ev.getY() - mLastY)){
                    intercept = true;
                }
                break;
        }
        return intercept;
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
            mOringalTop = inner.getTop();
            mOringalBottom = inner.getBottom();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (getChildCount() > 0) {
            inner = getChildAt(0);
            mOringalTop = inner.getTop();
            mOringalBottom = inner.getBottom();
        }
        if (inner == null) {
            return super.onTouchEvent(ev);
        }
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                isDragDown = false;
                isDragUp = false;
//			Log.d("debug", "y=" + mLastY);
                break;
            case MotionEvent.ACTION_UP:
                if (!normal.isEmpty()) {
                    animation();
                }
                isDragDown = false;
                isDragUp = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float nowY = ev.getY();
                int deltaY = (int) (nowY - mLastY);
//			Log.d("debug", "mLastY=" + mLastY + ",nowY=" + nowY);
                if (mLastY==0){
                    mLastY=nowY;
                    return true;
                }
                mLastY=nowY;
//			Log.d("debug", "this.getScrollY()=" + getScrollY() + ",deltaY=" + deltaY);
                if (isDragUp || isDragDown) {
                    move(deltaY);
                    return true;
                }
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (getScrollY() == 0 && deltaY > 0) {
                    isDragUp = true;
                    move(deltaY);
                    return true;
                }
//			Log.d("debug", "this.getScrollY()=" + this.getScrollY() + ",this.getHeight()=" + this.getHeight() + ",computeVerticalScrollRange()="+ computeVerticalScrollRange());
                if (getScrollY() + getHeight() >= computeVerticalScrollRange() && deltaY < 0) {
//				Log.d("debug", "move down");
                    isDragDown = true;
                    move(deltaY);
                    return true;
                }

                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void move(int deltaY) {
//		Log.d("debug", "inner.top=" + inner.getTop() + ",inner.bottom=" + inner.getBottom());
        if (normal.isEmpty()) {
            // 保存正常的布局位置
            normal.set(inner.getLeft(), mOringalTop, inner.getRight(), mOringalBottom);
        }
        // 移动布局
        inner.layout(inner.getLeft(), mOringalTop + deltaY / 2, inner.getRight(), mOringalBottom + deltaY / 2);
    }

    // 开启动画移动

    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(), normal.top);
        DecelerateInterpolator dd = new DecelerateInterpolator();
        ta.setInterpolator(dd);
        ta.setDuration(500);
        inner.startAnimation(ta);
        // 设置回到正常的布局位置
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

}