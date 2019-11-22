package com.hoangpro.amazingwords.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.base.BaseActivity;

public class MainActivity extends BaseActivity {
//hello change
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
        setContentView(R.layout.activity_main);
    }

    public void openGamePlay(View view) {
        openActivity(GamePlayActivity.class, true);
    }

    public void openRank(View view) {
        openActivity(RankActivity.class, true);
    }

    public void openAbout(View view) {
        openActivity(AboutActivity.class, true);
    }
}
