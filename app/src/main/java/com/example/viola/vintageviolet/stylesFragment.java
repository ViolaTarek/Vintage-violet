package com.example.viola.vintageviolet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class stylesFragment extends Fragment {

    RecyclerView mRecyclerView;
    boolean autumn_flag = false;
    boolean summer_flag = false;
    boolean spring_flag = false;
    boolean winter_flag = false;
    DatabaseReference reference;
    DatabaseReference childReference;
    DatabaseReference autumnReference, winterReference, springReference, summerReference;


    public stylesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_styles, container, false);
        reference = FirebaseDatabase.getInstance().getReference().child("styles");

        autumnReference = reference.child("autumn");
        winterReference = reference.child("winter");
        summerReference = reference.child("summer");
        springReference = reference.child("spring");
        mRecyclerView = RootView.findViewById(R.id.styles_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        Bundle bundle = getArguments();
        if (bundle != null) {
            String myStr = bundle.getString("category");
            if (myStr == "dress") {
                childReference = reference.child("dress");
            } else if (myStr == "casual") {
                childReference = reference.child("casual");
            } else {
                childReference = reference.child("classy");
            }
        }
        return RootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.styles_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case (R.id.autumn_item):
                if (item.isChecked()) {
                    // If item already checked then unchecked it
                    item.setChecked(false);
                    autumn_flag = false;
                } else {
                    // If item is unchecked then checked it
                    item.setChecked(true);
                    autumn_flag = true;
                }
            case (R.id.summer_item):
                if (item.isChecked()) {
                    // If item already checked then unchecked it
                    item.setChecked(false);
                    summer_flag = false;
                } else {
                    // If item is unchecked then checked it
                    item.setChecked(true);
                    summer_flag = true;
                }
                return true;
            case (R.id.spring_item):
                if (item.isChecked()) {
                    // If item already checked then unchecked it
                    item.setChecked(false);
                    spring_flag = false;
                } else {
                    // If item is unchecked then checked it
                    item.setChecked(true);
                    spring_flag = true;
                }
                return true;
            case (R.id.winter_item):
                if (item.isChecked()) {
                    // If item already checked then unchecked it
                    item.setChecked(false);
                    winter_flag = false;
                } else {
                    // If item is unchecked then checked it
                    item.setChecked(true);
                    winter_flag = true;
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<mainStyles, stylesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<mainStyles, stylesViewHolder>
                (mainStyles.class, R.layout.style_list_item, stylesViewHolder.class, childReference) {

            @Override
            public stylesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            protected void populateViewHolder(stylesViewHolder viewHolder, final mainStyles model, int position) {
                /*if ((summer_flag)) {
                    if ((summerReference.child("id")) == childReference.child("id")) {
                        viewHolder.setImage(getContext(), model.getUrl());
                    }
                } else if ((winter_flag)) {
                    if ((winterReference.child("id")) == childReference.child("id")) {
                        viewHolder.setImage(getContext(), model.getUrl());
                    }
                } else if ((autumn_flag)) {
                    if ((autumnReference.child("id")) == childReference.child("id")) {
                        viewHolder.setImage(getContext(), model.getUrl());
                    }
                } else if ((spring_flag)) {
                    if ((springReference.child("id")) == childReference.child("id")) {
                        viewHolder.setImage(getContext(), model.getUrl());
                    }
                }
                else*/
                viewHolder.setImage(getContext(), model.getUrl());
                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", model.getId());
                        bundle.putString("url", model.getUrl());
                        bundle.putString("desc", model.getDesc());
                        Intent mIntent = new Intent(getContext(), styleDetailsActivity.class);
                        mIntent.putExtras(bundle);
                        startActivity(mIntent);
                    }
                });
            }

        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class stylesViewHolder extends RecyclerView.ViewHolder {
        ImageView style_iv;
        View mview;

        public stylesViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;


        }

        public void setImage(Context context, String url) {
            style_iv = mview.findViewById(R.id.styles_fragment_list_item_iv);
            Glide.with(context).load(url).into(style_iv);

        }

    }


}
