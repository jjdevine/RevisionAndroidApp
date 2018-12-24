package com.jonathandevinesoftware.revisionapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class QAFlashCard {

    @PrimaryKey
    @ColumnInfo(name = "question")
    @NonNull
    public String question;

    @ColumnInfo(name = "answer")
    @NonNull
    public String answer;

    @ColumnInfo(name = "topic")
    @NonNull
    public String topic;
}
