package com.builtinmenlo.selfieclub.models;

import com.builtinmenlo.selfieclub.dataSources.User;

import java.util.ArrayList;

/**
 * Created by Leonardo on 6/16/14.
 */
public interface UserFriendsProtocol {
    public void didReceiveFriendsList(ArrayList<User> friendsList);
    public void didReceiveFriendsListError(String errorMessage);

}
