package com.builtinmenlo.selfieclub.models;

import android.app.Activity;
import android.content.SharedPreferences;

import com.builtinmenlo.selfieclub.Constants;

/**
 * Created by Leonardo on 7/9/14.
 */
public class ApplicationManager {

    Activity activity;

    public ApplicationManager(Activity activity){
        this.activity = activity;
    }

    public  boolean isFirstRun(){
        SharedPreferences preferences = activity.getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        Boolean response = preferences.getBoolean("firstRun",true);
        if(response){
            //Log event
            AnalyticsManager analyticsManager = AnalyticsManager.sharedInstance(activity.getApplication());
            analyticsManager.trackEvent(Constants.KEEN_EVENT_FRESHBOOT);
        }
        return response;
    }

    public void setFirstRun(boolean firstRun){
        SharedPreferences preferences = activity.getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        preferences.edit().putBoolean("firstRun",firstRun).apply();
    }

    /**
     * Retrieves the userId from the phone's settings
     * @return
     */
    public String getUserId(){
        SharedPreferences preferences = activity.getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        String userId = preferences.getString("userId", "");
        return userId;
    }

    /**
     * Saves the user id into the phone's settings
     * @param userId 
     */
    public void setUserId(String userId){
        SharedPreferences preferences = activity.getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        preferences.edit().putString("userId",userId).apply();

    }

    public void setUserName(String userName){
        SharedPreferences preferences = activity.getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        preferences.edit().putString("userName",userName).apply();
    }

    public String getUserName(){
        SharedPreferences preferences = activity.getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        String userId = preferences.getString("userName","");
        return userId;
    }


    /**
     * Returns the personal group id
     * @return
     */
    public String getUserPersonalClubId(){
        SharedPreferences preferences = activity.getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        String userPersonalClubId = preferences.getString("userPersonalClubId","");
        return userPersonalClubId;
    }

    /**
     * Saves the personal club id
     * @param userPersonalClubId personal club id
     */
    public void setUserPersonalClubId(String userPersonalClubId){
        SharedPreferences preferences = activity.getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        preferences.edit().putString("userPersonalClubId",userPersonalClubId).apply();
    }



}
