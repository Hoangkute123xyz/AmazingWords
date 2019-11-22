package com.hoangpro.amazingwords.morefunc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.model.ViewTranslate;


public class mAnimation {
    public static AnimatorSet setAnimScaleXY(final View view) {
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

    public static AnimatorSet setAnimTranslateSortGameXY(final View view, ViewTranslate from, ViewTranslate target) {
        view.setVisibility(View.VISIBLE);
        view.setClickable(false);
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view, "TranslationX", target.floatX - from.floatX);
        objectAnimatorX.setDuration(500);

        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(view, "TranslationY", target.floatY - from.floatY);
        objectAnimatorY.setDuration(500);

        ObjectAnimator objectAnimatorAlpha = ObjectAnimator.ofFloat(view, "Alpha", 0f);
        objectAnimatorAlpha.setDuration(500);

        objectAnimatorAlpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimatorX).with(objectAnimatorY).with(objectAnimatorAlpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setClickable(false);
            }
        });
        animatorSet.start();
        return animatorSet;
    }

    public static AnimatorSet setAnimTranslateSortGameXYBack(final View view) {
        view.setVisibility(View.VISIBLE);
        view.setClickable(false);
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view, "TranslationX", 1f);
        objectAnimatorX.setDuration(500);

        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(view, "TranslationY", 1f);
        objectAnimatorY.setDuration(500);

        ObjectAnimator objectAnimatorAlpha = ObjectAnimator.ofFloat(view, "Alpha", 1f);
        objectAnimatorAlpha.setDuration(400);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimatorX).with(objectAnimatorY).with(objectAnimatorAlpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setClickable(true);
            }
        });
        animatorSet.start();
        return animatorSet;
    }

    public static void setAnimTimeOver(final View view) {
        view.setBackgroundResource(R.drawable.bg_panel_game_mini_wrong);
        ObjectAnimator objectAnimatorRight = ObjectAnimator.ofFloat(view, "TranslationX", 50f);
        objectAnimatorRight.setDuration(150);

        ObjectAnimator objectAnimatorBack = ObjectAnimator.ofFloat(view, "TranslationX", 1f);
        objectAnimatorBack.setDuration(150);

        ObjectAnimator objectAnimatorLeft = ObjectAnimator.ofFloat(view, "TranslationX", -50f);
        objectAnimatorLeft.setDuration(150);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimatorRight).before(objectAnimatorBack).after(objectAnimatorLeft).before(objectAnimatorBack);
        animatorSet.start();
    }

    public static void setAnimShowResult(final TextView textView, int backGround, String textSetter) {
        textView.setVisibility(View.VISIBLE);
        textView.setBackgroundResource(backGround);
        textView.setText(textSetter);
        textView.setTranslationY(-900f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(textView, "TranslationY", 1f);
        objectAnimator.setDuration(400);

        ObjectAnimator objectAnimatorBack = ObjectAnimator.ofFloat(textView, "TranslationY", -900f);
        objectAnimatorBack.setDuration(200);
        objectAnimatorBack.setStartDelay(1000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator).before(objectAnimatorBack);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                textView.setVisibility(View.GONE);
            }
        });
        animatorSet.start();
    }

    public static void setAnimWrongTextviewAnswer(final TextView textView) {
        if (textView.isClickable()) {
            textView.setBackgroundResource(R.drawable.bg_answer_wrong);
            textView.setTextColor(Color.parseColor("#9F0E60"));
            ObjectAnimator objectAnimatorRight = ObjectAnimator.ofFloat(textView, "rotation", 25f);
            objectAnimatorRight.setDuration(200);

            ObjectAnimator objectAnimatorLeft = ObjectAnimator.ofFloat(textView, "rotation", -25f);
            objectAnimatorLeft.setDuration(200);

            ObjectAnimator objectAnimatorBack = ObjectAnimator.ofFloat(textView, "rotation", 1f);
            objectAnimatorBack.setDuration(200);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(objectAnimatorRight).before(objectAnimatorBack).after(objectAnimatorLeft).before(objectAnimatorBack);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    textView.setBackgroundResource(R.drawable.bg_answer);
                    textView.setTextColor(Color.parseColor("#098A45"));
                }
            });
            animatorSet.start();
        }
    }

    public static AnimatorSet setAnimFloatToTop(View view) {
        view.setTranslationY(view.getTranslationY() - 250f);
        view.setAlpha(0);
        ObjectAnimator objectAnimatorToTOp = ObjectAnimator.ofFloat(view, "TranslationY", 1f);
        objectAnimatorToTOp.setDuration(300);

        ObjectAnimator objectAnimatorAlpha = ObjectAnimator.ofFloat(view, "Alpha", 1f);
        objectAnimatorAlpha.setDuration(250);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimatorToTOp).with(objectAnimatorAlpha);
        return animatorSet;
    }
}
