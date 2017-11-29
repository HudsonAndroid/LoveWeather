package com.hudson.loveweather.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.global.Constants;
import com.hudson.loveweather.utils.BitmapUtils;
import com.hudson.loveweather.utils.HttpUtils;
import com.hudson.loveweather.utils.TimeUtils;
import com.hudson.loveweather.utils.ToastUtils;
import com.hudson.loveweather.utils.UIUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.hudson.loveweather.utils.BitmapUtils.gaussianBlur;
import static com.hudson.loveweather.utils.BitmapUtils.getShowPic;

public class DailyWordActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTextView;
    private View mRootView;
    private View mContent;
    public View mCloseView;
    private TextView mDate,mMonth,mDay;
    private View mImage;

    @Override
    public void setContentViewAndInit() {
        setContentView(R.layout.activity_daily_word);
        getDailyWord();
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
        mDate.setText(TimeUtils.getDayNumberOfDate());
        mDay.setText(TimeUtils.getDayWeekOfDate());
        mMonth.setText(TimeUtils.getMonthOfYear());
        findViewById(R.id.iv_share).setOnClickListener(this);
        final Bitmap background = getShowPic();
        if(background!=null){
            mRootView.setBackground(new BitmapDrawable(background));
            mImage.post(new Runnable() {
                @Override
                public void run() {
                    mImage.setBackground(new BitmapDrawable(
                            BitmapUtils.clipBitmap(background,mImage.getWidth()
                                    ,mImage.getHeight())));
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
                ToastUtils.showToast("分享了");
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
        AnimatorSet set = new AnimatorSet();
        set.setDuration(Constants.DEFAULT_BACKGROUND_TRANSITION_DURATION);
        set.playTogether(translateY,alpha,rootAlpha,backgroundAnimator);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
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

    //避免快速点击导致重复回调动画
    private boolean mIsAnimationStarted = false;

    private void startOutAnimation(){
        if(mIsAnimationStarted){
            return ;
        }
        ObjectAnimator translateY = ObjectAnimator.ofFloat(mContent,"translationY",0.0f,200.0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mContent,"alpha",1f,0f);
        ObjectAnimator rootAlpha = ObjectAnimator.ofFloat(mRootView,"alpha",1f,0f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(Constants.DEFAULT_BACKGROUND_TRANSITION_DURATION);
        set.playTogether(translateY,alpha,rootAlpha);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsAnimationStarted = true;
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

    private void getDailyWord(){
        HttpUtils.requestNetData(Constants.DAILY_WORD_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String words = response.body().string();
                if(!TextUtils.isEmpty(words)){
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setText(words);
                        }
                    });
                }
            }
        });
    }

}
