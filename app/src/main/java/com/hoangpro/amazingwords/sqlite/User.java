package com.hoangpro.amazingwords.sqlite;

import android.content.Context;
import android.content.SharedPreferences;

import com.hoangpro.amazingwords.model.Account;
import com.hoangpro.amazingwords.morefunc.MySession;

import static com.hoangpro.amazingwords.morefunc.MySession.currentAccount;

public class User {

    private static final String shareFreName = "UserData";
    public static void loadUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareFreName, Context.MODE_PRIVATE);
        int coin = sharedPreferences.getInt("coin", 5000);
        int timeCount = sharedPreferences.getInt("time", 30);
        int lvS = sharedPreferences.getInt("lvS", 1);
        int lvF = sharedPreferences.getInt("lvF", 1);
        String idFacebook = sharedPreferences.getString("id", "");
        String name = sharedPreferences.getString("name", "");
        String email=sharedPreferences.getString("email", "");
        if (timeCount == 0) {
            timeCount = 30;
        }
        currentAccount = new Account(idFacebook, name, email, coin, lvS, lvF, timeCount);
    }

    public static void writeUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareFreName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", currentAccount.idFacebook);
        editor.putString("name",currentAccount.name);
        editor.putString("email", currentAccount.email);
        editor.putInt("coin", currentAccount.coin);
        editor.putInt("lvS",currentAccount.lvSortWord);
        editor.putInt("lvF", currentAccount.lvFindWord);
        editor.putInt("time", currentAccount.timeCount);
        editor.apply();
    }


}
