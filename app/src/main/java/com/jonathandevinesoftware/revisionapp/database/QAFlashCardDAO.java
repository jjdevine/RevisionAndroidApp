package com.jonathandevinesoftware.revisionapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;


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

    @Query("SELECT DISTINCT topic FROM QAFlashCard ORDER BY topic")
    List<String> getAllTopics();

    @Query("DELETE FROM QAFlashCard")
    void deleteAll();
}
