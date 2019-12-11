package com.hoangpro.amazingwords.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hoangpro.amazingwords.R;
import com.hoangpro.amazingwords.base.BaseActivity;
import com.hoangpro.amazingwords.morefunc.MySession;
import com.hoangpro.amazingwords.sqlite.User;

import static com.hoangpro.amazingwords.morefunc.MySession.currentAccount;
import static com.hoangpro.amazingwords.morefunc.MySession.updateAccount;

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
        User.loadUser(this);
//        Log.e("idFacebook", currentAccount.idFacebook);
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

    public void actionLogout(View view) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.logout_confirm)).setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                openActivity(LoginActivity.class, true);
                overridePendingTransition(0, 0);
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
