package com.builtinmenlo.selfieclub.dataSources;

import java.util.ArrayList;

/**
 * Created by Leonardo on 6/19/14.
 */
public class FriendsViewData {
    private Friend owner;
    private ArrayList<Friend> friends;

    public User getOwner() {
        return owner;
    }

    public void setOwner(Friend owner) {
        this.owner = owner;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }
}
