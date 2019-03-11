package com.example.viola.vintageviolet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.example.viola.vintageviolet.R;
import com.example.viola.vintageviolet.models.homeStyle;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class homeFragment extends Fragment {
    RecyclerView main_rv;
    DatabaseReference reference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_home, container, false);

        reference = FirebaseDatabase.getInstance().getReference().child("home");

        main_rv = RootView.findViewById(R.id.main_recycler_view);
        main_rv.setHasFixedSize(true);
        main_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        return RootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<homeStyle, homestyleViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<homeStyle, homestyleViewHolder>
                (homeStyle.class, R.layout.main_list_item, homestyleViewHolder.class, reference) {
            @Override
            protected void populateViewHolder(homestyleViewHolder viewHolder, homeStyle model, int position) {
                viewHolder.setTitle(model.getDesc());
                viewHolder.setImage(getContext(), model.getUrl());

            }
        };
        main_rv.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class homestyleViewHolder extends RecyclerView.ViewHolder {
        View mview;

        public homestyleViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setTitle(String title) {
            TextView title_tv = mview.findViewById(R.id.home_style_tv);
            title_tv.setText(title);
        }

        public void setImage(Context context, String url) {
            ImageView home_style = mview.findViewById(R.id.styles_iv);
            Glide.with(context).load(url).into(home_style);

        }


    }


}
