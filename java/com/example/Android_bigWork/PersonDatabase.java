package com.example.Android_bigWork;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PersonEntity.class}, version = 2, exportSchema = false)
public abstract class PersonDatabase extends RoomDatabase {
    private static final String DB_NAME = "person.db";
    private static PersonDatabase INSTANCE;

    static synchronized PersonDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PersonDatabase.class,
                            DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public abstract PersonDao getPersonDao();

}


