package com.gargisoft.carbon.Adapter;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gargisoft.carbon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatList extends RecyclerView.ViewHolder {

    public ChatList(@NonNull View itemView) {
        super(itemView);
    }
    ProgressBar progressBar = (ProgressBar)itemView.findViewById(R.id.progres);
    CircleImageView profileImage = (CircleImageView)itemView.findViewById(R.id.profile_image);
    TextView name = (TextView)itemView.findViewById(R.id.name);
    TextView lastMsg = (TextView)itemView.findViewById(R.id.lastMsg);
    TextView timeAgo = (TextView)itemView.findViewById(R.id.timeAgo);
    TextView count = (TextView)itemView.findViewById(R.id.badgeCount);
    RelativeLayout relBadge = (RelativeLayout)itemView.findViewById(R.id.relBadge);

    public void setProfileImage(String url){
        if (!url.isEmpty()){
            Picasso.get().load(url).resize(200,200).centerCrop().placeholder(android.R.color.darker_gray).into(profileImage, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    progressBar.setVisibility(View.GONE);

                }
            });
        }else {
            progressBar.setVisibility(View.GONE);

        }

    }
    public void setUserName(String _name){
        DocumentReference ref = FirebaseFirestore.getInstance().collection("user")
                .document(_name);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    name.setText(task.getResult().getString("name"));
                }
            }
        });
        //  name.setText(_name);

    }
    public void setLastMsg(String _msg){
        lastMsg.setText(_msg);
    }

    public void setBadge(long badge){

        if (badge>0){
            relBadge.setVisibility(View.VISIBLE);
            count.setText(String.valueOf(badge));
        }else {
            relBadge.setVisibility(View.GONE);
        }
    }
    public  void setTimeAgo(Timestamp _time) {
        if (_time!=null){

            long time =_time.getSeconds();

            int SECOND_MILLIS = 1000;
            int MINUTE_MILLIS = 60 * SECOND_MILLIS;
            int HOUR_MILLIS = 60 * MINUTE_MILLIS;
            final int DAY_MILLIS = 24 * HOUR_MILLIS;
            if (time < 1000000000000L) {
                // if timestamp given in seconds, convert to millis
                time *= 1000;
            }

            long now = Calendar.getInstance().getTimeInMillis();
            if (time > now || time <= 0) {
                timeAgo.setText("");
            }

            // TODO: localize
            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                timeAgo.setText("şimdi");
            } else if (diff < 2 * MINUTE_MILLIS) {
                timeAgo.setText("Birkaç dk önce");
            } else if (diff < 50 * MINUTE_MILLIS) {
                String _Time =String.valueOf(diff / MINUTE_MILLIS) + " dakika önce";
                timeAgo.setText(_Time);
            } else if (diff < 90 * MINUTE_MILLIS) {
                timeAgo.setText("bir saat önce");
            } else if (diff < 24 * HOUR_MILLIS) {
                String _Time =String.valueOf(diff / HOUR_MILLIS) + " saat önce";
                timeAgo.setText(_Time);

            } else if (diff < 48 * HOUR_MILLIS) {
                timeAgo.setText("dün");
            } else {
                String _Time =String.valueOf(diff / DAY_MILLIS)+ " gün önce";
                timeAgo.setText(_Time);

            }
        }

    }

}
