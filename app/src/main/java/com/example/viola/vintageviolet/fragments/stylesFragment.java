package com.example.viola.vintageviolet.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.viola.vintageviolet.R;
import com.example.viola.vintageviolet.models.mainStyles;
import com.example.viola.vintageviolet.activities.styleDetailsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class stylesFragment extends Fragment {

    RecyclerView mRecyclerView;
    DatabaseReference reference;
    DatabaseReference childReference;
    String user_id;
    String myStr;

    private Parcelable savedRecyclerViewState;
    private final String LIST_STATE = "recycler_state";

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

        mRecyclerView = RootView.findViewById(R.id.styles_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        Bundle bundle = getArguments();
        if (bundle != null) {
            user_id = bundle.getString("userid");
            myStr = bundle.getString("category");
            if (myStr == "dress") {
                childReference = reference.child("dress");
            } else if (myStr == "casual") {
                childReference = reference.child("casual");
            } else if (myStr == "classy") {
                childReference = reference.child("classy");
            } else if (myStr == "winter") {
                childReference = reference.child("winter");
            } else if (myStr == "summer") {
                childReference = reference.child("summer");
            } else if (myStr == "spring") {
                childReference = reference.child("spring");
            } else if (myStr == "autumn") {
                childReference = reference.child("autumn");
            }
        }
        return RootView;

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
            protected void populateViewHolder(final stylesViewHolder viewHolder, final mainStyles model, int position) {

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        savedRecyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LIST_STATE, savedRecyclerViewState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            savedRecyclerViewState = savedInstanceState.getParcelable(LIST_STATE);
        }
    }
}
