package com.gargisoft.carbon.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class ChatListModel implements Parcelable {

    String senderUid,getterUid,senderProfileImage,lastMsg,name;
    long badge;
    long time;
    Timestamp date;

    public ChatListModel(String senderUid, String getterUid, String senderProfileImage, String lastMsg, String name, long badge, long time, Timestamp date) {

        this.senderUid = senderUid;
        this.getterUid = getterUid;
        this.senderProfileImage = senderProfileImage;
        this.lastMsg = lastMsg;
        this.name = name;
        this.badge = badge;
        this.time = time;
        this.date = date;
    }

    public ChatListModel() {
    }

    protected ChatListModel(Parcel in) {
        senderUid = in.readString();
        getterUid = in.readString();
        senderProfileImage = in.readString();
        lastMsg = in.readString();
        name = in.readString();
        badge = in.readLong();
        time = in.readLong();
        date = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<ChatListModel> CREATOR = new Creator<ChatListModel>() {
        @Override
        public ChatListModel createFromParcel(Parcel in) {
            return new ChatListModel(in);
        }

        @Override
        public ChatListModel[] newArray(int size) {
            return new ChatListModel[size];
        }
    };

    public String getSenderUid() {
        return senderUid;
    }

    public String getName() {
        return name;
    }

    public String getGetterUid() {
        return getterUid;
    }

    public String getSenderProfileImage() {
        return senderProfileImage;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public long getBadge() {
        return badge;
    }

    public long getTime() {
        return time;
    }

    public Timestamp getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(senderUid);
        dest.writeString(getterUid);
        dest.writeString(senderProfileImage);
        dest.writeString(lastMsg);
        dest.writeString(name);
        dest.writeLong(badge);
        dest.writeLong(time);
        dest.writeParcelable(date, flags);
    }
}
