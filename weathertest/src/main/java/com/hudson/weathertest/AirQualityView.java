package com.hudson.weathertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import static android.R.attr.radius;

/**
 * Created by Hudson on 2018/5/27.
 */

public class AirQualityView extends View {
    private Paint mPaint;
    private int mRadius;
    private static final int STROKE_WIDTH = 20;
    private int mAirColor = 0xfe00ff00;
    private int mLevelCount = 6;
    private int mCurLevel = 1;
    private LinearGradient mLinearGradient;
    private Rect mRect;
    public int mCenterX;
    public int mCenterY;
    private Bitmap mDst,mSrc;
    public Bitmap mBitmap;

    public AirQualityView(Context context) {
        this(context, null);
    }

    public AirQualityView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AirQualityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mAirColor);
        mPaint.setStrokeWidth(STROKE_WIDTH);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(MeasureSize(width, widthMode,1),
                MeasureSize(height, heightMode,2));//或者使用super.onMeasure()
    }

    /**
     * 测量控件大小
     * @param size
     * @param sizeMode
     * @param code 1表示width，2表示height
     * @return
     */
    private int MeasureSize(int size, int sizeMode,int code) {
        if (sizeMode == MeasureSpec.EXACTLY) {// 如果指定了明确的大小
            return size;
        } else {// 根据我们的情况设置大小
            int requireSize = 0;
            if(code == 1){//表示width
                requireSize = getPaddingLeft()+getPaddingRight()+radius*2;
            }else if(code == 2){//表示height
                requireSize = getPaddingBottom()+getPaddingTop()+radius*2;
            }
            if (sizeMode == MeasureSpec.AT_MOST) {
                requireSize = Math.min(size, requireSize);
            }
            return requireSize;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int contentHeight = h - getPaddingTop() - getPaddingBottom() - STROKE_WIDTH;
        int width = w - getPaddingLeft() - getPaddingRight() - STROKE_WIDTH;
        mRadius = Math.min(contentHeight,width) / 2;
        int startX = getPaddingLeft();
        int startY = getPaddingTop();
        mCenterX = startX + mRadius + STROKE_WIDTH/2;
        mCenterY = startY + mRadius + STROKE_WIDTH/2;
        mRect = new Rect(startX, startY, startX + mRadius*2 + STROKE_WIDTH,startY+mRadius*2+STROKE_WIDTH);
        updateGradient();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mAirColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX,mCenterY,mRadius,mPaint);
//        canvas.drawBitmap(mBitmap,0,0,null);
////        canvas.drawBitmap(mDst,0,0,mPaint);
//        canvas.drawCircle(mCenterX,mCenterY,mRadius,mPaint);
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
//        canvas.drawBitmap(mSrc,0,0,mPaint);
//        canvas.drawRect(new Rect(getPaddingLeft()));
//        canvas.drawArc(paddingLeft+STROKE_WIDTH/2, paddingTop+STROKE_WIDTH/2, paddingLeft + mRadius*2, paddingTop +mRadius*2,0f,360.0f,true,mPaint);
    }

    private void updateGradient(){
        mLinearGradient = new LinearGradient(0,mRect.bottom,
                0, mRect.top, new int[]{mAirColor, Color.TRANSPARENT},
                new float[]{0.0f,1.0f}, Shader.TileMode.CLAMP);
//        initDst();
//        initSrc();
//        mBitmap = Bitmap.createBitmap(mRect.width(),mRect.height(), Bitmap.Config.ARGB_4444);
//        Canvas canvas = new Canvas(mBitmap);
//        Paint paint = new Paint();
//        canvas.drawBitmap(mDst,0,0,paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(mSrc,0,0,paint);
    }

    private void initDst(){
        mDst = Bitmap.createBitmap(mRect.width(),mRect.height(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(mDst);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX,mCenterY,mRadius,paint);
    }

    private void initSrc(){
        mSrc = Bitmap.createBitmap(mRect.width(),mRect.height(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(mSrc);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(mLinearGradient);
        canvas.drawRect(mRect,paint);
    }

    private int[] generateColorArray(){
       int[] colors = new int[mLevelCount+1];
        int refer = mCurLevel - 1;
        for (int i = 0; i < colors.length; i++) {
            if(i<=refer){
                colors[i] = colorAlpha(0.6f,mAirColor);
            }else{
                colors[i] = colorAlpha(0.6f-(i-refer)*0.4f,mAirColor);
            }
        }
        return colors;
    }

    /**
     * 根据占比，来获取渐变色对应的颜色
     * 思路来源：系统渐变色动画
     * @param percentage 占比0.0-1.0之间
     * @param resColor 颜色
     * @return
     */
    private int colorAlpha(float percentage, int resColor) {
        int startA = (resColor >> 24) & 0xff;
        int startR = (resColor >> 16) & 0xff;
        int startG = (resColor >> 8) & 0xff;
        int startB = resColor & 0xff;
        if(percentage<0){
            percentage = 0;
        }
        return ((int)(startA *percentage ) << 24) |((startR ) << 16) | startG << 8 | startB;
    }

    private float[] generateColorRangeArray(){
        float average = 1.0f / mLevelCount;
        float[] colorRange = new float[mLevelCount+1];
        float tmp = 0f;
        for (int i = 0; i < colorRange.length; i++) {
            colorRange[i] = tmp;
            tmp += average;
        }
        return colorRange;
    }

    public int getAirColor() {
        return mAirColor;
    }

    public void setAirColor(int airColor) {
        mAirColor = airColor;
    }

    public int getLevelCount() {
        return mLevelCount;
    }

    public void setLevelCount(int levelCount) {
        mLevelCount = levelCount;
        updateGradient();
        invalidate();
    }

    public int getCurLevel() {
        return mCurLevel;
    }

    public void setCurLevel(int curLevel) {
        if(mCurLevel<=0||mCurLevel>mLevelCount-1){
            mCurLevel = 1;
        }else{
            mCurLevel = curLevel;
        }
        updateGradient();
        invalidate();
    }
}
