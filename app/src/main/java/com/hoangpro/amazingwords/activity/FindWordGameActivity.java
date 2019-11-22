package com.hoangpro.amazingwords.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.gridlayout.widget.GridLayout;

import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.base.BaseActivity;
import com.hoangpro.amazingwords.model.FwPos;

import static com.hoangpro.amazingwords.morefunc.mAnimation.setAnimScaleXY;
import static com.hoangpro.amazingwords.sqlite.User.coin;
import static com.hoangpro.amazingwords.sqlite.User.loadUser;
import static java.lang.Math.abs;

public class FindWordGameActivity extends BaseActivity {

    private TextView tvLv;
    private TextView tvBuyIdea;
    private TextView tvCoin;
    private GridLayout gridGame;
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
        setContentView(R.layout.activity_find_word_game);
        initView();
        loadUser(this);

        createUI();
    }

    private TextView[][] tvSelectArr;
    private FwPos firstClick, secondClick;

    private void createUI() {
        tvCoin.setText(String.format("%d %s", coin, getString(R.string.coin)));
        tvSelectArr = new TextView[5][6];
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 5; i++) {
                final int x = i, y = j;
                tvSelectArr[i][j] = creatTextView("A");
                final TextView tvSelect = tvSelectArr[i][j];
                tvSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvSelect.setTextColor(getResources().getColor(R.color.white));
                        tvSelect.setBackgroundResource(R.drawable.bg_idea);
                        tvSelect.setClickable(false);
                        setAnimScaleXY(tvSelect).start();

                        if (firstClick == null) {
                            firstClick = new FwPos(x, y);
                        } else {
                            secondClick = new FwPos(x, y);
                            int rageX = getRangeCount(firstClick.X, secondClick.X);
                            int rageY = getRangeCount(firstClick.Y, secondClick.Y);
                            Log.e("rage", String.format("X: %d, Y: %d", rageX, rageY));
                            if (rageY == 0) {
                                if (firstClick.X < x)
                                    for (int a = firstClick.X; a <= x; a++) {
                                        tvSelectArr[a][y].setBackgroundResource(R.drawable.bg_select_correct);
                                        tvSelectArr[a][y].setClickable(false);
                                        tvSelectArr[a][y].setTextColor(getResources().getColor(R.color.white));
                                    }
                                else
                                    for (int a = firstClick.X; a >= x; a--) {
                                        tvSelectArr[a][y].setBackgroundResource(R.drawable.bg_select_correct);
                                        tvSelectArr[a][y].setClickable(false);
                                        tvSelectArr[a][y].setTextColor(getResources().getColor(R.color.white));
                                    }
                            } else if (rageX == 0) {
                                if (firstClick.Y < y)
                                    for (int a = firstClick.Y; a <= y; a++) {
                                        tvSelectArr[x][a].setBackgroundResource(R.drawable.bg_select_correct);
                                        tvSelectArr[x][a].setTextColor(getResources().getColor(R.color.white));
                                        tvSelectArr[x][a].setClickable(false);
                                    }
                                else
                                    for (int a = firstClick.Y; a >= y; a--) {
                                        tvSelectArr[x][a].setBackgroundResource(R.drawable.bg_select_correct);
                                        tvSelectArr[x][a].setTextColor(getResources().getColor(R.color.white));
                                        tvSelectArr[x][a].setClickable(false);
                                    }
                            } else if (rageX == rageY) {
                                int a = firstClick.X;
                                int b = firstClick.Y;
                                if (a <= secondClick.X)
                                    do {
                                        tvSelectArr[a][b].setBackgroundResource(R.drawable.bg_select_correct);
                                        tvSelectArr[a][b].setTextColor(getResources().getColor(R.color.white));
                                        tvSelectArr[a][b].setClickable(false);
                                        a++;
                                        if (b <= secondClick.Y) {
                                            b++;
                                        } else {
                                            b--;
                                        }
                                    } while (a <= secondClick.X);
                                else
                                    do {
                                        tvSelectArr[a][b].setBackgroundResource(R.drawable.bg_select_correct);
                                        tvSelectArr[a][b].setTextColor(getResources().getColor(R.color.white));
                                        tvSelectArr[a][b].setClickable(false);
                                        a--;
                                        if (b <= secondClick.Y) {
                                            b++;
                                        } else {
                                            b--;
                                        }
                                    } while (a >= secondClick.X);
                            } else {
                                tvSelectArr[firstClick.X][firstClick.Y].setBackgroundResource(R.drawable.bg_answer);
                                tvSelectArr[firstClick.X][firstClick.Y].setTextColor(getResources().getColor(R.color.green));
                                tvSelectArr[firstClick.X][firstClick.Y].setClickable(true);
                                tvSelect.setBackgroundResource(R.drawable.bg_answer);
                                tvSelect.setTextColor(getResources().getColor(R.color.green));
                                tvSelect.setClickable(true);
                            }

                            firstClick = null;
                            secondClick = null;
                        }
                    }
                });
            }
        }
    }

    private int getRangeCount(int numStart, int numEnd) {
        return abs(numEnd - numStart);
    }

    private TextView creatTextView(String textSetter) {
        TextView tvSelect = new TextView(this);
        tvSelect.setTextColor(getResources().getColor(R.color.green));
        tvSelect.setBackgroundResource(R.drawable.bg_answer);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 0, 15, 15);
        tvSelect.setTextSize(32f);
        tvSelect.setLayoutParams(layoutParams);
        tvSelect.setGravity(Gravity.CENTER);
        tvSelect.setText(textSetter);
        gridGame.addView(tvSelect);
        return tvSelect;
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pauseGame(null);
    }
}
