package com.example.viola.vintageviolet.favorite;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class favoriteViewModel {
    private FavoriteRep FavRep;
    private LiveData<List<Style>> allFavorites;

    public favoriteViewModel( Application application) {
        FavRep = new FavoriteRep(application);
        allFavorites = FavRep.getAllFavorites();
    }
    public LiveData<List<Style>> getAllFavorites() {
        return allFavorites;
    }

    public void insert(Style MovRes) {
        FavRep.insert(MovRes);
    }

    public void deleteAllFavorites() {
        FavRep.deleteAllFavorites();
    }
}
