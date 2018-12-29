package com.jonathandevinesoftware.revisionapp.qaflashcardrevision;

import com.jonathandevinesoftware.revisionapp.common.App;
import com.jonathandevinesoftware.revisionapp.common.SharedPreferenceService;
import com.jonathandevinesoftware.revisionapp.database.RevisionDatabase;

public class QAFlashCardSettingsService {

    public static final String QAFLASH_CARD_SETTINGS_SERVICE_REVEAL_TYPE = "QAFlashCardSettingsService.RevealType";

    enum RevealType { QUESTION_FIRST, ANSWER_FIRST, ALTERNATE }

    public static void setRevealType(RevealType revealType) {
        SharedPreferenceService.getSharedPreferences().edit()
                .putString(QAFLASH_CARD_SETTINGS_SERVICE_REVEAL_TYPE, revealType.toString()).apply();
    }

    public static RevealType getRevealType() {
        String revealTypeStr = SharedPreferenceService.getSharedPreferences()
                .getString(QAFLASH_CARD_SETTINGS_SERVICE_REVEAL_TYPE, "none");

        System.out.println("Shared preferences reveal type is " + revealTypeStr);

        for (RevealType revealType : RevealType.values()) {
            if(revealType.toString().equals(revealTypeStr)) {
                return revealType;
            }
        }

        //default to QUESTION FIRST
        return  RevealType.QUESTION_FIRST;
    }
}
