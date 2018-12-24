package com.jonathandevinesoftware.revisionapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface QAFlashCardDAO {

    @Insert
    void insert(QAFlashCard... qaFlashCards);

    @Delete
    void delete(QAFlashCard qaFlashCard);

    @Query("SELECT * FROM QAFlashCard WHERE topic = :topic")
    List<QAFlashCard> getAllByTopic(String topic);
}
