package com.builtinmenlo.selfieclub.models;

/**
 * Created by Leonardo on 7/28/14.
 */
public interface ClubInviteProtocol {
    public void didSendCubInvite(Boolean response);
    public void didFailSendingClubInvite(String errorMessage);
}
