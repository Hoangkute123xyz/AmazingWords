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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.gridlayout.widget.GridLayout;

import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.base.BaseActivity;
import com.hoangpro.amazingwords.databinding.ActivitySortWordBinding;
import com.hoangpro.amazingwords.model.ViewTranslate;
import com.hoangpro.amazingwords.presenter.SortGamePresenter;
import com.hoangpro.amazingwords.sqlite.AppDAO;
import com.hoangpro.amazingwords.sqlite.AppDatabase;
import com.hoangpro.amazingwords.sqlite.Word;
import com.hoangpro.amazingwords.view.SortWordGameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimFloatToTop;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimScaleXY;
import static com.hoangpro.amazingwords.morefunc.MySession.currentAccount;
import static com.hoangpro.amazingwords.morefunc.MySession.updateAccount;

public class SortWordActivity extends BaseActivity implements SortWordGameView {

    public TextView tvLv;
    public AppDAO dao;
    public AppDatabase appDatabase;
    public List<String> charList = new ArrayList<>();
    public String[] wordArr;
    public TextView[] tvAnswerArr, tvSelectArr;
    public Word word;
    public int wordLength;
    public ViewTranslate[] answerTranslate, selectTranslate;
    public GridLayout gridGame;
    public TextView tvTime;
    public TextView tvCoin;
    public TextView tvResult;
    public TextView tvBuyAction;
    public CountDownTimer timerGame;
    public Dialog dialog;
    public int hintCount;
    public boolean isFirst = true;
    public ActivitySortWordBinding binding;
    public boolean hasFocus = false;
    SortGamePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sort_word);
        presenter = new SortGamePresenter(this, this);
        initView();
        presenter.mainGame();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        this.hasFocus = hasFocus;
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pauseGame(null);
    }

    public void initView() {
        tvLv = findViewById(R.id.tvLv);
        gridGame = findViewById(R.id.gridGame);
        tvTime = findViewById(R.id.tvBuyIdea);
        tvCoin = findViewById(R.id.tvCoin);
        tvResult = findViewById(R.id.tvResult);
        gridGame.setAlignmentMode(Gravity.CENTER);
        tvBuyAction = findViewById(R.id.tvBuyAction);
    }

    @Override
    public void openDialogLose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_lose, null, false);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvResult = view.findViewById(R.id.tvResult);
        Button btnAction = view.findViewById(R.id.btnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openActivity(MainActivity.class, true);
                overridePendingTransition(0, 0);
            }
        });
        tvResult.setText(String.format("%s: %d", getString(R.string.total_level), currentAccount.lvSortWord));
        currentAccount.lvSortWord = 1;
        currentAccount.timeCount = 30;
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
        updateAccount(this, currentAccount);
    }

    @Override
    public void openDialogWin() {
        timerGame.cancel();
        Random random = new Random();
        final int coinReward = 5 + random.nextInt(5);
        final int timeReward = 15 + random.nextInt(15);
        currentAccount.timeCount += timeReward;
        currentAccount.coin += coinReward;
        currentAccount.lvSortWord += 1;

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
        updateAccount(this, currentAccount);
    }

    @Override
    public void getData() {
        appDatabase = AppDatabase.getInstance(this);
        dao = appDatabase.getDAO();
        binding.setLvCurrent(String.format("%s %d", getString(R.string.level), currentAccount.lvSortWord));

        word = dao.getRandomWord();

        wordLength = word.wordName.length();
        wordArr = new String[wordLength];
        tvAnswerArr = new TextView[wordLength];
        tvSelectArr = new TextView[wordLength];
    }


    public void actionBuyHint(View view) {
        presenter.actionHint();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerGame != null)
            timerGame.cancel();
        hasFocus = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateAccount(this, currentAccount);
    }

    public void pauseGame(View view) {
        if (timerGame != null)
            timerGame.cancel();
        openActivity(MainActivity.class, true);
    }
}
