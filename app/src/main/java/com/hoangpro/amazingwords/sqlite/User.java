package com.hoangpro.amazingwords.sqlite;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    public static int coin;
    public static int timeCount;
    public static int lvSortWord;
    public static int lvFindWord;
    public static String wordNameCurrent;
    private static final String shareFreName = "UserData";
    public static boolean isNextLv;

    public static void loadUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareFreName, Context.MODE_PRIVATE);
        coin = sharedPreferences.getInt("coin", 5000);
        timeCount = sharedPreferences.getInt("time", 30);
        lvSortWord = sharedPreferences.getInt("lvS", 1);
        wordNameCurrent = sharedPreferences.getString("wordCurrent", wordNameCurrent);
        isNextLv = sharedPreferences.getBoolean("isNextLv", true);
        if (timeCount == 0) {
            timeCount = 30;
        }
        if (wordNameCurrent == null) {
            isNextLv = true;
        }
    }

    public static void writeUser(Context context, int timeSet, int coinSet, int lvSet, String wordCurrentSet, boolean isNextLvSet) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareFreName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        coin = coinSet;
        timeCount = timeSet;
        lvSortWord = lvSet;
        wordNameCurrent = wordCurrentSet;
        isNextLv = isNextLvSet;
        editor.putInt("time", timeSet);
        editor.putInt("coin", coinSet);
        editor.putInt("lvS", lvSet);
        editor.putString("wordCurrent", wordCurrentSet);
        editor.putBoolean("isNextLv", isNextLvSet);
        editor.apply();
    }

    public static void setLvFindWord(Context context, int lvFindWordSetter) {
        lvFindWord = lvFindWordSetter;
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareFreName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lvF", lvFindWordSetter);
        editor.apply();
    }

    public static int getLvFindWord(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareFreName, Context.MODE_PRIVATE);
        lvFindWord=sharedPreferences.getInt("lvF", 1);
        return lvFindWord;
    }

    public static void setCoin(Context context,int coinSetter){
        coin = coinSetter;
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareFreName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("coin", coinSetter);
        editor.apply();
    }


}
