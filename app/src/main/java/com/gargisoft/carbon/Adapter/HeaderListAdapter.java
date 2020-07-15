package com.gargisoft.carbon.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gargisoft.carbon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HeaderListAdapter extends RecyclerView.Adapter<HeaderListAdapter.ViewHolder> {
    ArrayList<String> list;

    public HeaderListAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.header_item, parent, false);
        return new ViewHolder(v);
    }

    @NonNull

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.setProfileImage(list.get(position));
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        CircleImageView circleImageView = (CircleImageView)itemView.findViewById(R.id.profileImage);
        public void setProfileImage(String url){
            DocumentReference ref = FirebaseFirestore.getInstance().collection("user")
                    .document(url);
            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        if (!task.getResult().getString("profileImage").isEmpty()){
                            Picasso.get().load(task.getResult().getString("profileImage")).resize(200,200).centerCrop().into(circleImageView);
                        }
                    }
                }
            });

        }
    }
}

