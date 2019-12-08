package com.hoangpro.amazingwords.presenter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.activity.FindWordGameActivity;
import com.hoangpro.amazingwords.model.FwPos;
import com.hoangpro.amazingwords.sqlite.AppDatabase;
import com.hoangpro.amazingwords.sqlite.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimFloatToTop;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimLeftTo;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimRightTo;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimScaleToSmall;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimScaleXY;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimShowResult;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimWrongTextviewAnswer;
import static com.hoangpro.amazingwords.morefunc.MySession.currentAccount;
import static com.hoangpro.amazingwords.morefunc.MySession.updateAccount;
import static java.lang.Math.abs;

public class FindWordGamePresenter {
    private FindWordGameActivity activity;

    public FindWordGamePresenter(FindWordGameActivity activity) {
        this.activity = activity;
    }

    public void runGame() {
        createUI();
        fillTableGame();
        setAllAnimForView();
    }

    public void actionBuyHint() {
        if (activity.wordList.size() > 0 && currentAccount.coin >=20) {
            int idPosAnswer = activity.posAnswer.size() <= 1 ? 0 : new Random().nextInt(activity.posAnswer.size() - 1);
            activity.returnAnswer(activity.wordList.get(idPosAnswer).wordName);
            activity.returnDivCoin();
            activity.wordList.remove(idPosAnswer);
            List<FwPos> posHint = activity.posAnswer.get(idPosAnswer);
            for (FwPos fwPos : posHint) {
                setAnimScaleToSmall(activity.tvSelectArr[fwPos.X][fwPos.Y], R.drawable.bg_select_correct);
                activity.tvSelectArr[fwPos.X][fwPos.Y].setTextColor(Color.WHITE);
            }
            activity.posAnswer.remove(idPosAnswer);
            currentAccount.coin = currentAccount.coin - 20;
            activity.binding.setUserCoin(String.format("%d %s", currentAccount.coin, activity.getString(R.string.coin)));
            updateAccount(activity, currentAccount);
            activity.checkWin();
        } else {
            Toast.makeText(activity, activity.getString(R.string.not_enough_money), Toast.LENGTH_SHORT).show();
        }
    }

    private void createUI() {
        activity.binding.setUserCoin(String.format("%d %s", currentAccount.coin, activity.getString(R.string.coin)));
        activity.tvSelectArr = new TextView[5][6];
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 5; i++) {
                final int x = i, y = j;
                activity.tvSelectArr[i][j] = createTextView("");
                final TextView tvSelect = activity.tvSelectArr[i][j];
                tvSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvSelect.setBackgroundResource(R.drawable.bg_selected);
                        tvSelect.setTextColor(Color.WHITE);
                        if (activity.firstClick == null) {
                            activity.firstClick = new FwPos(x, y);
                        } else {
                            activity.secondClick = new FwPos(x, y);
                            int rageX = getRangeCount(activity.firstClick.X, activity.secondClick.X);
                            int rageY = getRangeCount(activity.firstClick.Y, activity.secondClick.Y);
                            List<TextView> tvSelectedList = new ArrayList<>();
                            if (rageY == 0) {
                                if (activity.firstClick.X < x)
                                    for (int a = activity.firstClick.X; a <= x; a++) {
                                        tvSelectedList.add(activity.tvSelectArr[a][y]);
                                    }
                                else
                                    for (int a = activity.firstClick.X; a >= x; a--) {
                                        tvSelectedList.add(activity.tvSelectArr[a][y]);
                                    }
                            } else if (rageX == 0) {
                                if (activity.firstClick.Y < y)
                                    for (int a = activity.firstClick.Y; a <= y; a++) {
                                        tvSelectedList.add(activity.tvSelectArr[x][a]);
                                    }
                                else
                                    for (int a = activity.firstClick.Y; a >= y; a--) {
                                        tvSelectedList.add(activity.tvSelectArr[x][a]);
                                    }
                            } else if (rageX == rageY) {
                                int a = activity.firstClick.X;
                                int b = activity.firstClick.Y;
                                if (a <= activity.secondClick.X)
                                    do {
                                        tvSelectedList.add(activity.tvSelectArr[a][b]);
                                        a++;
                                        if (b <= activity.secondClick.Y) {
                                            b++;
                                        } else {
                                            b--;
                                        }
                                    } while (a <= activity.secondClick.X);
                                else
                                    do {
                                        tvSelectedList.add(activity.tvSelectArr[a][b]);
                                        a--;
                                        if (b <= activity.secondClick.Y) {
                                            b++;
                                        } else {
                                            b--;
                                        }
                                    } while (a >= activity.secondClick.X);
                            } else {
                                tvSelectedList.add(activity.tvSelectArr[activity.firstClick.X][activity.firstClick.Y]);
                                tvSelectedList.add(activity.tvSelectArr[activity.secondClick.X][activity.secondClick.Y]);
                            }
                            if (getAnswer(tvSelectedList) == null) {
                                for (final TextView textView : tvSelectedList) {
                                    setAnimWrongTextviewAnswer(textView).addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            if (textView.getScaleX() == 0.7f) {
                                                textView.setBackgroundResource(R.drawable.bg_select_correct);
                                                textView.setTextColor(Color.WHITE);
                                            }
                                        }
                                    });
                                    setAnimShowResult(activity.tvResult, R.drawable.bg_wrong, activity.getString(R.string.wrong));
                                }
                            } else {
                                for (TextView textView : tvSelectedList) {
                                    setAnimScaleToSmall(textView, R.drawable.bg_select_correct);
                                    setAnimShowResult(activity.tvResult, R.drawable.bg_correct, activity.getString(R.string.correct));
                                }
                            }
                            activity.firstClick = null;
                            activity.secondClick = null;
                        }
                    }
                });
            }
        }
    }


    private String getAnswer(List<TextView> tvSelected) {
        String textLeftTo = "";
        String texRightTo = "";
        for (TextView textView : tvSelected) {
            textLeftTo += textView.getText().toString().toLowerCase();
            texRightTo = textView.getText().toString().toLowerCase() + texRightTo;
        }
        for (int i = 0; i < activity.wordList.size(); i++) {
            Word word = activity.wordList.get(i);
            if (word.wordName.equalsIgnoreCase(textLeftTo)) {
                activity.returnAnswer(word.wordName);
                activity.wordList.remove(i);
                activity.posAnswer.remove(i);
                activity.checkWin();
                activity.binding.setRest(String.format("%s: %d", activity.getString(R.string.rest), activity.wordList.size()));
                return textLeftTo;
            } else if (word.wordName.equalsIgnoreCase(texRightTo)) {
                activity.returnAnswer(word.wordName);
                activity.posAnswer.remove(i);
                activity.wordList.remove(i);
                activity.checkWin();
                activity.tvRest.setText(String.format("%s: %d", activity.getString(R.string.rest), activity.wordList.size()));
                return texRightTo;
            }
        }
        return null;
    }

    private int getRangeCount(int numStart, int numEnd) {
        return abs(numEnd - numStart);
    }


    private TextView createTextView(String textSetter) {
        TextView tvSelect = new TextView(activity);
        tvSelect.setVisibility(View.GONE);
        tvSelect.setTextColor(activity.getResources().getColor(R.color.green));
        tvSelect.setBackgroundResource(R.drawable.bg_answer);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 15, 15, 15);
        tvSelect.setTextSize(32f);
        tvSelect.setLayoutParams(layoutParams);
        tvSelect.setGravity(Gravity.CENTER);
        tvSelect.setText(textSetter);
        activity.gridGame.addView(tvSelect);
        return tvSelect;
    }

    private void setAllAnimForView() {
        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.play(setAnimLeftTo(activity.tvBuyIdea)).with(setAnimRightTo(activity.tvCoin)).with(setAnimFloatToTop(activity.tvRest));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(setAnimScaleXY(activity.tvLv)).before(animatorSet1);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                for (int j = 0; j < 6; j++) {
                    for (int i = 0; i < 5; i++) {
                        if (j % 2 == 0) {
                            AnimatorSet objAnim = setAnimLeftTo(activity.tvSelectArr[i][j]);
                            objAnim.setStartDelay((i + j) * 200);
                            objAnim.start();
                        } else {
                            AnimatorSet objAnim = setAnimRightTo(activity.tvSelectArr[i][j]);
                            objAnim.setStartDelay((i + j) * 200);
                            objAnim.start();
                        }
                    }
                }
            }
        });
        animatorSet.start();
    }

    private void getData() {
        activity.binding.setCurrentLv(String.format("%s %d", activity.getString(R.string.level), currentAccount.lvFindWord));
        activity.dao = AppDatabase.getInstance(activity).getDAO();
        activity.wordList = activity.dao.getListWordRandom((new Random().nextInt(2)) + 3);
        for (Word word : activity.wordList) {
            Log.e("wordFind", word.wordName);
            List<String> stringList = new ArrayList<>();
            for (int i = 0; i < word.wordName.length(); i++) {
                stringList.add(word.wordName.charAt(i) + "");
            }
            activity.characterList.add(stringList);
        }
    }


    //Lay danh sach cac hang kha dung:
    private void fillTableGame() {
        getData();
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 5; i++) {

                //Hang cheo
                int a = i, b = j;
                List<FwPos> fwPosList = new ArrayList<>();
                while (a < 5 && b < 6) {
                    fwPosList.add(new FwPos(a, b));
                    a++;
                    b++;
                }
                if (fwPosList.size() >= 2) {
                    activity.posList.add(fwPosList);
                }

                //Hang Ngang

                a = i;
                b = j;
                fwPosList = new ArrayList<>();
                while (a < 5) {
                    fwPosList.add(new FwPos(a, b));
                    a++;
                }
                if (fwPosList.size() > 2) {
                    activity.posList.add(fwPosList);
                }
                //Hang doc

                a = i;
                b = j;
                fwPosList = new ArrayList<>();
                while (b < 6) {
                    fwPosList.add(new FwPos(a, b));
                    b++;
                }
                if (fwPosList.size() > 2) {
                    activity.posList.add(fwPosList);
                }
            }
        }

        //Gan cac dong chu dung vao cac hang tuong ung
        for (int k = 0; k < activity.characterList.size(); k++) {
            List<String> charList = activity.characterList.get(k);
            int countRand = -1;
            while (true) {
                countRand++;
                if (countRand == 1000) {
                    activity.characterList.remove(k);
                    activity.wordList.remove(k);
                    break;
                }
                int idPosList = activity.random.nextInt(activity.posList.size());
                List<FwPos> list = activity.posList.get(idPosList);
                if (list.size() == charList.size()) {
                    boolean isExist = false;
                    for (int i = 0; i < charList.size(); i++) {
                        if (!charList.get(i).equalsIgnoreCase(activity.tvSelectArr[list.get(i).X][list.get(i).Y].getText().toString()) &&
                                !activity.tvSelectArr[list.get(i).X][list.get(i).Y].getText().toString().isEmpty()) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        List<FwPos> posList = new ArrayList<>();
                        for (int i = 0; i < charList.size(); i++) {
                            FwPos fwPos = list.get(i);
                            activity.tvSelectArr[fwPos.X][fwPos.Y].setText(charList.get(i).toUpperCase());
                            posList.add(fwPos);
                        }
                        activity.posAnswer.add(posList);
                        break;
                    }
                }
            }
        }
        //gan text cho cac o con lai
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 5; i++) {
                TextView tvSelect = activity.tvSelectArr[i][j];
                String abc = "QWERTYUIOPASDFGHJKLZXCVBNM";
                if (tvSelect.getText().toString().isEmpty()) {
                    tvSelect.setText((abc.charAt(new Random().nextInt(abc.length())) + "").toUpperCase());
                }
            }
        }
        activity.checkWin();
    }
}
