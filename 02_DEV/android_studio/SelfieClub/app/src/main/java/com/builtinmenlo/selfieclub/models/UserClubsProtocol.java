package com.builtinmenlo.selfieclub.models;

import com.builtinmenlo.selfieclub.dataSources.Club;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Leonardo on 6/12/14.
 */
public interface UserClubsProtocol {
    public void didReceiveUserClubs(ArrayList<Club> userClubs);
    public void didReceiveUserClubsError(String errorMessage);

}
