package com.builtinmenlo.selfieclub.models;

/**
 * Created by Leonardo on 7/28/14.
 */
public interface CreateClubProtocol {
    public void didCreateClub(String clubId, String clubName);
    public void didFailCreatingClub(String errorMessage);
}
