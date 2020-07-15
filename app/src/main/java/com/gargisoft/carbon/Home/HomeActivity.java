package com.gargisoft.carbon.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gargisoft.carbon.Model.currentUser;
import com.gargisoft.carbon.R;

public class HomeActivity extends AppCompatActivity {
    currentUser currentUser = new currentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle extras = getIntent().getExtras();
        Intent intentIncoming = getIntent();
        if (extras != null) {

            currentUser = (currentUser) intentIncoming.getParcelableExtra("currentUser");

        }
    }
}
