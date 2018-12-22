package com.jonathandevinesoftware.revisionapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class RevisionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision);

        Toast.makeText(
                getApplicationContext(),
                getNextRevisionPair().orElse(new String[]{"default text"})[0],
                Toast.LENGTH_SHORT);
    }

    private Optional<String[]> getNextRevisionPair() {
        File f = new File(".");
        try {
            System.out.println(f.getCanonicalPath());
            Arrays.asList(f.listFiles()).forEach(fl -> {
                    try{
                        System.out.println(fl.getCanonicalPath());
                     }catch
                    (Exception ex) {}
             });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        File revisionFile = new File("/storage/self/Revision/FrenchVerbs.txt");

        try {
            List<String> list = Files.lines(revisionFile.toPath())
                    .filter(line -> line.contains("="))
                    .collect(Collectors.<String>toList());

            return Optional.empty().of(randomListItem(list).split("="));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

    private <T> T randomListItem(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }



}
