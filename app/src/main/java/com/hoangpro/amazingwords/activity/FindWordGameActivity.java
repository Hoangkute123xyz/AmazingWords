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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.gridlayout.widget.GridLayout;

import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.base.BaseActivity;
import com.hoangpro.amazingwords.databinding.ActivityFindWordGameBinding;
import com.hoangpro.amazingwords.model.FwPos;
import com.hoangpro.amazingwords.presenter.FindWordGamePresenter;
import com.hoangpro.amazingwords.sqlite.AppDAO;
import com.hoangpro.amazingwords.sqlite.Word;
import com.hoangpro.amazingwords.view.FindWordGameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimFloatToTop;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimScaleXY;
import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimShowAnswer;
import static com.hoangpro.amazingwords.morefunc.MySession.currentAccount;
import static com.hoangpro.amazingwords.morefunc.MySession.updateAccount;

public class FindWordGameActivity extends BaseActivity implements FindWordGameView {

    public TextView tvLv;
    public TextView tvBuyIdea;
    public TextView tvCoin;
    public GridLayout gridGame;
    public TextView tvResult;
    public TextView tvRest;
    public TextView tvAnswer;
    public TextView tvDivCoin;
    public List<List<FwPos>> posList = new ArrayList<>(), posAnswer = new ArrayList<>();
    public Random random = new Random();
    public List<Word> wordList;
    public AppDAO dao;
    public List<List<String>> characterList = new ArrayList<>();
    public TextView[][] tvSelectArr;
    public FwPos firstClick, secondClick;
    public Dialog dialog;
    public ActivityFindWordGameBinding binding;
    private FindWordGamePresenter presenter;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_word_game);
        initView();
        presenter = new FindWordGamePresenter(this);
        presenter.runGame();
    }


    public void pauseGame(View view) {
        openActivity(MainActivity.class, true);
    }

    private void initView() {
        tvLv = findViewById(R.id.tvLv);
        tvBuyIdea = findViewById(R.id.tvBuyIdea);
        tvCoin = findViewById(R.id.tvCoin);
        gridGame = findViewById(R.id.gridGame);
        tvResult = findViewById(R.id.tvResult);
        tvRest = findViewById(R.id.tvRest);
        tvAnswer = findViewById(R.id.tvAnswer);
        tvDivCoin = findViewById(R.id.tvDivCoin);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pauseGame(null);
    }

    public void actionBuyHint(View view) {
        presenter.actionBuyHint();
    }

    @Override
    public void checkWin() {
        binding.setRest(String.format("%s: %d", getString(R.string.rest), wordList.size()));
        if (wordList.size() == 0) {
            final int coinReward = 5 + new Random().nextInt(5);
            currentAccount.coin += coinReward;
            currentAccount.lvFindWord += 1;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_win, null, false);
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            TextView tvTime = view.findViewById(R.id.tvBuyIdea);
            final TextView tvCoin = view.findViewById(R.id.tvCoin);
            TextView tvResult = view.findViewById(R.id.tvResult);
            Button btnAction = view.findViewById(R.id.btnAction);
            tvResult.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
            AnimatorSet animatorSet1 = new AnimatorSet();
            animatorSet1.play(setAnimScaleXY(tvTitle)).before(setAnimFloatToTop(tvCoin));
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(animatorSet1).before(setAnimFloatToTop(btnAction));
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    CountDownTimer timer = new CountDownTimer(coinReward * 100, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tvCoin.setText(String.format("+ %d %s", coinReward - millisUntilFinished / 100, getString(R.string.coin)));
                        }

                        @Override
                        public void onFinish() {
                            tvCoin.setText(String.format("+ %d %s", coinReward, getString(R.string.coin)));
                        }
                    };
                    timer.start();
                }
            });
            animatorSet.start();
            btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CountDownTimer timer = new CountDownTimer(400, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            openActivity(FindWordGameActivity.class, true);
                            overridePendingTransition(0, 0);
                        }
                    };
                    updateAccount(getApplicationContext(), currentAccount);
                    timer.start();
                    dialog.dismiss();
                }
            });

            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.setCancelable(false);
            dialog = alertDialog;
            alertDialog.show();
        }
    }


    @Override
    public void returnAnswer(String text) {
        tvBuyIdea.setClickable(false);
        tvAnswer.setTextColor(Color.GREEN);
        tvAnswer.setTextSize(48f);
        tvAnswer.setText(text);
        setAnimShowAnswer(tvAnswer).addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tvBuyIdea.setClickable(true);
            }
        });
    }

    @Override
    public void returnDivCoin() {
        tvDivCoin.setText(String.format("- 50 %s", getString(R.string.coin)));
        tvDivCoin.setTextSize(16);
        tvDivCoin.setTextColor(getResources().getColor(R.color.yellow));
        setAnimShowAnswer(tvDivCoin);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateAccount(this, currentAccount);
    }
}
