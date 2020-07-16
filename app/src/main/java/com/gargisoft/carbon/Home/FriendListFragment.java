package com.gargisoft.carbon.Home;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gargisoft.carbon.Adapter.ChatList;
import com.gargisoft.carbon.ChatController.ChatActivity;
import com.gargisoft.carbon.Helper.Utils;
import com.gargisoft.carbon.Model.ChatListModel;
import com.gargisoft.carbon.Model.currentUser;
import com.gargisoft.carbon.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendListFragment extends Fragment {

    com.gargisoft.carbon.Model.currentUser currentUser = new currentUser();
    View rootView ;
    RecyclerView friendList;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    FirestoreRecyclerAdapter<ChatListModel, ChatList> adapter;
    public FriendListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_firend_list, container, false);
        friendList =(RecyclerView)rootView.findViewById(R.id.friendList);
        friendList.setHasFixedSize(true);
        friendList.setLayoutManager(linearLayoutManager);
        Bundle extras = getActivity().getIntent().getExtras();
        Intent intentIncoming = getActivity().getIntent();
        if (extras != null) {

            currentUser = (currentUser) intentIncoming.getParcelableExtra("currentUser");

        }
        getAllList();
        return rootView;

    }
    private void getAllList(){
        Query db = FirebaseFirestore.getInstance().collection("user").document(currentUser.getUid())
                .collection("msgList").document(currentUser.getUid()) .collection(currentUser.getUid()).orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatListModel> options = new FirestoreRecyclerOptions.Builder<ChatListModel>()
                .setLifecycleOwner(this)
                .setQuery(db,ChatListModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<ChatListModel, ChatList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatList holder, int position, @NonNull final ChatListModel model)
            {
                holder.setBadge(model.getBadge());
                holder.setLastMsg(model.getLastMsg());
                holder.setUserName(model.getSenderUid());
                holder.setProfileImage(model.getSenderProfileImage());
                holder.setTimeAgo(model.getDate());
                holder.itemView.findViewById(R.id.options).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //settingDialog(model.senderUid);
                    }
                });
                holder.itemView.findViewById(R.id.profile_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //   Utils.showImage(getActivity(),model.getSenderProfileImage());
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), ChatActivity.class);
                        getActivity().startActivity(i);
                        Utils.go(getActivity());
                       /* Intent i = new Intent(getActivity() , OneToOneChatActivity.class);
                        i.putExtra("currentUser",user);
                        i.putExtra("otherUser",model.getSenderUid());
                        startActivity(i);
                        setBadge(model.getSenderUid());
                        Utils.go(ChatActivity.this);*/


                    }
                });
            }
            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }
            @NonNull
            @Override
            public ChatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_list_item, parent, false);

                return new ChatList(itemView);
            }
        } ;

        friendList.setAdapter(adapter);

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
