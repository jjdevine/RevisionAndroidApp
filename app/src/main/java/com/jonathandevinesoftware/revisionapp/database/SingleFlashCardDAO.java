package com.jonathandevinesoftware.revisionapp.database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

public interface SingleFlashCardDAO {

    @Insert
    void insert(SingleFlashCard... singleFlashCards);

    @Update
    void update(SingleFlashCard... singleFlashCards);

    @Delete
    void delete(SingleFlashCard singleFlashCards);
}
