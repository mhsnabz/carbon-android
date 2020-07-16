package com.gargisoft.carbon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gargisoft.carbon.Model.ConservationModel;
import com.gargisoft.carbon.Model.currentUser;
import com.gargisoft.carbon.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECIEVED = 2;


    private static final int VIEW_TYPE_IMAGE_RECIEVED = 3;
    private static final int VIEW_TYPE_IMAGE_SEND = 4;

    private static final int VIEW_TYPE_VIDEO_RECIEVED = 5;
    private static final int VIEW_TYPE_VIDEO_SEND = 6;
    private static final int VIEW_TYPE_SOUND_SEND = 7;
    private static final int VIEW_TYPE_SOUND_RECEIVED = 8;



    List<ConservationModel> list;
    currentUser user , otherUser;
    Context context;



    public ChatAdapter(List<ConservationModel> list, currentUser user, currentUser otherUser, Context context) {
        this.list = list;
        this.user = user;
        this.otherUser = otherUser;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if(i == VIEW_TYPE_MESSAGE_SENT)

        {    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_msg,parent,false);
            return new ChatAdapter.ViewHolder(itemView);

        }else if ( i == VIEW_TYPE_MESSAGE_RECIEVED){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.received,parent,false);
            return new ChatAdapter.ViewHolder(itemView);
        }else if (i == VIEW_TYPE_IMAGE_SEND){

        }else if (i == VIEW_TYPE_IMAGE_RECIEVED){

        }else if ( i == VIEW_TYPE_VIDEO_RECIEVED){

        }else if (i == VIEW_TYPE_VIDEO_SEND){

        }else if (i == VIEW_TYPE_SOUND_RECEIVED){

        }else {
            //sound send
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        ConservationModel msg = (ConservationModel) list.get(position);
        if (msg.getSenderUid().equals(user.getUid())) {
            if (msg.getType().equals("image")){
                return VIEW_TYPE_IMAGE_SEND;
            }else if (msg.getType().equals("video")){
                return VIEW_TYPE_VIDEO_SEND;
            }else if (msg.getType().equals("msg")){
                return VIEW_TYPE_MESSAGE_SENT;
            }else {
                return VIEW_TYPE_SOUND_SEND;
            }
        }else {
            if (msg.getType().equals("image")){
                return VIEW_TYPE_IMAGE_RECIEVED;
            }else if (msg.getType().equals("video")){
                return VIEW_TYPE_VIDEO_RECIEVED;
            }else if (msg.getType().equals("msg")){
                return VIEW_TYPE_MESSAGE_RECIEVED;
            }else {
                return VIEW_TYPE_SOUND_RECEIVED;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        switch (holder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SENT:
                holder.setMsg(list.get(position).getMsg());
                holder.getDate(list.get(position).getDate().getSeconds());
                break;
            case VIEW_TYPE_MESSAGE_RECIEVED:
                holder.setMsg(list.get(position).getMsg());
                holder.setProfileImage(otherUser.getProfileImage());
                holder.getDate(list.get(position).getDate().getSeconds());
                break;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        TextView msg = (TextView)itemView.findViewById(R.id.msgText);
        TextView timeAgo = (TextView)itemView.findViewById(R.id.timeAgo);

        CircleImageView profileImage = (CircleImageView)itemView.findViewById(R.id.profile_image);

        void setMsg(String  _msg){
            msg.setText(_msg);
        }
        void setProfileImage(String url){
            Picasso.get().load(url).centerCrop().resize(64,64).into(profileImage);

        }
        private void getDate(long milliSeconds) {

            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd , h:mm a");

            Calendar calendar = Calendar.getInstance(Locale.forLanguageTag("tr"));
            calendar.setTimeInMillis((int) milliSeconds * 1000L);
            timeAgo.setText(formatter.format(calendar.getTime()));

        }
    }
}
