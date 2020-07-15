package com.gargisoft.carbon.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class currentUser implements Parcelable {

    String name,username,insta,twitter,snap,github,profileImage,skils,job,uid;
    int age;

    public currentUser(String name, String username, String insta, String twitter, String snap, String github, String profileImage, String skils, String job, String uid, int age) {
        this.name = name;
        this.username = username;
        this.insta = insta;
        this.twitter = twitter;
        this.snap = snap;
        this.github = github;
        this.profileImage = profileImage;
        this.skils = skils;
        this.job = job;
        this.uid = uid;
        this.age = age;
    }

    public currentUser() {
    }

    protected currentUser(Parcel in) {
        name = in.readString();
        username = in.readString();
        insta = in.readString();
        twitter = in.readString();
        snap = in.readString();
        github = in.readString();
        profileImage = in.readString();
        skils = in.readString();
        job = in.readString();
        uid = in.readString();
        age = in.readInt();
    }

    public static final Creator<currentUser> CREATOR = new Creator<currentUser>() {
        @Override
        public currentUser createFromParcel(Parcel in) {
            return new currentUser(in);
        }

        @Override
        public currentUser[] newArray(int size) {
            return new currentUser[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getInsta() {
        return insta;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getSnap() {
        return snap;
    }

    public String getGithub() {
        return github;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getSkils() {
        return skils;
    }

    public String getJob() {
        return job;
    }

    public String getUid() {
        return uid;
    }

    public int getAge() {
        return age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(insta);
        dest.writeString(twitter);
        dest.writeString(snap);
        dest.writeString(github);
        dest.writeString(profileImage);
        dest.writeString(skils);
        dest.writeString(job);
        dest.writeString(uid);
        dest.writeInt(age);
    }
}
