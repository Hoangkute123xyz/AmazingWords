package com.hoangpro.amazingwords.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.model.ViewTranslate;
import com.hoangpro.amazingwords.morefunc.mAnimation;
import com.hoangpro.amazingwords.sqlite.AppDAO;
import com.hoangpro.amazingwords.sqlite.AppDatabase;
import com.hoangpro.amazingwords.sqlite.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.hoangpro.amazingwords.morefunc.mAnimation.setAnimShowResult;
import static com.hoangpro.amazingwords.morefunc.mAnimation.setAnimTimeOver;
import static com.hoangpro.amazingwords.morefunc.mAnimation.setAnimWrongTextviewAnswer;
import static com.hoangpro.amazingwords.sqlite.User.coin;
import static com.hoangpro.amazingwords.sqlite.User.isNextLv;
import static com.hoangpro.amazingwords.sqlite.User.loadUser;
import static com.hoangpro.amazingwords.sqlite.User.lv;
import static com.hoangpro.amazingwords.sqlite.User.timeCount;
import static com.hoangpro.amazingwords.sqlite.User.wordNameCurrent;
import static com.hoangpro.amazingwords.sqlite.User.writeUser;

public class SortWordActivity extends AppCompatActivity {

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
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_sort_word);
        initView();
        appDatabase = AppDatabase.getInstance(this);
        dao = appDatabase.getDAO();
        loadUser(this);
        if (isNextLv) {
            word = dao.getRandomWord();
            lv++;
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

    CountDownTimer countDownTimer;

    private void runGame() {
        tvCoin.setText(coin + " Xu");
        tvTime.setText(timeCount + "s");
        countDownTimer = new CountDownTimer(timeCount * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                writeUser(SortWordActivity.this, (int) (millisUntilFinished / 1000), coin, lv, word.wordName, false);
                tvTime.setText(timeCount + "s");
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
        countDownTimer.start();
    }

    private void openDialogLose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_win, null, false);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                                            answerTranslate[j],
                                            true
                                    );
                            answerTranslate[j].posionRelative = finalI;
                            Log.e("answerTranslate", answerTranslate[j].toString());

                            tvAnswerArr[j].setText(tvSelectArr[finalI].getText().toString());

                            String result = getAnswer();
                            if (result.length() == wordLength)
                                if (getAnswer().equalsIgnoreCase(word.wordName)) {
                                    Toast.makeText(SortWordActivity.this, "Chính xác", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            for (int i = 0; i < wordLength; i++) {
                int[] location = new int[2];
                tvAnswerArr[i].getLocationOnScreen(location);
                answerTranslate[i] = new ViewTranslate(location[0], location[1]);
                int[] locationSelect = new int[2];
                tvSelectArr[i].getLocationOnScreen(locationSelect);
                selectTranslate[i] = new ViewTranslate(locationSelect[0], locationSelect[1]);
            }
        }
    }

    private void initView() {
        tvLv = findViewById(R.id.tvLv);
        gridGame = findViewById(R.id.gridGame);
        tvTime = findViewById(R.id.tvTime);
        tvCoin = findViewById(R.id.tvCoin);
        tvResult = findViewById(R.id.tvResult);
    }

    private TextView createTextView(ViewGroup parent, int bgView) {
        TextView textView = new TextView(this);
        textView.setBackgroundResource(bgView);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.height = layoutParams.width;
        textView.setTextSize(24f);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        layoutParams.setMargins(15, 15, 15, 15);
        parent.addView(textView);
        return textView;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
        finish();
    }
}
