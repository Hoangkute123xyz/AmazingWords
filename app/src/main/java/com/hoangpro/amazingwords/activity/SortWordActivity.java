package com.hoangpro.amazingwords.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.gridlayout.widget.GridLayout;

import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.base.BaseActivity;
import com.hoangpro.amazingwords.model.ViewTranslate;
import com.hoangpro.amazingwords.morefunc.mAnimation;
import com.hoangpro.amazingwords.sqlite.AppDAO;
import com.hoangpro.amazingwords.sqlite.AppDatabase;
import com.hoangpro.amazingwords.sqlite.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.hoangpro.amazingwords.morefunc.mAnimation.setAnimFloatToTop;
import static com.hoangpro.amazingwords.morefunc.mAnimation.setAnimScaleXY;
import static com.hoangpro.amazingwords.morefunc.mAnimation.setAnimShowResult;
import static com.hoangpro.amazingwords.morefunc.mAnimation.setAnimTimeOver;
import static com.hoangpro.amazingwords.morefunc.mAnimation.setAnimTranslateSortGameXY;
import static com.hoangpro.amazingwords.morefunc.mAnimation.setAnimTranslateSortGameXYBack;
import static com.hoangpro.amazingwords.morefunc.mAnimation.setAnimWrongTextviewAnswer;
import static com.hoangpro.amazingwords.sqlite.User.coin;
import static com.hoangpro.amazingwords.sqlite.User.isNextLv;
import static com.hoangpro.amazingwords.sqlite.User.loadUser;
import static com.hoangpro.amazingwords.sqlite.User.lv;
import static com.hoangpro.amazingwords.sqlite.User.timeCount;
import static com.hoangpro.amazingwords.sqlite.User.wordNameCurrent;
import static com.hoangpro.amazingwords.sqlite.User.writeUser;

public class SortWordActivity extends BaseActivity {

    private TextView tvLv;
    private AppDAO dao;
    private AppDatabase appDatabase;
    List<String> charList = new ArrayList<>();
    String[] wordArr;
    TextView[] tvAnswerArr, tvSelectArr;
    private Word word;
    private int wordLength;
    ViewTranslate[] answerTranslate, selectTranslate;
    private GridLayout gridGame;
    private TextView tvTime;
    private TextView tvCoin;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_sort_word);
        initView();
        appDatabase = AppDatabase.getInstance(this);
        dao = appDatabase.getDAO();
        loadUser(this);
        tvLv.setText(String.format("%s %d", getString(R.string.level), lv));
        if (isNextLv) {
            word = dao.getRandomWord();
        } else {
            word = dao.getWordByWordName(wordNameCurrent);
        }
        wordLength = word.wordName.length();
        wordArr = new String[wordLength];
        tvAnswerArr = new TextView[wordLength];
        tvSelectArr = new TextView[wordLength];
        createUI();


        runGame();
    }

    CountDownTimer timerGame;

    private void runGame() {
        tvCoin.setText(coin + " Xu");
        tvTime.setText(timeCount + " s");
        timerGame = new CountDownTimer(timeCount * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                writeUser(SortWordActivity.this, (int) (millisUntilFinished / 1000), coin, lv, word.wordName, false);
                tvTime.setText(timeCount + " s");
                if (timeCount < 11) {
                    setAnimTimeOver(tvTime);
                }
            }

            @Override
            public void onFinish() {
                openDialogLose();
                writeUser(SortWordActivity.this, timeCount, coin, lv, wordNameCurrent, true);
            }
        };
        timerGame.start();
    }

    private void openDialogLose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_lose, null, false);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvResult = view.findViewById(R.id.tvResult);
        Button btnAction = view.findViewById(R.id.btnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MainActivity.class, true);
                overridePendingTransition(0, 0);
            }
        });
        tvResult.setText(String.format("%s: %d", getString(R.string.total_level), lv));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(setAnimScaleXY(tvTitle)).before(setAnimFloatToTop(tvResult));
        animatorSet.start();
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.setCancelable(false);
        dialog = alertDialog;
        alertDialog.show();
    }

    private String getAnswer() {
        String result = "";
        for (TextView textView : tvAnswerArr) {
            result += textView.getText().toString();
        }
        return result.toLowerCase();
    }

    private void createUI() {
        gridGame.setColumnCount(wordLength);
        answerTranslate = new ViewTranslate[wordLength];
        selectTranslate = new ViewTranslate[wordLength];
        String txt = word.wordName;
        Log.e("wordName", txt);
        for (int i = 0; i < wordLength; i++) {
            charList.add(txt.charAt(i) + "");
        }
        Random random = new Random();
        int count = 0;
        while (charList.size() > 0) {
            int i = random.nextInt(charList.size());
            wordArr[count] = charList.get(i);
            charList.remove(i);
            count++;
        }
        for (int i = 0; i < wordLength; i++) {
            tvAnswerArr[i] = createTextView(gridGame, R.drawable.bg_answer);
            tvAnswerArr[i].setAllCaps(true);
            tvAnswerArr[i].setTextColor(Color.WHITE);
            tvAnswerArr[i].setTextColor(Color.parseColor("#1B8162"));


            final int finalI = i;
            tvAnswerArr[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvAnswerArr[finalI].setText("");
                    mAnimation.setAnimTranslateSortGameXYBack(tvSelectArr[answerTranslate[finalI].posionRelative]);
                    Log.e("check", "select = " + answerTranslate[finalI].posionRelative + " answer = " + finalI);
                }
            });
        }


        for (int i = 0; i < wordLength; i++) {
            tvSelectArr[i] = createTextView(gridGame, R.drawable.bg_select_sort_word);
            GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) tvSelectArr[i].getLayoutParams();
            layoutParams.setMargins(15, 100, 15, 15);
            tvSelectArr[i].setLayoutParams(layoutParams);
            tvSelectArr[i].setAllCaps(true);
            tvSelectArr[i].setText(wordArr[i]);
            tvSelectArr[i].setTextColor(Color.WHITE);


            final int finalI = i;

            tvSelectArr[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < wordLength; j++) {
                        if (tvAnswerArr[j].getText().toString().length() == 0) {
                            mAnimation.setAnimTranslateSortGameXY
                                    (
                                            tvSelectArr[finalI],
                                            selectTranslate[finalI],
                                            answerTranslate[j]
                                    );
                            answerTranslate[j].posionRelative = finalI;
                            Log.e("answerTranslate", answerTranslate[j].toString());

                            tvAnswerArr[j].setText(tvSelectArr[finalI].getText().toString());

                            String result = getAnswer();
                            if (result.length() == wordLength)
                                if (getAnswer().equalsIgnoreCase(word.wordName)) {
                                    openDialogWin();
                                } else {
                                    setAnimShowResult(tvResult, R.drawable.bg_wrong, "Sai rồi!!");
                                    for (TextView textView : tvAnswerArr) {
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

    Dialog dialog;

    private void openDialogWin() {
        timerGame.cancel();
        Random random = new Random();
        final int coinReward = 5 + random.nextInt(5);
        final int timeReward = 15 + random.nextInt(15);

        writeUser(this, timeCount + timeReward, coin + coinReward, lv + 1, null, true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(SortWordActivity.this).inflate(R.layout.dialog_win, null, false);
        final TextView tvCoin = view.findViewById(R.id.tvCoin);
        final TextView tvTime = view.findViewById(R.id.tvBuyIdea);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvResult = view.findViewById(R.id.tvResult);
        Button btnAction = view.findViewById(R.id.btnAction);
        tvResult.setText(String.format("%s: %s", getString(R.string.result), word.wordName));
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openActivity(SortWordActivity.class, true);
                overridePendingTransition(0, 0);
            }
        });

        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.play(setAnimScaleXY(tvTitle))
                .before(setAnimFloatToTop(tvResult));

        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.play(setAnimFloatToTop(tvCoin))
                .after(setAnimFloatToTop(tvTime))
                .before(setAnimFloatToTop(btnAction));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorSet1).before(animatorSet2);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                CountDownTimer timeRewardTimer = new CountDownTimer(100 * timeReward, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText("+ " + ((100 * timeReward - millisUntilFinished) / 100) + " s");
                    }

                    @Override
                    public void onFinish() {
                        tvTime.setText(String.format("+ %d s", timeReward));

                    }
                };

                CountDownTimer coinRewardTimer = new CountDownTimer(coinReward * 100, 50) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvCoin.setText("+ " + ((100 * coinReward - millisUntilFinished) / 100) + " " + getString(R.string.coin));
                    }

                    @Override
                    public void onFinish() {
                        tvCoin.setText(String.format("+ %d %s", coinReward, getString(R.string.coin)));
                    }
                };
                coinRewardTimer.start();

                timeRewardTimer.start();
            }
        });
        animatorSet.start();


        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog = alertDialog;
        alertDialog.show();
    }

    boolean isFirst = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (isFirst) {
                for (int i = 0; i < wordLength; i++) {
                    int[] location = new int[2];
                    tvAnswerArr[i].getLocationOnScreen(location);
                    answerTranslate[i] = new ViewTranslate(location[0], location[1]);
                    int[] locationSelect = new int[2];
                    tvSelectArr[i].getLocationOnScreen(locationSelect);
                    selectTranslate[i] = new ViewTranslate(locationSelect[0], locationSelect[1]);
                }
            }
            isFirst = false;
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void initView() {
        tvLv = findViewById(R.id.tvLv);
        gridGame = findViewById(R.id.gridGame);
        tvTime = findViewById(R.id.tvBuyIdea);
        tvCoin = findViewById(R.id.tvCoin);
        tvResult = findViewById(R.id.tvResult);
        gridGame.setAlignmentMode(Gravity.CENTER);
    }

    private TextView createTextView(ViewGroup parent, int bgView) {
        TextView textView = new TextView(this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timerGame.cancel();
        finish();
    }

    int hintCount;

    public void makeHint() {
        Random random = new Random();
        int iAnswer = random.nextInt(wordLength), iSelect = random.nextInt(wordLength);
        if (!tvAnswerArr[iAnswer].isClickable()) {
            while (!tvAnswerArr[iAnswer].isClickable()) {
                iAnswer = random.nextInt(wordLength);
            }
        }

        if (!(tvSelectArr[iSelect].getText().toString()).equalsIgnoreCase((word.wordName.charAt(iAnswer) + "")) || !tvSelectArr[iSelect].isClickable()) {
            while (!(tvSelectArr[iSelect].getText().toString()).equalsIgnoreCase((word.wordName.charAt(iAnswer) + "")) || !tvSelectArr[iSelect].isClickable()) {
                iSelect = random.nextInt(wordLength);
                Log.e("CharTest", tvSelectArr[iSelect].getText().toString());
            }
        }
        tvAnswerArr[iAnswer].setClickable(false);
        setAnimTranslateSortGameXY(tvSelectArr[iSelect], selectTranslate[iSelect], answerTranslate[iAnswer]);
        tvAnswerArr[iAnswer].setTextColor(Color.WHITE);
        tvAnswerArr[iAnswer].setBackgroundResource(R.drawable.bg_idea);
        tvAnswerArr[iAnswer].setText(tvSelectArr[iSelect].getText().toString());
        String result = getAnswer();
        if (result.length() == wordLength)
            if (getAnswer().equalsIgnoreCase(word.wordName)) {
                openDialogWin();
            } else {
                setAnimShowResult(tvResult, R.drawable.bg_wrong, "Sai rồi!!");
                for (TextView textView : tvAnswerArr) {
                    setAnimWrongTextviewAnswer(textView);
                }
            }
    }

    public void actionBuyHint(View view) {
        AnimatorSet animatorSet = null;
        for (int i = 0; i < wordLength; i++) {
            if (tvAnswerArr[i].getText().toString().length() > 0 && tvAnswerArr[i].isClickable()) {
                tvAnswerArr[i].setText("");
                Log.e("positionRelative", answerTranslate[i].posionRelative + "");
                animatorSet = setAnimTranslateSortGameXYBack(tvSelectArr[answerTranslate[i].posionRelative]);
            }
        }
        if (coin >= 20 && hintCount < wordLength) {
            if (animatorSet != null)
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        makeHint();
                    }
                });
            else
                makeHint();
            writeUser(this, timeCount, coin - 20, lv, wordNameCurrent, isNextLv);
            tvCoin.setText(String.format("%d Xu", coin));
        } else {
            Toast.makeText(this, "Không thể mua", Toast.LENGTH_SHORT).show();
        }
    }

    public void pauseGame(View view) {
        timerGame.cancel();
        openActivity(MainActivity.class, true);
    }
}
