package com.jonathandevinesoftware.revisionapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

@Entity(primaryKeys = {"topic", "question"})
public class QAFlashCard implements Serializable {

    @ColumnInfo(name = "question")
    @NonNull
    private String question;

    @ColumnInfo(name = "answer")
    @NonNull
    private String answer;

    @ColumnInfo(name = "topic")
    @NonNull
    private String topic;

    @ColumnInfo(name = "favourite")
    private boolean favourite;

    public QAFlashCard(String topic, String question, String answer, boolean favourite) {
        this.topic = topic;
        this.question = question;
        this.answer = answer;
        this.favourite = favourite;
    }

    private QAFlashCard() {

    }

    @NonNull
    public String getQuestion() {
        return question;
    }

    public void setQuestion(@NonNull String question) {
        this.question = question;
    }

    @NonNull
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(@NonNull String answer) {
        this.answer = answer;
    }

    @NonNull
    public String getTopic() {
        return topic;
    }

    public void setTopic(@NonNull String topic) {
        this.topic = topic;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    @Override
    public String toString() {
        return "QAFlashCard{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", topic='" + topic + '\'' +
                ", favourite=" + favourite +
                '}';
    }
}
