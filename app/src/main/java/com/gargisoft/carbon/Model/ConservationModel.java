package com.gargisoft.carbon.Model;

import com.google.firebase.Timestamp;

public class ConservationModel {
    String msg,senderUid,getterUid,type,name,senderProfileImage;

    Timestamp date;

    public ConservationModel(String msg, String senderUid, String getterUid, String type, String name ,Timestamp date) {
        this.msg = msg;
        this.senderUid = senderUid;
        this.getterUid = getterUid;
        this.type = type;
        this.name = name;
        this.date = date;
    }

    public ConservationModel() {
    }

    public String getMsg() {
        return msg;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public String getGetterUid() {
        return getterUid;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }



    public Timestamp getDate() {
        return date;
    }
}
