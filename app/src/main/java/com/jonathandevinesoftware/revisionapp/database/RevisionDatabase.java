package com.jonathandevinesoftware.revisionapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(
        entities = {
                QAFlashCard.class,
                SingleFlashCard.class
        }
        , version = 5)
public abstract class RevisionDatabase extends RoomDatabase {
    public abstract QAFlashCardDAO qaFlashCardDAO();

    public abstract SingleFlashCardDAO singleFlashCardDAO();


}
