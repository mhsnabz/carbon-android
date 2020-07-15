package com.gargisoft.carbon.Home;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gargisoft.carbon.Adapter.ChatList;
import com.gargisoft.carbon.Adapter.DiscoverList;
import com.gargisoft.carbon.Model.ChatListModel;
import com.gargisoft.carbon.Model.currentUser;
import com.gargisoft.carbon.Model.discoverUser;
import com.gargisoft.carbon.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    com.gargisoft.carbon.Model.currentUser currentUser = new currentUser();
    View rootView ;
    RecyclerView discoverList;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    FirestoreRecyclerAdapter<discoverUser, DiscoverList> adapter;
    public DiscoverFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        discoverList =(RecyclerView)rootView.findViewById(R.id.discoverList);
        discoverList.setHasFixedSize(true);
        discoverList.setLayoutManager(linearLayoutManager);
        Bundle extras = getActivity().getIntent().getExtras();
        Intent intentIncoming = getActivity().getIntent();
        if (extras != null) {

            currentUser = (currentUser) intentIncoming.getParcelableExtra("currentUser");

        }
        getAllList();
        return rootView;
    }

    private void getAllList()
    {

        CollectionReference ref = FirebaseFirestore.getInstance().collection("user");
        FirestoreRecyclerOptions<discoverUser> options = new FirestoreRecyclerOptions.Builder<discoverUser>()
                 .setLifecycleOwner(this)
            .setQuery(ref,discoverUser.class)
            .build();
        adapter = new FirestoreRecyclerAdapter<discoverUser, DiscoverList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DiscoverList holder, int position, @NonNull discoverUser model)
            {
                holder.setProfileImage(model.getProfileImage());
                holder.setUserName(model.getName());
                holder.setJob(model.getJob());
            }

            @NonNull
            @Override
            public DiscoverList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.discover_list_item, parent, false);

                return new DiscoverList(itemView);
            }
            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };

        discoverList.setAdapter(adapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
