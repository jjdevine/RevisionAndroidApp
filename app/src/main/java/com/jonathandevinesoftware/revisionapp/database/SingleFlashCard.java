package com.jonathandevinesoftware.revisionapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(primaryKeys = {"topic", "fileName"})
public class SingleFlashCard {

    @ColumnInfo(name = "topic")
    @NonNull
    private String topic;

    @ColumnInfo(name = "fileName")
    @NonNull
    private String fileName;

    @ColumnInfo(name = "maintText")
    @NonNull
    private String mainText;

    public SingleFlashCard(String topic, String fileName, String mainText) {
        this.topic = topic;
        this.fileName = fileName;
        this.mainText = mainText;
    }

    private SingleFlashCard() {}

    @NonNull
    public String getTopic() {
        return topic;
    }

    public void setTopic(@NonNull String topic) {
        this.topic = topic;
    }

    @NonNull
    public String getFileName() {
        return fileName;
    }

    public void setFileName(@NonNull String fileName) {
        this.fileName = fileName;
    }

    @NonNull
    public String getMainText() {
        return mainText;
    }

    public void setMainText(@NonNull String mainText) {
        this.mainText = mainText;
    }

    @Override
    public String toString() {
        return "SingleFlashCard{" +
                "topic='" + topic + '\'' +
                ", fileName='" + fileName + '\'' +
                ", mainText='" + mainText + '\'' +
                '}';
    }
}
