package com.gargisoft.carbon.Home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.TextView;

import com.gargisoft.carbon.Adapter.HeaderListAdapter;
import com.gargisoft.carbon.Adapter.ViewPagerAdapter;
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
    TextView headerLbl , friendLbl, discoverLbl;
    ViewPager mainViewPager;
    ViewPagerAdapter viewPagerAdapter;
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

        headerLbl = (TextView)findViewById(R.id.headerLbl);
        friendLbl = (TextView)findViewById(R.id.friendLbl);
        discoverLbl = (TextView)findViewById(R.id.discoverLbl);
        mainViewPager = (ViewPager)findViewById(R.id.mainPager);

        friendLbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewPager.setCurrentItem(0);
            }
        });

        discoverLbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainViewPager.setCurrentItem(1);
            }
        });
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(viewPagerAdapter);
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

        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setUserInfo(){

    }

    private void changeTabs(int position){
        if (position == 0){
            headerLbl.setText("Chats");
            friendLbl.setTextSize(24);
            friendLbl.setTextColor(getColor(R.color.colorPrimary));
            discoverLbl.setTextSize(16);
            discoverLbl.setTextColor(getColor(R.color.gray));
        }
        else if (position == 1){
            headerLbl.setText("Discover");
            friendLbl.setTextSize(16);
            friendLbl.setTextColor(getColor(R.color.gray));
            discoverLbl.setTextSize(24);
            discoverLbl.setTextColor(getColor(R.color.colorPrimary));
        }
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
