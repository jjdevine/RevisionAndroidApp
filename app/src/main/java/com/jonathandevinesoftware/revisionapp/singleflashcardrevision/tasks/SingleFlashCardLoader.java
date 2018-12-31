package com.jonathandevinesoftware.revisionapp.singleflashcardrevision.tasks;

import android.os.AsyncTask;

import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;
import com.jonathandevinesoftware.revisionapp.database.SingleFlashCard;
import com.jonathandevinesoftware.revisionapp.database.SingleFlashCardDAO;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SingleFlashCardLoader extends AsyncTask<String, Integer, Optional<SingleFlashCard>> {

    private Consumer<Optional<SingleFlashCard>> callbackConsumer;

    public SingleFlashCardLoader(Consumer<Optional<SingleFlashCard>> callbackConsumer) {
        this.callbackConsumer = callbackConsumer;
    }

    @Override
    protected Optional<SingleFlashCard> doInBackground(String... strings) {
        if(strings.length != 2) {
            throw new IllegalArgumentException("Argument list must be of length 2");
        }
        String topic = strings[0];
        String fileName = strings[1];

        SingleFlashCardDAO dao = ServiceFactory.getRevisionDatabase().singleFlashCardDAO();

        Optional<SingleFlashCard> flashCard = Optional.of(dao.getByTopicAndFileName(topic, fileName));

        if(!flashCard.isPresent() ||
                (flashCard.isPresent() && flashCard.get().getMainText() == null)) {

            boolean update = flashCard.isPresent();

            //load from DropBox
            flashCard = ServiceFactory.getDropboxService().getSingleFlashCard(topic, fileName);

            //save to local DB
            flashCard.ifPresent(card -> {
                if(update) {
                    dao.update(card);
                } else {
                    dao.insert(card);
                }
            });
        }

        return flashCard;
    }

    @Override
    protected void onPostExecute(Optional<SingleFlashCard> flashCard) {
        callbackConsumer.accept(flashCard);
    }
}
