package com.builtinmenlo.selfieclub.models;

import com.builtinmenlo.selfieclub.dataSources.Club;



/**
 * Created by Leonardo on 6/11/14.
 */
public interface ClubInfoProtocol {
    public void didReceiveClubInfo(Club club);
    public void didReceiveClubInfoError(String error);

}
