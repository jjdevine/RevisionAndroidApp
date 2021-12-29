package com.jonathandevinesoftware.revisionapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

@Entity(primaryKeys = {"topic", "fileName"})
public class SingleFlashCard {

    @ColumnInfo(name = "topic")
    @NonNull
    private String topic;

    @ColumnInfo(name = "fileName")
    @NonNull
    private String fileName;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "mainText")
    private String mainText;

    public SingleFlashCard(String topic, String fileName, String title, String mainText) {
        this.topic = topic;
        this.fileName = fileName;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "SingleFlashCard{" +
                "topic='" + topic + '\'' +
                ", fileName='" + fileName + '\'' +
                ", title='" + title + '\'' +
                ", mainText='" + mainText + '\'' +
                '}';
    }
}
