package com.hudson.loveweather.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
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

import com.hudson.loveweather.R;
import com.hudson.loveweather.service.ScheduledTaskService;
import com.hudson.loveweather.utils.log.LogUtils;
import com.hudson.loveweather.utils.storage.AppStorageUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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
        if (original != null) {
            Allocation input = Allocation.createFromBitmap(renderScript, original);
            Allocation output = Allocation.createTyped(renderScript, input.getType());
            ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            scriptIntrinsicBlur.setRadius(radius);
            scriptIntrinsicBlur.setInput(input);
            scriptIntrinsicBlur.forEach(output);
            Bitmap bitmap = Bitmap.createBitmap(original.getWidth(), original.getHeight(),
                    Bitmap.Config.ARGB_4444);
            output.copyTo(bitmap);
            return bitmap;
        }
        return null;
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
     * @param view                设置了bitmap背景的view
     * @param nextBitmapLocalPath 需要切换的目标图片的路径
     */
    public static void backgroundBitmapTransition(final View view, String nextBitmapLocalPath) {
        backgroundBitmapTransition(view,
                createTransitionBackgroundBitmap(BitmapFactory.decodeFile(nextBitmapLocalPath)
                        , new int[]{0x99000000, 0x0000000, 0x00000000, 0x99000000},
                        new float[]{0.0f, 0.3f, 0.6f, 1.0f}),
                com.hudson.loveweather.global.Constants.DEFAULT_BACKGROUND_TRANSITION_DURATION);
    }


    public static void backgroundAnimation(final View view, long duration) {
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
        return getShowPic(new int[]{0x99000000, 0x0000000, 0x00000000, 0x99000000},
                new float[]{0.0f, 0.3f, 0.6f, 1.0f});
    }

    /**
     * 从缓存的图片中拿图片，图片经过修改
     *
     * @return
     */
    public static Bitmap getShowPic(int[] colors, float[] stops) {
        int picIndex = ScheduledTaskService.mPicIndex;
        picIndex = (picIndex < 0) ? 0 : picIndex;
        Bitmap sourceBg = BitmapFactory
                .decodeFile(AppStorageUtils.getAppBackgroundPicFilePath(picIndex));
        if (sourceBg == null) {
            sourceBg = BitmapFactory.decodeResource(UIUtils.getContext().getResources(),
                    R.drawable.default_bg);
        }
        return createTransitionBackgroundBitmap(sourceBg, colors, stops);
    }


    /**
     * 提取图片中央块或者放大图片
     *
     * @param source
     * @param destWidth
     * @param destHeight
     * @return
     */
    public static Bitmap clipBitmap(Bitmap source, int destWidth, int destHeight) {
        if (source != null) {
            int width = source.getWidth();
            int height = source.getHeight();
            Bitmap bitmap;
            if(width<destWidth||height<destHeight){//如果原始图片小于我们目标大小，我们进行放大
                //先裁剪后放大
                if(width>destWidth){
                    int xOffset = (width - destWidth) >> 2;
                    LogUtils.e("裁剪宽度"+xOffset);
                    source = Bitmap.createBitmap(source, xOffset, 0, destWidth, height);
                }
                if(height>destHeight){
                    int yOffset = (height - destHeight) >> 2;
                    LogUtils.e("裁剪高度"+yOffset);
                    source = Bitmap.createBitmap(source, 0, yOffset, width, destHeight);
                }
                bitmap = zoomImage(source, destWidth, destHeight);
            }else{//如果原始图片大于我们目标大小，那么我们就裁剪中央块
                int xOffset = (width - destWidth) >> 2;
                int yOffset = (height - destHeight) >> 2;
                bitmap = Bitmap.createBitmap(source, xOffset, yOffset, destWidth, destHeight);
            }
            return createTransitionBackgroundBitmap(bitmap
                    , new int[]{0x0000000, 0x00000000, 0x99000000}
                    , new float[]{0.0f, 0.6f, 1.0f});
        }
        return null;
    }

    public static Bitmap createTransitionBackgroundBitmap(Bitmap sourceBg, int[] colors, float[] stops) {
        if (sourceBg != null) {
            int width = sourceBg.getWidth();
            int height = sourceBg.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap);
            Bitmap dst = createDarkTransitionBitmap(width, height, colors, stops);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawBitmap(dst, 0, 0, paint);//目标
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            canvas.drawBitmap(sourceBg, 0, 0, paint);//源
            return bitmap;
        }
        return null;
    }


    /**
     * 防止因为背景导致文字看不清楚
     * bottom + top
     *
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createDarkTransitionBitmap(int width, int height, int[] colors, float[] stops) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        LinearGradient gradient = new LinearGradient(0, 0, 0, height, colors, stops, Shader.TileMode.CLAMP);
        paint.setShader(gradient);
        canvas.drawRect(new Rect(0, 0, width, height), paint);
        return bitmap;
    }

    public static Bitmap createTopRoundBitmap(Bitmap source, int radius) {
        if (source != null) {
            int width = source.getWidth();
            int height = source.getHeight();
            Bitmap dst = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(dst);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setDither(true);
            canvas.drawRoundRect(0, 0, width, height * 2, radius, radius, paint);//dst
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawRect(0, 0, width, height, paint);//src

            //下面代码结果跟实际总结不一致，怀疑是硬件加速问题导致
//            paint.setColor(UIUtils.getColor(R.color.red));
//            canvas.drawRect(0,0,width,height,paint);//dst
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//            paint.setColor(UIUtils.getColor(R.color.warning_color));
//            canvas.drawRoundRect(0,0,width,height*2,radius,radius,paint);//src,两个半径都得设置且一样,否则无效果

            Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
            Canvas resultCanvas = new Canvas(result);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setDither(true);
            resultCanvas.drawBitmap(dst, 0, 0, paint);//目标
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            resultCanvas.drawBitmap(source, 0, 0, paint);//源
            return result;
        }
        return null;
    }

    /**
     * 获取屏幕截图
     * 获取的结果有异常，比如我们的控件是圆角的，但是截图出来是方形的
     *
     * @param activity
     * @return
     */
    public static Bitmap captureScreen(Activity activity) {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        return activity.getWindow().getDecorView().getDrawingCache();
    }

    public static InputStream bitmap2InputStream(Bitmap bitmap) {
        if(bitmap == null){
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * 不同于clip，scale结果出来的bitmap的宽高未必是目标宽高
     * 等比例缩放图片，这里缩放的比例最终结果未必是目标宽高
     * 本方法不同于zoomImage,本方式是宽高等比例缩放的
     * @param path
     * @param destWidth
     * @param destHeight
     * @return
     */
    public static Bitmap scaleBitmap(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        int scaleY = imageHeight / destHeight;
        int scaleX = imageWidth / destWidth;
        int scale = 1;
        if (scaleX > scaleY & scaleY > 1) {
            scale = scaleX;
        } else if (scaleY > scaleX & scaleX > 1) {
            scale = scaleY;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        return BitmapFactory.decodeFile(path, options);
    }


    /**
     * 压缩图片
     *
     * @param bitMap  要压缩的bitmap对象
     * @param maxSize 压缩的大小(kb)，压缩结果不是很准确大约比你设置的maxSize大于100k是因为比例决定的
     * @return
     */
    public static Bitmap imageZoom(Bitmap bitMap, double maxSize) {
        if (bitMap != null) {
            //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            //将字节换成KB
            double mid = b.length / 1024;
            //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
            if (mid > maxSize) {
                //获取bitmap大小 是允许最大大小的多少倍
                double i = mid / maxSize;
                //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
                bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
                        bitMap.getHeight() / Math.sqrt(i));
            }
        }
        return bitMap;
    }


    /***
     * 图片的缩放方法,使用Matrix方式
     * 本方式不同于scaleBitmap，这里宽度缩放比与高度缩放比不一定相等
     * @param source   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap source, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = source.getWidth();
        float height = source.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 修整图片，防止OOM
     * 1.缩放图片
     * 2.等比例裁剪区域
     * 3.更改显示格式
     *
     * @param path
     * @return
     */
    public static Bitmap resizeBitmap(String path) {
        int[] pixels = new int[2];
        DeviceUtils.getScreen(pixels);
        Bitmap bitmap = scaleBitmap(path, pixels[0], pixels[1]);
        if(bitmap == null){
            return null;
        }
        bitmap = rateClip(bitmap, pixels[0],pixels[1]);
        return resizeBitmap(bitmap,500);
    }

    /**
     * 重整bitmap大小
     * @param bitmap
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap,double maxMemorySize){
        if (bitmap == null) {
            return null;
        }
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap, 0, 0, null);
        //如果图片大小不合格，则再次缩放
        return imageZoom(result,maxMemorySize);//最大占据内存大小
    }

    /**
     * 比例裁剪
     *
     * @param source
     * @return
     */
    public static Bitmap rateClip(Bitmap source, int referWidth,int referHeight) {
        if (source == null) {
            return null;
        }
        float referRate = referHeight*1.0f/referWidth;
        int width = source.getWidth();
        int height = source.getHeight();
        float curRate = height*1.0f/width;
        int destHeight, destWidth;
        int xOffset = 0;
        int yOffset = 0;
        float value = (curRate - referRate) / referRate;
        if(value > 0.2f){//以宽度作为不变值
            destWidth = width;
            destHeight = Math.round(width * referRate);
            yOffset = (height - destHeight) >> 2;
        }else if(value<-0.2f){//以高度作为不变值
            destHeight = height;
            destWidth = Math.round(height / referRate);
            xOffset = (width - destWidth) >> 2;
        }else{
            return source;
        }
        xOffset = xOffset < 0 ? 0 : xOffset;
        yOffset = yOffset < 0 ? 0 : yOffset;
        destWidth = width < destWidth ? width : destWidth;
        destHeight = height < destHeight ? height : destHeight;
        return Bitmap.createBitmap(source, xOffset, yOffset, destWidth, destHeight);
    }
}
