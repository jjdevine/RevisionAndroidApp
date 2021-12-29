package com.jonathandevinesoftware.revisionapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

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
