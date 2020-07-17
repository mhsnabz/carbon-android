package com.gargisoft.carbon.App;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Carbon extends Application {
    String currentUser;
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    @Override
    public void onCreate() {
        super.onCreate();
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    currentUser = firebaseAuth.getCurrentUser().getUid();
                    String tokenID = FirebaseInstanceId.getInstance().getToken();
                    Map<String,Object> map=new HashMap<>();
                    map.put("tokenID",tokenID);
                    Log.d("tokenId", "onAuthStateChanged: " + tokenID);
                    db.collection("user")
                            .document(currentUser)
                            .set(map, SetOptions.merge());
                }
            }
        });
    }
}
