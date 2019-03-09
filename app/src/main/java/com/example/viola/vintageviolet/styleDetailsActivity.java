package com.example.viola.vintageviolet;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.viola.vintageviolet.favorite.Style;
import com.example.viola.vintageviolet.favorite.favoriteDatabase;
import com.example.viola.vintageviolet.favorite.favoriteViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class styleDetailsActivity extends AppCompatActivity {
    ImageView iv;
    TextView desc;
    String url;
    boolean checkMovieInFavorites;
    FloatingActionButton favorite;
    private favoriteDatabase mDatabase;
    private favoriteViewModel mFavVMod;
    private Style CurrentStyle;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_details);

        iv = findViewById(R.id.style_detail_iv);
        desc = findViewById(R.id.description);
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getExtras().getString("url");
            id = intent.getExtras().getInt("id");
            desc.setText(intent.getExtras().getString("desc"));
            GlideApp.with(this).load(url).into(iv);
        }
        CurrentStyle = new Style(id, url, intent.getExtras().getString("desc"));

//        mDatabase = favoriteDatabase.getInstance(getApplicationContext());
/*
        mFavVMod= ViewModelProviders.of(this).get(favoriteViewModel.class);
*/
//
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        int id = item.getItemId();
        switch (id) {
            case (R.id.share):
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setText(url)
                        .getIntent(), "Share"));
                break;
            case (R.id.save):
                BitmapDrawable draw = (BitmapDrawable) iv.getDrawable();
                Bitmap bitmap = draw.getBitmap();

                FileOutputStream outStream = null;
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Camera");
                dir.mkdirs();
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);
                try {
                    outStream = new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "Image saved to gallery", Snackbar.LENGTH_LONG);
                snackbar.show();
                break;


        }
        return true;
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
