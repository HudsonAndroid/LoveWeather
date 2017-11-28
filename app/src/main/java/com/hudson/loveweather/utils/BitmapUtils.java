package com.hudson.loveweather.utils;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.utils.log.LogUtils;
import com.hudson.loveweather.utils.storage.AppStorageUtils;

import static com.hudson.loveweather.utils.BitmapUtils.BitmapUtilsHelper.renderScript;


/**
 * Created by Hudson on 2017/11/26.
 */

public class BitmapUtils {
    static class BitmapUtilsHelper {
        static final RenderScript renderScript = RenderScript.create(UIUtils.getContext());
    }

    /**
     * 对original图片进行模糊，模糊结果直接在original上
     *
     * @param radius   模糊半径
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
        Bitmap bitmap = Bitmap.createBitmap(original.getWidth(),original.getHeight(),
                Bitmap.Config.ARGB_4444);
        output.copyTo(bitmap);
        return bitmap;
    }

    /**
     * 背景动画切换
     *
     * @param view       设置了bitmap背景的view
     * @param nextBitmap 需要切换的目标
     * @param duration   时长
     */
    public static void backgroundBitmapTransition(final View view, Bitmap nextBitmap, int duration) {
        Drawable background = view.getBackground();
        TransitionDrawable drawable = new TransitionDrawable(
                new Drawable[]{background, new BitmapDrawable(nextBitmap)});
        view.setBackground(drawable);
        drawable.startTransition(duration);
    }

    /**
     * 背景动画切换
     *
     * @param view       设置了bitmap背景的view
     * @param nextBitmap 需要切换的目标
     */
    public static void backgroundBitmapTransition(final View view, Bitmap nextBitmap) {
        backgroundBitmapTransition(view, nextBitmap,
                com.hudson.loveweather.global.Constants.DEFAULT_BACKGROUND_TRANSITION_DURATION);
    }


    public static void backgroundAnimation(final View view,long duration) {
        final Drawable background = view.getBackground();
        if (background instanceof BitmapDrawable) {
            final Bitmap original = ((BitmapDrawable) background).getBitmap();
            ValueAnimator animator = ValueAnimator.ofInt(1, 25);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    LogUtils.e("当前的值" + animation.getAnimatedValue());
                    view.setBackground(new BitmapDrawable(gaussianBlur((int) animation.getAnimatedValue(), original)));
                }
            });
            animator.setDuration(duration);
            animator.start();
        }
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
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

    /**
     * 从缓存的图片中拿第一张
     * @return
     */
    public static Bitmap getShowPic() {
        return BitmapFactory.decodeFile(new StringBuilder(AppStorageUtils.getPicCachePath())
                .append("/").append(Constants.PIC_CACHE_NAME)
                .append("0").append(".jpg").toString());
    }

    /**
     * 提取图片中央块
     * @param source
     * @param destWidth
     * @param destHeight
     * @return
     */
    public static Bitmap clipBitmap(Bitmap source,int destWidth,int destHeight){
        if(source != null){
            int xOffset = (source.getWidth() - destWidth)>>2;
            int yOffset = (source.getHeight() - destHeight)>>2;
            return Bitmap.createBitmap(source,xOffset,yOffset,destWidth,destHeight);
        }
        return null;
    }
}
