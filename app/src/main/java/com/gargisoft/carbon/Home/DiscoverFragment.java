package com.gargisoft.carbon.Home;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gargisoft.carbon.Model.currentUser;
import com.gargisoft.carbon.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    currentUser currentUser = new currentUser();
    View rootView ;
    public DiscoverFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        Bundle extras = getActivity().getIntent().getExtras();
        Intent intentIncoming = getActivity().getIntent();
        if (extras != null) {

            currentUser = (currentUser) intentIncoming.getParcelableExtra("currentUser");

        }

        return rootView;
    }

}
