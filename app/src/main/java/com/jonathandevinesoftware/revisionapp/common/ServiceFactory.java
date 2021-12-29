package com.jonathandevinesoftware.revisionapp.common;

import androidx.room.Room;

import com.jonathandevinesoftware.revisionapp.database.RevisionDatabase;
import com.jonathandevinesoftware.revisionapp.dropbox.DropboxService;
import com.jonathandevinesoftware.revisionapp.dropbox.DropboxServiceImpl;

public class ServiceFactory {

    private static DropboxService dropboxService;

    private static RevisionDatabase revisionDatabase;

    public static DropboxService getDropboxService() {
        if (dropboxService == null) {
            dropboxService = new DropboxServiceImpl();
        }
        return dropboxService;
    }

    public static RevisionDatabase getRevisionDatabase() {
        if(revisionDatabase == null) {
            revisionDatabase = Room.databaseBuilder(
                    App.getContext(),
                    RevisionDatabase.class,
                    "revision-db"
            ).fallbackToDestructiveMigration().build();
        }
        return revisionDatabase;
    }

}
