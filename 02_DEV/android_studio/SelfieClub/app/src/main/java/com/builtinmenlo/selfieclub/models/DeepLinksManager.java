package com.builtinmenlo.selfieclub.models;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.dataSources.Club;
import com.builtinmenlo.selfieclub.fragments.SCDialog;
import com.builtinmenlo.selfieclub.util.Util;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;


/**
 * Created by Leonardo on 8/14/14.
 */
public class DeepLinksManager implements UserFinderProtocol,OtherUserClubsProtocol,SCDialogProtocol,ClubJoinProtocol{
    public static final String ALREADY_MEMBER_TAG = "already_member";
    public static final String WOULD_YOU_LIKE_TAG = "would_join";
    public Context context;
    public Activity activity;
    String userId;
    String clubId;
    String clubName;
    String userName;
    String ownerId;

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
                userManager.requestOtherUserClubs(this,userId);
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
        ownerId = club.getClubOwner().getUserId();
        clubId=club.getClubId();
        ApplicationManager applicationManager = new ApplicationManager(activity);
        String myId = applicationManager.getUserId();
        JSONArray members = club.getClubMembers();
        boolean isMember = false;
        for (int i=0;i<members.length();i++){
            try {
                JSONObject member = members.getJSONObject(i);
                if(myId.equalsIgnoreCase(member.getString("id"))){
                    isMember=true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(myId.equalsIgnoreCase(club.getClubOwner().getUserId())){
            isMember=true;
        }

        if(isMember){
            String message = context.getResources().getString(R.string.already_club_member_dialog);
            SCDialog dialog = new SCDialog();
            dialog.setScDialogProtocol(this);
            dialog.showTwoButtons=false;
            dialog.setMessage(message);
            dialog.setNeutralButtonTitle(context.getResources().getString(R.string.ok_button_title));
            dialog.show(activity.getFragmentManager(),ALREADY_MEMBER_TAG);
        }
        else {
            String message = String.format(context.getResources().getString(R.string.would_you_like_joinclub_dialog), clubName);
            SCDialog dialog = new SCDialog();
            dialog.setScDialogProtocol(this);
            dialog.showTwoButtons=true;
            dialog.setMessage(message);
            dialog.setNegativeButtonTitle(context.getResources().getString(R.string.no_button_title));
            dialog.setPositiveButtonTitle(context.getResources().getString(R.string.yes_button_title));
            dialog.show(activity.getFragmentManager(),WOULD_YOU_LIKE_TAG);
        }
    }

    public void didClickedButton(String dialogTag, int buttonIndex){
        if(dialogTag.equalsIgnoreCase(WOULD_YOU_LIKE_TAG)){
            if(buttonIndex==1){
                ApplicationManager applicationManager = new ApplicationManager(activity);
                ClubManager clubManager = new ClubManager();
                clubManager.joinClub(this,applicationManager.getUserId(),clubId,ownerId);

            }

        }
    }

    public void didJoinClub(Boolean result){
        Util.playDefaultNotificationSound(context);
    }
    public void didFailJoiningClub(String errorMessage){
        Log.w("DeepLinksManager","failed joining the club");
    }

}
