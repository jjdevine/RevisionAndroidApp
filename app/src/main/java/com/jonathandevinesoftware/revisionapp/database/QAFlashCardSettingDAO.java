package com.jonathandevinesoftware.revisionapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QAFlashCardSettingDAO {

    @Insert
    void insert(QAFlashCardSetting... qaFlashCardSettings);

    @Delete
    void delete(QAFlashCardSetting qaFlashCardSetting);

    @Query("SELECT * FROM QAFlashCardSetting WHERE topic = :topic")
    List<QAFlashCardSetting> getAllByTopic(String topic);

    @Query("DELETE FROM QAFlashCardSetting")
    void deleteAll();
}
