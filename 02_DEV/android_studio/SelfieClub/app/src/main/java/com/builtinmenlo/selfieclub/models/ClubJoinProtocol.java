package com.builtinmenlo.selfieclub.models;

/**
 * Created by Leonardo on 7/28/14.
 */
public interface ClubJoinProtocol {
    public void didJoinClub(Boolean result);
    public void didFailJoiningClub(String errorMessage);
}
