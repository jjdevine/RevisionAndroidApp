package com.jonathandevinesoftware.revisionapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(primaryKeys = {"topic", "name"})
public class QAFlashCardSetting implements Serializable {

    @ColumnInfo(name = "topic")
    @NonNull
    private String topic;

    @ColumnInfo(name = "name")
    @NonNull
    private String name;

    @ColumnInfo(name = "value")
    @NonNull
    private String value;

    public QAFlashCardSetting(String topic, String name, String value) {
        this.topic = topic;
        this.name = name;
        this.value = value;
    }

    @NonNull
    public String getTopic() {
        return topic;
    }

    public void setTopic(@NonNull String topic) {
        this.topic = topic;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getValue() {
        return value;
    }

    public void setValue(@NonNull String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "QAFlashCardSetting{" +
                "topic='" + topic + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
