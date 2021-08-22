package com.jonathandevinesoftware.revisionapp.qaflashcardrevision;

import com.jonathandevinesoftware.revisionapp.database.QAFlashCard;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCardSetting;

import java.io.Serializable;
import java.util.List;

public class QAFlashCardsWrapper implements Serializable {

    private String topic;
    private List<QAFlashCard> qaFlashCardList;
    private List<QAFlashCardSetting> qaFlashCardSettingList;

    public QAFlashCardsWrapper(String topic, List<QAFlashCard> qaFlashCardList, List<QAFlashCardSetting> qaFlashCardSettingList) {
        this.topic = topic;
        this.qaFlashCardList = qaFlashCardList;
        this.qaFlashCardSettingList = qaFlashCardSettingList;
    }

    public String getTopic() {
        return topic;
    }

    public List<QAFlashCard> getQaFlashCardList() {
        return qaFlashCardList;
    }

    public List<QAFlashCardSetting> getQaFlashCardSettingList() {
        return qaFlashCardSettingList;
    }
}
