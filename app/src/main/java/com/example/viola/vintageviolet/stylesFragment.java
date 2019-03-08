package com.example.viola.vintageviolet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class stylesFragment extends Fragment {
    TextView category_tv;

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
        category_tv = RootView.findViewById(R.id.category_tv);

     /*  Bundle bundle = getArguments();
        if (bundle != null)
        {
        String myStr = bundle.getString("category");
        if(myStr=="dress")category_tv.setText("Dresses fragment");
        else if(myStr=="casual")category_tv.setText("Casual fragment");
        else category_tv.setText("classy fragment");

    }*/
        return RootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.styles_fragment_menu, menu);
    }


}
