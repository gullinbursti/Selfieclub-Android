package com.builtinmenlo.selfieclub.models;

import com.builtinmenlo.selfieclub.dataSources.FriendsViewData;
import com.builtinmenlo.selfieclub.dataSources.User;

import java.util.ArrayList;

/**
 * Created by Leonardo on 6/16/14.
 */
public interface UserFriendsProtocol {
    public void didReceiveFriendsList(FriendsViewData friendsViewData);
    public void didReceiveFriendsListError(String errorMessage);

}
