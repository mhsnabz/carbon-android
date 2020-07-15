package com.gargisoft.carbon.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.gargisoft.carbon.Model.currentUser;
import com.gargisoft.carbon.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    currentUser currentUser = new currentUser();
    CircleImageView profileImage ;
    TextView username ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        profileImage = (CircleImageView)findViewById(R.id.profileImage);
        username = (TextView)findViewById(R.id.username);

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
    }

    private void setUserInfo(){

    }
}
