package com.hoangpro.amazingwords.sqlite;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Random;

public class User {
    public static int coin;
    public static int timeCount;
    public static int lv;
    public static String wordNameCurrent;
    private static final String shareFreName="UserData";
    public static boolean isNextLv;
    public static void loadUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareFreName, Context.MODE_PRIVATE);
        if (sharedPreferences!=null){
            coin=sharedPreferences.getInt("coin", 10000000);
            timeCount=sharedPreferences.getInt("time", 30);
            lv =sharedPreferences.getInt("lv", 1);
            wordNameCurrent = sharedPreferences.getString("wordCurrent", wordNameCurrent);
            isNextLv=sharedPreferences.getBoolean("isNextLv",true);
            if (timeCount==0){
                timeCount=30;
            }
            if (wordNameCurrent==null){
                isNextLv=true;
            }
        }
    }

    public static void writeUser(Context context,int timeSet,int coinSet,int lvSet,String wordCurrentSet,boolean isNextLvSet){
        SharedPreferences sharedPreferences=context.getSharedPreferences(shareFreName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        coin=coinSet;
        timeCount=timeSet;
        lv=lvSet;
        wordNameCurrent=wordCurrentSet;
        editor.putInt("time",timeSet);
        editor.putInt("coin", coinSet);
        editor.putInt("lv", lvSet);
        editor.putString("wordCurrent", wordCurrentSet);
        editor.putBoolean("isNextLv", isNextLvSet);
        editor.apply();
    }

}
