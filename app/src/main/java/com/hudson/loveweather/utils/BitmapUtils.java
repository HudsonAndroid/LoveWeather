package com.hudson.loveweather.utils;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.IntRange;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import com.hudson.loveweather.utils.log.LogUtils;

import static com.hudson.loveweather.utils.BitmapUtils.BitmapUtilsHelper.renderScript;


/**
 * Created by Hudson on 2017/11/26.
 */

public class BitmapUtils {
    static class BitmapUtilsHelper{
        static final RenderScript renderScript = RenderScript.create(UIUtils.getContext());
    }

    /**
     * 对original图片进行模糊，模糊结果直接在original上
     * @param radius 模糊半径
     * @param original 被模糊的图片
     * @return
     */
    public static Bitmap gaussianBlur(@IntRange(from = 1, to = 25) int radius, Bitmap original) {
        Allocation input = Allocation.createFromBitmap(renderScript, original);
        Allocation output = Allocation.createTyped(renderScript, input.getType());
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setRadius(radius);
        scriptIntrinsicBlur.setInput(input);
        scriptIntrinsicBlur.forEach(output);
        output.copyTo(original);
        return original;
    }

    /**
     * 背景动画切换
     * @param view 设置了bitmap背景的view
     * @param nextBitmap 需要切换的目标
     * @param duration 时长
     * @return
     */
    public static boolean backgroundBitmapTrasition(final View view,Bitmap nextBitmap,int duration){
        final Drawable background = view.getBackground();
        if(background instanceof BitmapDrawable) {
            Bitmap original = ((BitmapDrawable) background).getBitmap();
            TransitionDrawable drawable = new TransitionDrawable(
                    new Drawable[]{new BitmapDrawable(original),new BitmapDrawable(nextBitmap)});
            view.setBackground(drawable);
            drawable.startTransition(duration);
        }
        return false;
    }

    /**
     * 背景动画切换
     * @param view 设置了bitmap背景的view
     * @param nextBitmap 需要切换的目标
     * @return
     */
    public static boolean backgroundBitmapTrasition(final View view,Bitmap nextBitmap){
        return backgroundBitmapTrasition(view,nextBitmap,
                com.hudson.loveweather.global.Constants.DEFAULT_BACKGROUND_TRANSITION_DURATION);
    }


    public static void backgroundAnimation(final View view){
        final Drawable background = view.getBackground();
        if(background instanceof BitmapDrawable){
            final Bitmap original = ((BitmapDrawable) background).getBitmap();
            ValueAnimator animator = ValueAnimator.ofInt(1,20,1);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    LogUtils.e("当前的值"+animation.getAnimatedValue());
                    view.setBackground(new BitmapDrawable(gaussianBlur((int)animation.getAnimatedValue(),original)));
                }
            });
            animator.setDuration(2000);
            animator.start();
        }
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable)
    {
        if (drawable instanceof BitmapDrawable)
        {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}
