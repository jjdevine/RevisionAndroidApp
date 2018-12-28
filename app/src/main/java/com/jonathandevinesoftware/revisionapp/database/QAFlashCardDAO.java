package com.jonathandevinesoftware.revisionapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
public interface QAFlashCardDAO {

    @Insert
    void insert(QAFlashCard... qaFlashCards);

    @Update
    void update(QAFlashCard... qaFlashCards);

    @Delete
    void delete(QAFlashCard qaFlashCard);

    @Query("SELECT * FROM QAFlashCard WHERE topic = :topic")
    List<QAFlashCard> getAllByTopic(String topic);

}
