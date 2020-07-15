package com.gargisoft.carbon.Helper;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.gargisoft.carbon.Model.currentUser;
import com.gargisoft.carbon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public abstract class Utils {
    public static void go(Activity activity){
        activity.overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left);
    }
    public static void back(Activity activity){
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    public static void sendNotificaiton(final currentUser user , final String otherUserUid , final String type , final long time , final String msg){
        final CollectionReference ref = FirebaseFirestore.getInstance().collection("notification")
                .document(otherUserUid).collection("notification");
        final Map<String,Object> map = new HashMap<>();

        DocumentReference document = FirebaseFirestore.getInstance().collection("user")
                .document(otherUserUid);
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    map.put("from",user.getUid());
                    map.put("to",otherUserUid);
                    map.put("senderName",user.getName());
                    map.put("type",type);
                    map.put("senderImage",user.getProfileImage());
                    map.put("time",time);
                    map.put("msg",msg);
                    map.put("getterTokenID",task.getResult().getString("tokenID"));
                    if (!otherUserUid.equals(user.getUid()))
                        ref.add(map);
                }
            }
        });






    }

}
