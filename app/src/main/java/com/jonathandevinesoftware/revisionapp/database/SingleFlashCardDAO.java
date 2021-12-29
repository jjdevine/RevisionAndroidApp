package com.jonathandevinesoftware.revisionapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT fileName FROM SingleFlashCard WHERE topic = :topic AND mainText IS NOT NULL")
    List<String> getFileNamesForCompleteFlashCards(String topic);

    @Query("SELECT * FROM SingleFlashCard WHERE topic = :topic AND fileName = :fileName")
    SingleFlashCard getByTopicAndFileName(String topic, String fileName);

    @Query("SELECT DISTINCT topic FROM SingleFlashCard")
    List<String> getTopics();

    @Query("DELETE FROM SingleFlashCard")
    void deleteAll();

}
