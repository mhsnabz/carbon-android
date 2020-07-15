package com.gargisoft.carbon.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.gargisoft.carbon.Helper.Utils;
import com.gargisoft.carbon.Home.HomeActivity;
import com.gargisoft.carbon.Model.currentUser;
import com.gargisoft.carbon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kongzue.dialog.v3.WaitDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import okhttp3.internal.Util;

public class LoginActivity extends AppCompatActivity {
    Dialog dialog;
    Button login;
    LottieAnimationView wait;
    MaterialEditText email , password;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (MaterialEditText)findViewById(R.id.email);
        password = (MaterialEditText)findViewById(R.id.password);

        wait = (LottieAnimationView)findViewById(R.id.lottie);
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLogin(email,password);
            }
        });
    }

    public void signUp(View view)
    {
        Intent i = new Intent(LoginActivity.this,RegisterActivity.class);

        startActivity(i);
        Utils.go(LoginActivity.this);

    }

    private void setLogin(final MaterialEditText email, final MaterialEditText password)
    {

        String _email = email.getText().toString();
        String _pass = password.getText().toString();
        if (_email.isEmpty()){
            email.setError("Pleas Enter An E-Mail Adress");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(_email).matches())
        {

            email.setError("Please Enter A Valid E-Mail");
            email.requestFocus();
            return;
        }
        if (_pass.isEmpty())
        {

            password.setError("Please Enter A Password");
            password.requestFocus();
            return;
        }
        login.setVisibility(View.GONE);
        wait.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(_email,_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful()){
                    DocumentReference ref = FirebaseFirestore.getInstance().collection("user").document(task.getResult().getUser().getUid());
                    ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult().exists()){
                                    currentUser user = task.getResult().toObject(currentUser.class);
                                    Log.d("username", "onComplete: "+ user.getUsername());
                                   Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                    i.putExtra("currentUser",user);
                                    startActivity(i);
                                    Utils.go(LoginActivity.this);
                                    finish();


                                }
                            }
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("errr",e.getMessage());
                if (e.getMessage() == "There is no user record corresponding to this identifier. The user may have been deleted."){
                    email.setError("There is no user record corresponding to this identifier. The user may have been deleted.");
                    email.requestFocus();

                    login.setVisibility(View.VISIBLE);
                    wait.setVisibility(View.INVISIBLE);
                    return;
                }
                else if (e.getMessage() == "The password is invalid or the user does not have a password."){
                    password.setError("The password is invalid or the user does not have a password.");
                    password.requestFocus();

                    login.setVisibility(View.VISIBLE);
                    wait.setVisibility(View.INVISIBLE);
                    return;
                }
            }
        });

    }


}
