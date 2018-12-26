package com.jonathandevinesoftware.revisionapp.qaflashcardrevision;

import com.jonathandevinesoftware.revisionapp.database.QAFlashCard;

import java.io.Serializable;
import java.util.List;

public class QAFlashCardsWrapper implements Serializable {

    private String topic;
    private List<QAFlashCard> qaFlashCardList;

    public QAFlashCardsWrapper(String topic, List<QAFlashCard> qaFlashCardList) {
        this.topic = topic;
        this.qaFlashCardList = qaFlashCardList;
    }

    public String getTopic() {
        return topic;
    }

    public List<QAFlashCard> getQaFlashCardList() {
        return qaFlashCardList;
    }
}
