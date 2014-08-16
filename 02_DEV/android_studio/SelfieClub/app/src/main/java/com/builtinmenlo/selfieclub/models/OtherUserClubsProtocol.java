package com.builtinmenlo.selfieclub.models;

import com.builtinmenlo.selfieclub.dataSources.Club;

import java.util.ArrayList;

/**
 * Created by Leonardo on 8/15/14.
 */
public interface OtherUserClubsProtocol {
    public void didFindOtherUserClubs(ArrayList<Club> userClubs);
    public void didFailOtherUserClubs(String message);
}
