package com.jonathandevinesoftware.revisionapp.dropbox;

import java.util.Map;

public class QAFlashCardSourceData {

    private Map<String, String> flashCards;
    private Map<String, String> settings;

    public Map<String, String> getFlashCards() {
        return flashCards;
    }

    public void setFlashCards(Map<String, String> flashCards) {
        this.flashCards = flashCards;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }
}
