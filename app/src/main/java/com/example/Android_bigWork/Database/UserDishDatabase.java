package com.example.Android_bigWork.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.Android_bigWork.Entity.UserDish;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {UserDish.class}, version = 3, exportSchema = false)
public abstract class UserDishDatabase extends RoomDatabase {

    public abstract UserDishDao userDishDao();

    private static volatile UserDishDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static UserDishDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserDishDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UserDishDatabase.class,
                                    "order_table")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}