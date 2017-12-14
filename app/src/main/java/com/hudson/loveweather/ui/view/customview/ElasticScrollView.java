package com.hudson.loveweather.ui.view.customview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.hudson.loveweather.utils.log.LogUtils;

/**
 * Created by Hudson on 2017/12/3.
 * 弹性scrollView
 * 参考：http://blog.csdn.net/a105865708/article/details/17784041
 * 原文有小bug,这里已经做了修改
 *
 * 有一个地方绝对需要注意：scrollView重写onInterceptTouchEvent方法的注意点
 */

public class ElasticScrollView extends ScrollView {
    private View inner;
    private float y;
    private Rect normal = new Rect();
    private boolean animationFinish = true;
    private int mMaxScroll = -1;

    public ElasticScrollView(Context context) {
        super(context);
    }

    public ElasticScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }

    private float mLastX,mLastY;
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
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            return commOnTouchEvent(ev);
        }
//        return super.onTouchEvent(ev);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mMaxScroll = inner.getMeasuredHeight() - getHeight();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public boolean commOnTouchEvent(MotionEvent ev) {
        if (animationFinish) {
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    y = ev.getY();
                    return super.onTouchEvent(ev);
                case MotionEvent.ACTION_MOVE:
                    final float preY = y == 0 ? ev.getY() : y;
                    float nowY = ev.getY();
                    int deltaY = (int) (preY - nowY);
                    y = nowY;
                    int scrollY = getScrollY();
                    LogUtils.e("已经花了"+scrollY+"接下来是"+deltaY);
                    // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                    if ((scrollY ==0&&deltaY<0)||(scrollY == mMaxScroll&&deltaY>0)) {
                        if (normal.isEmpty()) {
                            // 保存正常的布局位置
                            normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                        }
                        // 移动布局
                        int bottom = inner.getBottom() - deltaY / 2;
                        if(bottom>0){//这里有个bug，整个视图移出不可见了就会导致后面的animation不执行，所以防止移出不可见
                            inner.layout(inner.getLeft(), inner.getTop() - deltaY / 2, inner.getRight(), bottom);
                        }
                        return false;
                    } else {
                        LogUtils.e("交给系统处理");
                        return super.onTouchEvent(ev);
                    }
//                    break;
                case MotionEvent.ACTION_UP:
                    y = 0;
                    if (isNeedAnimation()) {
                        animation();
                        return true;
                    }else{
                        return super.onTouchEvent(ev);
                    }
//                    break;
                default:
                    break;
            }
        }
        return true;
    }

    // 开启动画移动
    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, normal.top - inner.getTop());
        ta.setDuration(200);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationFinish = false;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                inner.clearAnimation();
                // 设置回到正常的布局位置
                inner.layout(normal.left, normal.top, normal.right, normal.bottom);
                normal.setEmpty();
                animationFinish = true;
            }
        });
        inner.startAnimation(ta);
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

}
