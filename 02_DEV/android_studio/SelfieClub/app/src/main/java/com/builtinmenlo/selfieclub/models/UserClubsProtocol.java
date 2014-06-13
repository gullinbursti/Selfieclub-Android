package com.builtinmenlo.selfieclub.models;

import org.json.JSONObject;

/**
 * Created by Leonardo on 6/12/14.
 */
public interface UserClubsProtocol {
    public void didReceiveUserClubs(JSONObject userClubs);
    public void didReceiveUserClubsError(String errorMessage);

}
