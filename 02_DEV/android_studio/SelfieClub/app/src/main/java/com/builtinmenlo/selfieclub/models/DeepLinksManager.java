package com.builtinmenlo.selfieclub.models;

import android.net.Uri;
import android.util.Log;

import com.builtinmenlo.selfieclub.dataSources.Club;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;


/**
 * Created by Leonardo on 8/14/14.
 */
public class DeepLinksManager implements UserFinderProtocol,OtherUserClubsProtocol{
    String userId;
    String clubName;
    String userName;
    public void deepLinkRegistry(Uri data){
        if(data!=null) {
            try{
                String absolutePath = URLDecoder.decode(data.getPath(),"UTF-8");
                String[] tokens = absolutePath.split("/");
                userName = tokens[1];
                clubName = tokens[2];
                UserManager userManager = new UserManager();
                userManager.findUserByUsername(this,userName);
            }
            catch (Exception e){}

        }
    }
    public void didFailFindingUser(String errorMessage){
        Log.w("DeepLinksManager",errorMessage);
    }
    public void didFindUser(JSONObject user){
        if(user != null){
            try {
                userId = user.getString("id");
                UserManager userManager = new UserManager();
                //userManager.requestOtherUserClubs(this,userId);
                userManager.requestOtherUserClubs(this, "151159");
            }
            catch (Exception e){}

        }
    }

    public void didFailOtherUserClubs(String message){
        Log.w("","");

    }

    public void didFindOtherUserClubs(ArrayList<Club> userClubs){
        for (int i=0;i<userClubs.size();i++){
            if(clubName.equalsIgnoreCase(userClubs.get(i).getClubName())){
                inviteIntoClub(userClubs.get(i));
                break;
            }
        }
    }

    public void inviteIntoClub(Club club){
        JSONArray members = club.getClubMembers();
        for (int i=0;i<members.length();i++){

        }
    }


}
