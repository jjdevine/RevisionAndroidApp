package com.jonathandevinesoftware.revisionapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SingleFlashCardDAO {

    @Insert
    void insert(SingleFlashCard... singleFlashCards);

    @Update
    void update(SingleFlashCard... singleFlashCards);

    @Delete
    void delete(SingleFlashCard singleFlashCards);

    @Query("SELECT fileName FROM SingleFlashCard WHERE topic = :topic")
    List<String> getFileNames(String topic);

    @Query("SELECT * FROM SingleFlashCard WHERE topic = :topic AND fileName = :fileName")
    SingleFlashCard getByTopicAndFileName(String topic, String fileName);
}
