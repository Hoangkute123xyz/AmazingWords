package com.hoangpro.amazingwords.activity;

import android.os.Bundle;
import android.view.View;

import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.base.BaseActivity;

public class GamePlayActivity extends BaseActivity {

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
        setContentView(R.layout.activity_game_play);
    }

    public void openSortWords(View view) {
        openActivity(SortWordActivity.class, true);
    }

    public void openFindWordsGame(View view) {
        openActivity(FindWordGameActivity.class, true);
    }
}
