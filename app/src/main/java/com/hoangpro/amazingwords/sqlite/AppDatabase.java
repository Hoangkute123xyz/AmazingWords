package com.hoangpro.amazingwords.sqlite;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Update;

@Database(version = 1,entities = {Word.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract AppDAO getDAO();

    public static AppDatabase getInstance(Context context){
        if (instance==null){
            instance= Room.databaseBuilder(context, AppDatabase.class,"word.db")
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
}
