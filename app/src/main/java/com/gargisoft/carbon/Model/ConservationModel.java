package com.gargisoft.carbon.Model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class ConservationModel {
    String msg,senderUid,getterUid,type,name,senderProfileImage;
    Date tarih;
    Timestamp date;

    public ConservationModel() {
    }

    public ConservationModel(String msg, String senderUid, String getterUid, String type, String name, String senderProfileImage, Date tarih, Timestamp date) {
        this.msg = msg;
        this.senderUid = senderUid;
        this.getterUid = getterUid;
        this.type = type;
        this.name = name;
        this.senderProfileImage = senderProfileImage;
        this.tarih = tarih;
        this.date = date;
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

    public String getSenderProfileImage() {
        return senderProfileImage;
    }

    public Date getTarih() {
        return tarih;
    }

    public Timestamp getDate() {
        return date;
    }
}
