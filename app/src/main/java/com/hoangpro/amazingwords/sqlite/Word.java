package com.hoangpro.amazingwords.sqlite;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Word {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String wordName;
    @ColumnInfo
    public int isAnswer;
}
