package com.hudson.loveweather.utils;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
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
import com.hudson.loveweather.service.ScheduledTaskService;
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
     * @param nextBitmapLocalPath 需要切换的目标图片的路径
     */
    public static void backgroundBitmapTransition(final View view, String nextBitmapLocalPath) {
        backgroundBitmapTransition(view,
                createTransitionBackgroundBitmap(BitmapFactory.decodeFile(nextBitmapLocalPath)
                        ,new int[]{0x99000000,0x0000000,0x00000000,0x99000000},
                        new float[]{0.0f,0.3f,0.6f,1.0f}),
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

    public static Bitmap getShowPic() {
        return getShowPic(new int[]{0x99000000,0x0000000,0x00000000,0x99000000},
                new float[]{0.0f,0.3f,0.6f,1.0f});
    }

    /**
     * 从缓存的图片中拿图片，图片经过修改
     * @return
     */
    public static Bitmap getShowPic(int[] colors,float[] stops){
        int picIndex = ScheduledTaskService.mPicIndex;
        picIndex = (picIndex<0)?0:picIndex;
        picIndex = picIndex % Constants.PIC_CACHE_COUNT;
        return createTransitionBackgroundBitmap(BitmapFactory
                        .decodeFile(new StringBuilder(AppStorageUtils.getPicCachePath())
                                .append("/").append(Constants.PIC_CACHE_NAME)
                                .append(picIndex).append(".jpg").toString()),colors,stops);
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
            return createTransitionBackgroundBitmap(Bitmap.createBitmap(source,xOffset,yOffset,destWidth,destHeight)
            ,new int[]{0x0000000,0x00000000,0x99000000}
            ,new float[]{0.0f,0.6f,1.0f});
        }
        return null;
    }

    public static Bitmap createTransitionBackgroundBitmap(Bitmap sourceBg,int[] colors,float[] stops){
        if(sourceBg !=null){
            int width = sourceBg.getWidth();
            int height = sourceBg.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap);
            Bitmap dst = createDarkTransitionBitmap(width,height,colors,stops);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawBitmap(dst,0,0,paint);//目标
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            canvas.drawBitmap(sourceBg,0,0,paint);//源
            return bitmap;
        }
        return null;
    }


    /**
     * 防止因为背景导致文字看不清楚
     * bottom + top
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createDarkTransitionBitmap(int width, int height,int[] colors,float[] stops){
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        LinearGradient gradient = new LinearGradient(0,0,0,height,colors,stops,Shader.TileMode.CLAMP);
        paint.setShader(gradient);
        canvas.drawRect(new Rect(0,0,width,height),paint);
        return bitmap;
    }

}
