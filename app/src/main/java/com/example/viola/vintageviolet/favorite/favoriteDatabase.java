package com.example.viola.vintageviolet.favorite;


import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

public abstract class favoriteDatabase extends RoomDatabase {
    private static favoriteDatabase instance;
    public static favoriteDatabase getInstance(Context context) {

        if (instance == null) {
            synchronized (favoriteDatabase.class) {

                instance = Room.databaseBuilder(context.getApplicationContext(),
                        favoriteDatabase.class, "favorites_database.db")
                        .fallbackToDestructiveMigration()
                        .build();
            }}
        return instance;
    }

    public abstract favorite favoritesDao();
}
