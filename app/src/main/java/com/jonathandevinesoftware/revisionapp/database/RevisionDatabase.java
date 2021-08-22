package com.jonathandevinesoftware.revisionapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(
        entities = {
                QAFlashCard.class,
                QAFlashCardSetting.class,
                SingleFlashCard.class
        }
        , version = 6)
public abstract class RevisionDatabase extends RoomDatabase {
    public abstract QAFlashCardDAO qaFlashCardDAO();

    public abstract  QAFlashCardSettingDAO qaFlashCardSettingDAO();

    public abstract SingleFlashCardDAO singleFlashCardDAO();




}
