package com.gargisoft.carbon.ChatController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
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
import com.gargisoft.carbon.Helper.Utils;
import com.gargisoft.carbon.Model.currentUser;
import com.gargisoft.carbon.Model.discoverUser;
import com.gargisoft.carbon.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private Boolean isShow = false;
    currentUser currentUser = new currentUser();
    currentUser otherUser = new currentUser();
    LottieAnimationView video , image , location , sound ,cancel;
    CircleImageView profileImage;
    TextView name ;
    ImageButton back;
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
                Toast.makeText(ChatActivity.this,"sound",Toast.LENGTH_SHORT).show();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Utils.back(ChatActivity.this);
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
        transition.setDuration(300);
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
}
