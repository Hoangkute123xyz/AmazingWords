package com.hoangpro.amazingwords.presenter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.gridlayout.widget.GridLayout;

import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.activity.SortWordActivity;
import com.hoangpro.amazingwords.model.ViewTranslate;
import com.hoangpro.amazingwords.view.SortWordGameView;

import java.util.Random;

import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimFloatToTop;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimLeftTo;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimRightTo;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimScaleXY;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimShowResult;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimTimeOver;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimTranslateSortGameXY;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimTranslateSortGameXYBack;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimWrongTextviewAnswer;
import static com.hoangpro.amazingwords.sqlite.User.coin;
import static com.hoangpro.amazingwords.sqlite.User.isNextLv;
import static com.hoangpro.amazingwords.sqlite.User.lvSortWord;
import static com.hoangpro.amazingwords.sqlite.User.timeCount;
import static com.hoangpro.amazingwords.sqlite.User.wordNameCurrent;
import static com.hoangpro.amazingwords.sqlite.User.writeUser;

public class SortGamePresenter {
    SortWordGameView view;
    SortWordActivity activity;

    public SortGamePresenter(SortWordGameView sortWordGameView, SortWordActivity sortWordActivity) {
        this.view = sortWordGameView;
        this.activity = sortWordActivity;
    }

    public void createUI() {
        view.getData();
        activity.binding.setUserCoin(String.format("%d %s", coin, activity.getString(R.string.coin)));
        activity.binding.setUserTime(String.format("%d s", timeCount));
        activity.gridGame.setColumnCount(activity.wordLength);
        activity.answerTranslate = new ViewTranslate[activity.wordLength];
        activity.selectTranslate = new ViewTranslate[activity.wordLength];
        String txt = activity.word.wordName;
        Log.e("wordName", txt);
        for (int i = 0; i < activity.wordLength; i++) {
            activity.charList.add(txt.charAt(i) + "");
        }
        Random random = new Random();
        int count = 0;
        while (activity.charList.size() > 0) {
            int i = random.nextInt(activity.charList.size());
            activity.wordArr[count] = activity.charList.get(i);
            activity.charList.remove(i);
            count++;
        }
        for (int i = 0; i < activity.wordLength; i++) {
            activity.tvAnswerArr[i] = createTextView(activity.gridGame, R.drawable.bg_answer);
            activity.tvAnswerArr[i].setAllCaps(true);
            activity.tvAnswerArr[i].setTextColor(Color.WHITE);
            activity.tvAnswerArr[i].setTextColor(Color.parseColor("#1B8162"));


            final int finalI = i;
            activity.tvAnswerArr[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.tvAnswerArr[finalI].setText("");
                    setAnimTranslateSortGameXYBack(activity.tvSelectArr[activity.answerTranslate[finalI].posionRelative]);
                }
            });
        }


        for (int i = 0; i < activity.wordLength; i++) {
            activity.tvSelectArr[i] = createTextView(activity.gridGame, R.drawable.bg_select_sort_word);
            GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) activity.tvSelectArr[i].getLayoutParams();
            layoutParams.setMargins(15, 100, 15, 15);
            activity.tvSelectArr[i].setLayoutParams(layoutParams);
            activity.tvSelectArr[i].setAllCaps(true);
            activity.tvSelectArr[i].setText(activity.wordArr[i]);
            activity.tvSelectArr[i].setTextColor(Color.WHITE);


            final int finalI = i;

            activity.tvSelectArr[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < activity.wordLength; j++) {
                        if (activity.tvAnswerArr[j].getText().toString().length() == 0) {
                            setAnimTranslateSortGameXY
                                    (
                                            activity.tvSelectArr[finalI],
                                            activity.selectTranslate[finalI],
                                            activity.answerTranslate[j]
                                    );
                            activity.answerTranslate[j].posionRelative = finalI;
                            Log.e("answerTranslate", activity.answerTranslate[j].toString());

                            activity.tvAnswerArr[j].setText(activity.tvSelectArr[finalI].getText().toString());

                            String result = getAnswer();
                            if (result.length() == activity.wordLength)
                                if (getAnswer().equalsIgnoreCase(activity.word.wordName)) {
                                    view.openDialogWin();
                                } else {
                                    setAnimShowResult(activity.tvResult, R.drawable.bg_wrong, "Sai rồi!!");
                                    for (TextView textView : activity.tvAnswerArr) {
                                        setAnimWrongTextviewAnswer(textView);
                                    }
                                }
                            break;
                        }
                    }
                }
            });
        }
    }

    public void runGame() {
        activity.timerGame = new CountDownTimer(timeCount * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                writeUser(activity, (int) (millisUntilFinished / 1000), coin, lvSortWord, activity.word.wordName, false);
                activity.binding.setUserTime(String.format("%d s", timeCount));
                if (timeCount < 11) {
                    setAnimTimeOver(activity.tvTime);
                }
            }

            @Override
            public void onFinish() {
                view.openDialogLose();
                writeUser(activity, timeCount, coin, lvSortWord, wordNameCurrent, true);
            }
        };
        activity.timerGame.start();
    }

    public void mainGame() {
        createUI();
        activity.tvBuyAction.setClickable(false);
        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.play(setAnimLeftTo(activity.tvTime)).with(setAnimRightTo(activity.tvCoin)).with(setAnimFloatToTop(activity.tvBuyAction));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(setAnimScaleXY(activity.tvLv)).before(animatorSet1);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                for (int i = 0; i < activity.tvAnswerArr.length; i++) {
                    AnimatorSet animObj = setAnimLeftTo(activity.tvAnswerArr[i]);
                    animObj.setStartDelay(i * 300);
                    animObj.start();
                }
                for (int i = activity.tvSelectArr.length - 1; i >= 0; i--) {
                    AnimatorSet animObj = setAnimRightTo(activity.tvSelectArr[i]);
                    animObj.setStartDelay((-i + 2 * activity.tvSelectArr.length) * 300);
                    animObj.start();
                    if (i == 0) {
                        animObj.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                Log.e("hasFocus", activity.hasFocus+"");
                                if (activity.hasFocus)
                                    runGame();
                                activity.tvBuyAction.setClickable(true);
                            }
                        });
                    }
                }
            }
        });
        animatorSet.start();
    }

    public String getAnswer() {
        String result = "";
        for (TextView textView : activity.tvAnswerArr) {
            result += textView.getText().toString();
        }
        return result.toLowerCase();
    }

    public TextView createTextView(ViewGroup parent, int bgView) {
        TextView textView = new TextView(activity);
        textView.setVisibility(View.INVISIBLE);
        textView.setBackgroundResource(bgView);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setTextSize(24f);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        layoutParams.setMargins(15, 15, 15, 15);
        parent.addView(textView);
        return textView;
    }


    public AnimatorSet makeHint() {
        Random random = new Random();
        int iAnswer = random.nextInt(activity.wordLength), iSelect = random.nextInt(activity.wordLength);
        if (!activity.tvAnswerArr[iAnswer].isClickable()) {
            while (!activity.tvAnswerArr[iAnswer].isClickable()) {
                iAnswer = random.nextInt(activity.wordLength);
            }
        }

        if (!(activity.tvSelectArr[iSelect].getText().toString()).equalsIgnoreCase((activity.word.wordName.charAt(iAnswer) + "")) || !activity.tvSelectArr[iSelect].isClickable()) {
            while (!(activity.tvSelectArr[iSelect].getText().toString()).equalsIgnoreCase((activity.word.wordName.charAt(iAnswer) + "")) || !activity.tvSelectArr[iSelect].isClickable()) {
                iSelect = random.nextInt(activity.wordLength);
                Log.e("CharTest", activity.tvSelectArr[iSelect].getText().toString());
            }
        }
        activity.tvAnswerArr[iAnswer].setClickable(false);
        AnimatorSet animatorSet = setAnimTranslateSortGameXY(activity.tvSelectArr[iSelect], activity.selectTranslate[iSelect], activity.answerTranslate[iAnswer]);
        activity.tvAnswerArr[iAnswer].setTextColor(Color.WHITE);
        activity.tvAnswerArr[iAnswer].setBackgroundResource(R.drawable.bg_idea);
        activity.tvAnswerArr[iAnswer].setText(activity.tvSelectArr[iSelect].getText().toString());
        String result = getAnswer();
        if (result.length() == activity.wordLength)
            if (getAnswer().equalsIgnoreCase(activity.word.wordName)) {
                view.openDialogWin();
            } else {
                setAnimShowResult(activity.tvResult, R.drawable.bg_wrong, "Sai rồi!!");
                for (TextView textView : activity.tvAnswerArr) {
                    setAnimWrongTextviewAnswer(textView);
                }
            }
        return animatorSet;
    }

    public void actionHint() {
        activity.tvBuyAction.setClickable(false);
        AnimatorSet animatorSet = null;
        for (int i = 0; i < activity.wordLength; i++) {
            if (activity.tvAnswerArr[i].getText().toString().length() > 0 && activity.tvAnswerArr[i].isClickable()) {
                activity.tvAnswerArr[i].setText("");
                Log.e("positionRelative", activity.answerTranslate[i].posionRelative + "");
                animatorSet = setAnimTranslateSortGameXYBack(activity.tvSelectArr[activity.answerTranslate[i].posionRelative]);
            }
        }
        if (coin >= 20 && activity.hintCount < activity.wordLength) {
            if (animatorSet != null)
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        makeHint().addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                activity.tvBuyAction.setClickable(true);
                            }
                        });
                    }
                });
            else
                makeHint().addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        activity.tvBuyAction.setClickable(true);
                    }
                });
            writeUser(activity, timeCount, coin - 20, lvSortWord, wordNameCurrent, isNextLv);
            activity.binding.setUserCoin(String.format("%d %s", coin, activity.getString(R.string.coin)));
        } else {
            Toast.makeText(activity, activity.getString(R.string.not_enough_money), Toast.LENGTH_SHORT).show();
        }
    }

}
