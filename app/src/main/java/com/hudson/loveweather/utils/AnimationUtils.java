package com.hudson.loveweather.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Hudson on 2017/12/4.
 */

public class AnimationUtils {

    public static void scaleAnimation(long duration,View view, Animator.AnimatorListener listener){
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view,"scaleX",1.0f,1.1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view,"scaleY",1.0f,1.1f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        set.playTogether(scaleXAnimator,scaleYAnimator);
        set.addListener(listener);
        set.start();
    }
}
