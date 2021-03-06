package com.example.viola.vintageviolet.activities;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.viola.vintageviolet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import io.paperdb.Paper;

public class styleDetailsActivity extends AppCompatActivity {
    ImageView iv;
    TextView desc;

    FloatingActionButton favorite;

    int id;
    String url;
    String description;
    String userId;
    String season;
    String category;
    DatabaseReference mDatabase;
    CoordinatorLayout linearLayout;
    Boolean isExist = false;
    TextView seasonTV;
    TextView categTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Paper.init(this);

        linearLayout = findViewById(R.id.linear_layout);
        iv = findViewById(R.id.style_detail_iv);
        desc = findViewById(R.id.description);
        favorite = findViewById(R.id.fab);
        seasonTV = findViewById(R.id.season);
        categTV = findViewById(R.id.category);

        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getExtras().getString("userId");
            url = intent.getExtras().getString("url");
            id = intent.getExtras().getInt("id");
            season = intent.getExtras().getString("season");
            category = intent.getExtras().getString("category");
            description = intent.getExtras().getString("desc");
            desc.setText(description);
            categTV.setText(category);
            seasonTV.setText(season);
            Glide.with(this).load(url).into(iv);
        }
        initializeFab();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != null) {

                    styleIsExist(new StyleFetchingListener() {
                        @Override
                        public void onStyleFound() {
                            DatabaseReference childRef = mDatabase.child("usersFavorite").child(userId).child(String.valueOf(id));
                            childRef.removeValue();
                            favorite.setImageResource(R.drawable.ic_favorite);
                            Snackbar snackbar = Snackbar
                                    .make(linearLayout, R.string.removed_from_fav, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                        @Override
                        public void onStyleNotFound() {
                            DatabaseReference childRef = mDatabase.child("usersFavorite").child(userId).child(String.valueOf(id));
                            childRef.child("url").setValue(url);
                            childRef.child("id").setValue(id);
                            childRef.child("desc").setValue(description);
                            childRef.child("category").setValue(category);
                            childRef.child("season").setValue(season);
                            favorite.setImageResource(R.drawable.ic_favorite_filled);
                            Snackbar snackbar = Snackbar
                                    .make(linearLayout, R.string.added_to_fav, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });

                } else {
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, R.string.sign_in_toast, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

                Paper.book().write("desc", description);
                Paper.book().write("url", url);
                Snackbar snackbar = Snackbar
                        .make(linearLayout, R.string.image_save_to_fav, Snackbar.LENGTH_LONG);
                snackbar.show();
                return true;
            case (R.id.home):
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void initializeFab() {
        if (userId != null) {
            DatabaseReference root = FirebaseDatabase.getInstance().getReference();
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("usersFavorite").child(userId).hasChild(String.valueOf(id))) {
                        favorite.setImageResource(R.drawable.ic_favorite_filled);
                    } else {
                        favorite.setImageResource(R.drawable.ic_favorite);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public boolean styleIsExist(final StyleFetchingListener listener) {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child("usersFavorite").child(userId).hasChild(String.valueOf(id))) {
                    listener.onStyleFound();
                } else {
                    listener.onStyleNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return isExist;
    }

    interface StyleFetchingListener {
        void onStyleFound();

        void onStyleNotFound();
    }
}