package com.gargisoft.carbon.ChatController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.gargisoft.carbon.Adapter.ChatAdapter;
import com.gargisoft.carbon.Helper.Utils;
import com.gargisoft.carbon.Model.ConservationModel;
import com.gargisoft.carbon.Model.currentUser;
import com.gargisoft.carbon.Model.discoverUser;
import com.gargisoft.carbon.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private Boolean isShow = false, isRecording = false;
    currentUser currentUser = new currentUser();
    currentUser otherUser = new currentUser();
    LottieAnimationView video , image , location , sound ,cancel , recordBtn  , recording;
    CircleImageView profileImage;
    TextView name ;
    ImageButton back , dismiss;
    ConservationModel model;
    List<ConservationModel> msgges = new ArrayList<>();
    RecyclerView msg_list;
    ChatAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        profileImage =(CircleImageView)findViewById(R.id.profileImage);
        back=(ImageButton)findViewById(R.id.back);
        name = (TextView)findViewById(R.id.name);
        Bundle extras = getIntent().getExtras();
        Intent intentIncoming = getIntent();
        if (extras != null) {

      currentUser = (currentUser) intentIncoming.getParcelableExtra("currentUser");
            Log.d("otherUser", "onCreate: "+intentIncoming.getParcelableExtra("otherUser"));
           otherUser = (currentUser) intentIncoming.getParcelableExtra("otherUser");
            Picasso.get().load(otherUser.getProfileImage()).resize(200,200).centerCrop()
                    .into(profileImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
            name.setText(otherUser.getName());
        }

        ///TODO: record bar init

        recordBtn = (LottieAnimationView)findViewById(R.id.record);
        recording =(LottieAnimationView)findViewById(R.id.recordinProgres);
        dismiss = (ImageButton)findViewById(R.id.down);

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording){


                    recordBtn.setSpeed(1F);
                    recordBtn.playAnimation();
                    recording.setSpeed(0.1334F);
                    recording.playAnimation();
                    isRecording = true;
                }else{

                    recordBtn.setSpeed(-1F);
                    recordBtn.playAnimation();
                    recording.pauseAnimation();
                    isRecording = false;

                }
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRecord(false);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toggle(true);
                    }
                }, 100);

            }
        });

        recordBtn.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        ///TODO: main bar ini
        video =(LottieAnimationView)findViewById(R.id.video);
        image =(LottieAnimationView)findViewById(R.id.image);
        sound =(LottieAnimationView)findViewById(R.id.sound);
        location =(LottieAnimationView)findViewById(R.id.location);
        cancel =(LottieAnimationView)findViewById(R.id.cancel);



        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this,"video",Toast.LENGTH_SHORT).show();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this,"image",Toast.LENGTH_SHORT).show();

            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this,"location",Toast.LENGTH_SHORT).show();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(false);
            }
        });
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(false);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toggleRecord(true);
                    }
                }, 100);



            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Utils.back(ChatActivity.this);
            }
        });

        //TODO: msg init

        msg_list = (RecyclerView)findViewById(R.id.list);
        msg_list.setHasFixedSize(true);
        msg_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        getMsg();
    }

    private void getMsg(){
        Query db = FirebaseFirestore.getInstance().collection("msg")
                .document(currentUser.getUid()).collection(otherUser.getUid()).orderBy("time", Query.Direction.ASCENDING);
        db.addSnapshotListener(this, MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e == null){
                    if (querySnapshot!=null){
                        for (DocumentChange doc : querySnapshot.getDocumentChanges()){
                            if (doc.getType().equals(DocumentChange.Type.ADDED)){
                                model = doc.getDocument().toObject(ConservationModel.class);
                                msgges.add(model);
                                msg_list.scrollToPosition(msgges.size() - 1);
                            }
                        }

                    }
                    adapter = new ChatAdapter(msgges,currentUser,otherUser,ChatActivity.this);
                    msg_list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }



    public void mediaClik(View view) {
        toggle(true);
    }

    private void toggle(boolean show) {
        CardView toolbar = (CardView)findViewById(R.id.mediaToolbar);
        ViewGroup parent = findViewById(R.id.parent);
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(500);
        transition.addTarget(R.id.mediaToolbar);

        TransitionManager.beginDelayedTransition(parent, transition);
        toolbar.setVisibility(show ? View.VISIBLE : View.GONE);
        video.setRepeatCount(1);
        image.setRepeatCount(1);
        location.setRepeatCount(1);
        cancel.setRepeatCount(1);
        sound.setRepeatCount(1);
        sound.playAnimation();
        video.playAnimation();
        image.playAnimation();
        location.playAnimation();
        cancel.playAnimation();
    }
    private void toggleRecord(final boolean show) {
        CardView toolbar = (CardView)findViewById(R.id.recordBar);
        ViewGroup parent = findViewById(R.id.parent);
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(500);
        transition.addTarget(R.id.recordBar);
        TransitionManager.beginDelayedTransition(parent, transition);
        toolbar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void sendMsg(View view)
    {

    }
}
