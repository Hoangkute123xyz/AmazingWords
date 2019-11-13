package com.hoangpro.amazingwords.morefunc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class mAnimation {
    public static AnimatorSet setAnimScaleXY(final View view){
        view.setScaleX(0f);
        view.setScaleY(0f);
        final ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view, "ScaleX", 1.3f);
        objectAnimatorX.setDuration(800);
        ObjectAnimator objectAnimatorXBack = ObjectAnimator.ofFloat(view, "ScaleX", 1f);
        objectAnimatorXBack.setDuration(200);

        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(view, "ScaleY", 1.3f);
        objectAnimatorY.setDuration(800);
        ObjectAnimator objectAnimatorYBack = ObjectAnimator.ofFloat(view, "ScaleY", 1f);
        objectAnimatorYBack.setDuration(200);

        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.play(objectAnimatorX).with(objectAnimatorY);

        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.play(objectAnimatorXBack).with(objectAnimatorYBack);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorSet1).before(animatorSet2);
        return animatorSet;
    }
}
