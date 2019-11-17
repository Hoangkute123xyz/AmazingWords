package com.hoangpro.amazingwords.sqlite;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AppDAO {
    @Query("select * from Word where isAnswer==0 and length(wordName)<6 order by random() limit 1")
    Word getRandomWord();

    @Update
    void updateWord(Word word);

    @Query("Select * from Word where wordName = :wordName limit 1")
    Word getWordByWordName(String wordName);
}
