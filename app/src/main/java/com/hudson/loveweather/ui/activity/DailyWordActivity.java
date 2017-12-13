package com.hudson.loveweather.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.utils.BitmapUtils;
import com.hudson.loveweather.utils.ShareUtils;
import com.hudson.loveweather.utils.SharedPreferenceUtils;
import com.hudson.loveweather.utils.TimeUtils;
import com.hudson.loveweather.utils.UIUtils;
import com.hudson.loveweather.utils.storage.AppStorageUtils;

import static com.hudson.loveweather.utils.BitmapUtils.gaussianBlur;
import static com.hudson.loveweather.utils.BitmapUtils.getShowPic;

public class DailyWordActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTextView;
    private View mRootView;
    private View mContent;
    public View mCloseView;
    private TextView mDate,mMonth,mDay;
    private View mImage;
    public AnimatorSet mAnimatorSet;

    @Override
    public void setContentViewAndInit() {
        setContentView(R.layout.activity_daily_word);
    }

    @Override
    public void initView() {
        mTextView = (TextView) this.findViewById(R.id.tv_daily_word);
        mRootView = findViewById(R.id.rl_root);
        mContent = findViewById(R.id.ll_content);
        mCloseView = findViewById(R.id.iv_close);
        mImage = findViewById(R.id.rl_img);
        mCloseView.setVisibility(View.INVISIBLE);
        mCloseView.setOnClickListener(this);
        mDate = (TextView) this.findViewById(R.id.tv_date);
        mMonth = (TextView) this.findViewById(R.id.tv_month);
        mDay = (TextView) this.findViewById(R.id.tv_day);
        mDate.setText(TimeUtils.getDayNumberStringOfDate());
        mDay.setText(TimeUtils.getDayWeekOfDate());
        mMonth.setText(TimeUtils.getMonthOfYear());
        mTextView.setText(SharedPreferenceUtils.getInstance().getDailyWords());
        findViewById(R.id.iv_share).setOnClickListener(this);
        final Bitmap background = getShowPic();
        if(background!=null){
            mRootView.setBackground(new BitmapDrawable(background));
            mImage.post(new Runnable() {
                @Override
                public void run() {
                    //很奇葩的问题，由于z轴的存在，所以父view背景必须不能是透明的，
                    //但是如果有透明色存在，通过xfermode方式来完成的圆角顶部会
                    //存在一个很细的父view的背景色的像素，难以消除
                    mImage.setBackground(new BitmapDrawable(
                            BitmapUtils.createTopRoundBitmap(BitmapUtils.clipBitmap(background,mImage.getWidth()
                                    ,mImage.getHeight()),(int) UIUtils.getDimension(R.dimen.round_rect_radius))));
                }
            });
        }
        startInAnimation();
    }

    @Override
    public void recycle() {

    }

    @Override
    public void onBackPressed() {
        startOutAnimation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                startOutAnimation();
                break;
            case R.id.iv_share:
                new Thread(){
                    @Override
                    public void run(){
                        Bitmap bitmap = BitmapUtils.captureScreen(DailyWordActivity.this);
                        if(bitmap!=null){
                            String imagePath = AppStorageUtils.getPicCachePath() + "/capture.jpg";
                            if(!AppStorageUtils.writeFile(imagePath,
                                    BitmapUtils.bitmap2InputStream(bitmap))){
                                imagePath = null;
                            }
                            ShareUtils.shareMsg(DailyWordActivity.this,"分享一句","我看到一句很有意思的话，分享给大家！",SharedPreferenceUtils.getInstance().getDailyWords(),imagePath);
                        }
                    }
                }.start();
                break;
//            case :
//
//                break;
            default:
                break;
        }
    }

    private void startInAnimation(){
        ObjectAnimator translateY = ObjectAnimator.ofFloat(mContent,"translationY",-200.0f,0.0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mContent,"alpha",0f,1f);
        ObjectAnimator rootAlpha = ObjectAnimator.ofFloat(mRootView,"alpha",0f,1f);
        final Drawable background = mRootView.getBackground();
        ValueAnimator backgroundAnimator = ValueAnimator.ofInt(1, 25);
        if (background instanceof BitmapDrawable) {
            final Bitmap original = ((BitmapDrawable) background).getBitmap();
            backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRootView.setBackground(new BitmapDrawable(gaussianBlur((int) animation.getAnimatedValue(), original)));
                }
            });
        }
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(Constants.DEFAULT_BACKGROUND_TRANSITION_DURATION);
        mAnimatorSet.playTogether(translateY,alpha,rootAlpha,backgroundAnimator);
        mAnimatorSet.start();
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCloseView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private void startOutAnimation(){
        if(mAnimatorSet.isStarted()){
            return ;
        }
        ObjectAnimator translateY = ObjectAnimator.ofFloat(mContent,"translationY",0.0f,200.0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mContent,"alpha",1f,0f);
        ObjectAnimator rootAlpha = ObjectAnimator.ofFloat(mRootView,"alpha",1f,0f);
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(Constants.DEFAULT_BACKGROUND_TRANSITION_DURATION);
        mAnimatorSet.playTogether(translateY,alpha,rootAlpha);
        mAnimatorSet.start();
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                DailyWordActivity.this.finish();
                overridePendingTransition(-1,-1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

}
