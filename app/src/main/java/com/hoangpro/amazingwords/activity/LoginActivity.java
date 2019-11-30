package com.hoangpro.amazingwords.activity;

import android.animation.AnimatorSet;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.base.BaseActivity;
import com.hoangpro.amazingwords.morefunc.DatabaseAccess;
import com.hoangpro.amazingwords.morefunc.MyAnimation;

public class LoginActivity extends BaseActivity {

    private ImageView imgAmazing;
    private ImageView imgWords;

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
        setContentView(R.layout.activity_login);
        initView();
        DatabaseAccess databaseAccess = new DatabaseAccess(this);
        databaseAccess.createDatabase();
    }

    private void initView() {
        imgAmazing = findViewById(R.id.imgAmazing);
        imgWords = findViewById(R.id.imgWords);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(MyAnimation.setAnimScaleXY(imgWords)).after(MyAnimation.setAnimScaleXY(imgAmazing));
        animatorSet.start();
    }

    public void openMain(View view) {
        openActivity(MainActivity.class, false);
    }
}
