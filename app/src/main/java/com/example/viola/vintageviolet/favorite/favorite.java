package com.example.viola.vintageviolet.favorite;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
interface favorite {

    @Insert
    void insert(Style results);

    @Query("DELETE FROM favorites_table")
    void deleteAllFavorites();

    @Query("SELECT * FROM favorites_table ORDER BY id DESC")
    LiveData<List<Style>> getAllFavorites();

    @Query("DELETE FROM favorites_table WHERE id = :id")
    void deleteThisMovie(int id);

    @Query("SELECT COUNT(id) FROM favorites_table WHERE id = :id")
    Integer ifExists(int id);


}
