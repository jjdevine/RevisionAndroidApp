package com.jonathandevinesoftware.revisionapp.fileio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileAccessor {

    public static List<String> getFlashCardTitles() {

        List<String> titles = new ArrayList<>();
        Collections.addAll(titles, "Paragraph Type 1", "Paragraph Type 2");

        return titles;
    }

    public static List<String> getQAFlashCardTitles() {

        List<String> titles = new ArrayList<>();
        Collections.addAll(titles, "QA Type 1", "QA Type 2");

        return titles;
    }

}
