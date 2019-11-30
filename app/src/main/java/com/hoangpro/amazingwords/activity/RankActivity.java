package com.hoangpro.amazingwords.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.adapter.RankingUserAdapter;
import com.hoangpro.amazingwords.base.BaseActivity;
import com.hoangpro.amazingwords.model.Top;

import java.util.ArrayList;
import java.util.List;

import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimScaleXY;

public class RankActivity extends BaseActivity {

    private LinearLayout lnAccountInfo;
    private TextView tvCoin;
    private RecyclerView rvRank;
    RankingUserAdapter adapter;

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
        setContentView(R.layout.activity_rank);
        initView();

        AnimatorSet animatorSet = setAnimScaleXY(lnAccountInfo);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                List<Top> list = new ArrayList<>();
                list.add(new Top(R.drawable.gate, "Bill Gate", 23000));
                list.add(new Top(R.drawable.mark, "Mark Zuckerberg", 17300));
                list.add(new Top(R.drawable.trump, "Donald Trump", 10200));
                adapter = new RankingUserAdapter(RankActivity.this, list);
                rvRank.setAdapter(adapter);
                rvRank.setLayoutManager(new LinearLayoutManager(RankActivity.this));
            }
        });
        animatorSet.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        actionBack(null);
    }

    private void initView() {
        lnAccountInfo = findViewById(R.id.lnAccountInfo);
        tvCoin = findViewById(R.id.tvCoin);
        rvRank = findViewById(R.id.rvRank);
    }

    public void actionBack(View view) {
        openActivity(MainActivity.class, true);
        overridePendingTransition(0, 0);
    }
}
