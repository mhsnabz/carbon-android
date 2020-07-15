package com.gargisoft.carbon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.gargisoft.carbon.Helper.Utils;
import com.gargisoft.carbon.Home.HomeActivity;
import com.gargisoft.carbon.LoginRegister.LoginActivity;
import com.gargisoft.carbon.Model.currentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {
    ImageView logo;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String currentUser = auth.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        logo = (ImageView)findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.mytransistion);
        logo.startAnimation(animation);
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2000);
                }catch (InterruptedException ex){
                    ex.printStackTrace();
                }finally {
                    if (currentUser!=null){
                        DocumentReference ref = FirebaseFirestore.getInstance().collection("user").document(currentUser);
                        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                                    i.putExtra("currentUser",task.getResult().toObject(com.gargisoft.carbon.Model.currentUser.class));
                                    startActivity(i);
                                    Utils.go(SplashScreen.this);
                                    finish();
                                }
                            }
                        });

                    }else {
                        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(i);
                        Utils.go(SplashScreen.this);
                        finish();
                    }

                }
            }
        };
        thread.start();


    }
}
