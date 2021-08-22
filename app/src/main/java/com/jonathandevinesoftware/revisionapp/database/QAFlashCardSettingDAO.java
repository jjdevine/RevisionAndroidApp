package com.jonathandevinesoftware.revisionapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

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
