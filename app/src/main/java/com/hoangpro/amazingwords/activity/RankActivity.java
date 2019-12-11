package com.hoangpro.amazingwords.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.adapter.RankingUserAdapter;
import com.hoangpro.amazingwords.base.BaseActivity;
import com.hoangpro.amazingwords.model.Account;
import com.hoangpro.amazingwords.morefunc.CircleImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.hoangpro.amazingwords.morefunc.MyAnimation.setAnimScaleXY;
import static com.hoangpro.amazingwords.morefunc.MySession.currentAccount;

public class RankActivity extends BaseActivity {

    private static final String TAG = "RankActivity";
    private LinearLayout lnAccountInfo;
    private TextView tvCoin;
    private RecyclerView rvRank;
    RankingUserAdapter adapter;
    private ImageView imgAvatar;
    private TextView tvName;
    private TextView tvRank;
    private List<Account> list;
    private int posAccount;

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
        tvName.setText(currentAccount.name);
        Picasso.get().load("https://graph.facebook.com/" + currentAccount.idFacebook + "/picture?type=large").transform(new CircleImage()).into(imgAvatar);
        tvCoin.setText(String.format("%d %s", currentAccount.coin, getString(R.string.coin)));
        setAnimforView();
    }

    private void getData() {
        list = new ArrayList<>();
        adapter = new RankingUserAdapter(this, list);
        rvRank.setAdapter(adapter);
        rvRank.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("accounts");
        Query query = reference.orderByChild("coin");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Account account = snapshot.getValue(Account.class);
                    list.add(0, account);
                    if (account.idFacebook.equalsIgnoreCase(currentAccount.idFacebook)) {
                        posAccount = list.size();
                    }
                }
                tvRank.setText((list.size() - posAccount + 1) + "");
                if (list.size() > 100) {
                    list = list.subList(0, 99);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Dialog dialog;

    private void showLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(LayoutInflater.from(this).inflate(R.layout.dialog_loading, null, false));
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog = alertDialog;
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        actionBack(null);
    }

    private void setAnimforView() {
        AnimatorSet animatorSet = setAnimScaleXY(lnAccountInfo);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                getData();
            }
        });
        animatorSet.start();
    }

    private void initView() {
        lnAccountInfo = findViewById(R.id.lnAccountInfo);
        tvCoin = findViewById(R.id.tvCoin);
        rvRank = findViewById(R.id.rvRank);
        imgAvatar = findViewById(R.id.imgAvatar);
        tvName = findViewById(R.id.tvName);
        tvRank = findViewById(R.id.tvRank);
    }

    public void actionBack(View view) {
        openActivity(MainActivity.class, true);
        overridePendingTransition(0, 0);
    }
}
