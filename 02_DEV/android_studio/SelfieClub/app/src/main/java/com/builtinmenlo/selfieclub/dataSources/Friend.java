package com.builtinmenlo.selfieclub.dataSources;

import android.graphics.Bitmap;

public class Friend {
    private boolean friend;
    private String name;
    private int followers;
    private Bitmap avatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers1) {
        this.followers = followers;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}