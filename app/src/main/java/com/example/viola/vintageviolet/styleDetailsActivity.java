package com.example.viola.vintageviolet;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.CheckBoxPreference;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.paperdb.Paper;

public class styleDetailsActivity extends AppCompatActivity {
    ImageView iv;
    TextView desc;

    FloatingActionButton favorite;

    int id;
    String url;
    String description;
    String userId;
    DatabaseReference mDatabase;
    LinearLayout linearLayout;
    Boolean isExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout = findViewById(R.id.linear_layout);
        iv = findViewById(R.id.style_detail_iv);
        desc = findViewById(R.id.description);
        favorite = findViewById(R.id.fab);

        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getExtras().getString("userId");
            url = intent.getExtras().getString("url");
            id = intent.getExtras().getInt("id");
            description = intent.getExtras().getString("desc");
            desc.setText(description);
            Glide.with(this).load(url).into(iv);
        }
        initializeFab();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != null) {
                    if (!styleIsExist()) {
                        DatabaseReference childRef = mDatabase.child("usersFavorite").child(userId).child(String.valueOf(id));
                        childRef.child("url").setValue(url);
                        childRef.child("id").setValue(id);
                        childRef.child("desc").setValue(description);
                        favorite.setImageResource(R.drawable.ic_favorite_filled);
                        Snackbar snackbar = Snackbar
                                .make(linearLayout, "Added to favorite", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        DatabaseReference childRef = mDatabase.child("usersFavorite").child(userId).child(String.valueOf(id));
                        childRef.removeValue();
                        favorite.setImageResource(R.drawable.ic_favorite);
                        Snackbar snackbar = Snackbar
                                .make(linearLayout, "Removed from favorite", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Sign in First to add to your styles", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case (R.id.share):
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setText(url)
                        .getIntent(), "Share"));
                return true;
            case (R.id.save):
                /*Bitmap bitmap = getBitmapFromURL(url);
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "id", "");  // Saves the image.
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "Image saved to gallery", Snackbar.LENGTH_LONG);
                snackbar.show();*/
                Paper.book().write("desc",description);
                Paper.book().write("url",url);

                return true;
            case (R.id.home):
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            // Log exception
            return null;
        }
    }

    public void initializeFab() {
        if (userId != null) {
            DatabaseReference root = FirebaseDatabase.getInstance().getReference();
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.child("usersFavorite").child(userId).hasChild(String.valueOf(id))) {
                        favorite.setImageResource(R.drawable.ic_favorite_filled);
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(linearLayout, "photo with id = " + id + " does not exist", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        favorite.setImageResource(R.drawable.ic_favorite);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public boolean styleIsExist() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child("usersFavorite").child(userId).hasChild(String.valueOf(id))) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return isExist;
    }
}
