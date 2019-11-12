package com.hoangpro.amazingwords.morefunc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class mAnimation {
    public static void setAnimScaleXY(final View view){
        final ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view, "ScaleX", 0f,1f);
        objectAnimatorX.setDuration(800);
//        objectAnimatorX.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                ObjectAnimator objectAnimatorXBack = ObjectAnimator.ofFloat(view, "ScaleX", 1f);
//                objectAnimatorXBack.setDuration(400);
//                objectAnimatorXBack.start();
//            }
//        });
        objectAnimatorX.start();

        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(view, "ScaleY", 0f,1f);
        objectAnimatorY.setDuration(800);
//        objectAnimatorY.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                ObjectAnimator objectAnimatorYBack = ObjectAnimator.ofFloat(view, "ScaleY", 1f);
//                objectAnimatorYBack.setDuration(400);
//                objectAnimatorYBack.start();
//            }
//        });
        objectAnimatorY.start();
    }
}
