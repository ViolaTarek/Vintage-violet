package com.example.viola.vintageviolet.favorite;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FavoriteRep {
    favorite favoriteDao;
    private LiveData<List<Style>> allFavorites;

    public FavoriteRep(Application application) {
        favoriteDatabase database = favoriteDatabase.getInstance(application);
        favoriteDao = database.favoritesDao();
        allFavorites = favoriteDao.getAllFavorites();

    }

    public LiveData<List<Style>> getAllFavorites() {
        return allFavorites;
    }

    public void insert(Style fav) {
        new InsertFavoritesAsyncTask(favoriteDao).execute(fav);

    }
    private static class InsertFavoritesAsyncTask extends AsyncTask<Style, Void, Void> {
        private favorite AsyncDao;


        private InsertFavoritesAsyncTask(favorite Fav) {
            this.AsyncDao = Fav;
        }

        @Override
        protected Void doInBackground(final Style... movieResults) {
            AsyncDao.insert(movieResults[0]);
            return null;
        }
    }


    public void deleteAllFavorites() {
        new DeleteAllFavoritesAsyncTask(favoriteDao).execute();
    }

    private static class DeleteAllFavoritesAsyncTask extends AsyncTask<Void, Void, Void> {
        private favorite mAsyncDao;

        private DeleteAllFavoritesAsyncTask(favorite favv) {
            mAsyncDao = favv;
        }

        @Override
        protected Void doInBackground(Void... Voids) {
            mAsyncDao.deleteAllFavorites();
            return null;
        }
    }
}
