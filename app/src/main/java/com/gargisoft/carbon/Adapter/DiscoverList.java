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

public class DiscoverList extends RecyclerView.ViewHolder {
    public DiscoverList(@NonNull View itemView) {
        super(itemView);
    }
    ProgressBar progressBar = (ProgressBar)itemView.findViewById(R.id.progres);
    CircleImageView profileImage = (CircleImageView)itemView.findViewById(R.id.profile_image);
    TextView name = (TextView)itemView.findViewById(R.id.name);
    TextView job = (TextView)itemView.findViewById(R.id.job);
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
    name.setText(_name);
    }
    public void setJob(String _job){
        job.setText(_job);
    }



}
