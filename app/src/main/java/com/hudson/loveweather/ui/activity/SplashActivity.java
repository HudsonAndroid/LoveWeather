package com.hudson.loveweather.ui.activity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.TextView;

import com.hudson.loveweather.R;
import com.hudson.loveweather.utils.AnimationUtils;
import com.hudson.loveweather.utils.BitmapUtils;
import com.hudson.loveweather.utils.SharedPreferenceUtils;

public class SplashActivity extends BaseActivity {

    @Override
    public void setContentViewAndInit() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void initView() {
        View container = findViewById(R.id.rl_root);
        Bitmap showPic = BitmapUtils.getShowPic();
        if(showPic!=null){
            container.setBackground(new BitmapDrawable(showPic));
        }
        SharedPreferenceUtils instance = SharedPreferenceUtils.getInstance();
        ((TextView) findViewById(R.id.tv_words)).setText(instance.getDailyWords());
//        if(!TextUtils.isEmpty(instance.getLastLocationInfo())){//如果不是首次启动
//
//        }
        AnimationUtils.scaleAnimation(4000,container, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(SplashActivity.this,WeatherActivity.class));
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @Override
    public void recycle() {

    }
}
