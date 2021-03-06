package com.example.viola.vintageviolet.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.viola.vintageviolet.R;
import com.example.viola.vintageviolet.models.mainStyles;
import com.example.viola.vintageviolet.activities.styleDetailsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FavoriteFragment extends Fragment {

    String user_id;
    RecyclerView mRecyclerView;
    DatabaseReference mChilddatabaseRef;
    DatabaseReference mDatabase;
    LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_styles, container, false);
        linearLayout = root.findViewById(R.id.linear_styles_layout);
        Bundle bundle = getArguments();
        if (bundle != null) {
            user_id = bundle.getString("userid");
        }
        mRecyclerView = root.findViewById(R.id.styles_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (user_id != null) {
            mChilddatabaseRef = mDatabase.child("usersFavorite").child(user_id);

        } else {
            Snackbar snackbar = Snackbar
                    .make(linearLayout, R.string.sign_in_to_open_styles, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (user_id != null) {
            FirebaseRecyclerAdapter<mainStyles, FavoriteFragment.favoriteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<mainStyles, FavoriteFragment.favoriteViewHolder>
                    (mainStyles.class, R.layout.style_list_item, favoriteViewHolder.class, mChilddatabaseRef) {

                @Override
                public FavoriteFragment.favoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return super.onCreateViewHolder(parent, viewType);
                }

                @Override
                protected void populateViewHolder(favoriteViewHolder viewHolder, final mainStyles model, int position) {
                    viewHolder.setImage(getContext(), model.getUrl());
                    viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", model.getId());
                            bundle.putString("url", model.getUrl());
                            bundle.putString("desc", model.getDesc());
                            bundle.putString("category", model.getCategory());
                            bundle.putString("season", model.getSeason());
                            bundle.putString("userId", user_id);
                            Intent mIntent = new Intent(getContext(), styleDetailsActivity.class);
                            mIntent.putExtras(bundle);
                            startActivity(mIntent);
                        }
                    });
                }
            };
            mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        }
    }


    public static class favoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView style_iv;
        View mview;

        public favoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;

        }

        public void setImage(Context context, String url) {
            style_iv = mview.findViewById(R.id.styles_fragment_list_item_iv);
            Glide.with(context).load(url).into(style_iv);

        }

    }
}





