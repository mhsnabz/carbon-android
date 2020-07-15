package com.gargisoft.carbon.Home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.HeaderViewListAdapter;
import android.widget.TextView;

import com.gargisoft.carbon.Adapter.HeaderListAdapter;
import com.gargisoft.carbon.Model.currentUser;
import com.gargisoft.carbon.Model.discoverUser;
import com.gargisoft.carbon.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    currentUser currentUser = new currentUser();
    CircleImageView profileImage ;
    TextView username ;
    RecyclerView headerFriend;

    ArrayList<String> list;
    HeaderListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        profileImage = (CircleImageView)findViewById(R.id.profileImage);
        username = (TextView)findViewById(R.id.username);
        headerFriend =(RecyclerView)findViewById(R.id.headerFriend);
        headerFriend.setHasFixedSize(true);

        headerFriend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        list = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        Intent intentIncoming = getIntent();
        if (extras != null) {

            currentUser = (currentUser) intentIncoming.getParcelableExtra("currentUser");
            Picasso.get().load(currentUser.getProfileImage()).resize(200,200)
                    .into(profileImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
            username.setText(currentUser.getUsername());
        }
        getFriendList();

    }

    private void setUserInfo(){

    }
    private void getFriendList(){
        CollectionReference ref = FirebaseFirestore.getInstance().collection("user")
                .document(currentUser.getUid()).collection("msgList").document(currentUser.getUid()).collection(currentUser.getUid());
        ref.addSnapshotListener(HomeActivity.this, MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    System.out.println(e.getLocalizedMessage());
                }else {
                    for (DocumentChange doc : querySnapshot.getDocumentChanges()){
                        if (doc.getType() == DocumentChange.Type.ADDED){
                            list.add(doc.getDocument().getId());
                            adapter = new HeaderListAdapter(list);
                            headerFriend.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
            }
        });
    }
}
