package com.builtinmenlo.selfieclub.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.fragments.FirstRunRegistrationFragment;

/**
 * Created by Leonardo on 7/9/14.
 */
public class ApplicationManager {

    Activity activity;

    public ApplicationManager(Activity activity){
        this.activity = activity;
    }

    public  boolean isFirstRun(){
        SharedPreferences settings = activity.getPreferences(Context.MODE_PRIVATE);
        Boolean response = settings.getBoolean("firstRun",true);
        if(response){
            //Log event
            KeenManager keenManager = KeenManager.sharedInstance();
            keenManager.initialize(activity.getApplicationContext());
            keenManager.trackEvent(Constants.KEEN_EVENT_FRESHBOOT);
        }
        return response;
    }

    public void setFirstRun(boolean firstRun){
        SharedPreferences settings = activity.getPreferences(Context.MODE_PRIVATE);
        settings.edit().putBoolean("firstRun",firstRun).apply();
    }

    /**
     * Retrieves the userId from the phone's settings
     * @return
     */
    public String getUserId(){
        SharedPreferences prefs = activity.getPreferences(Activity.MODE_PRIVATE);
        String userId = prefs.getString(FirstRunRegistrationFragment.EXTRA_ID, "");
        return userId;
    }

    /**
     * Saves the user id into the phone's settings
     * @param userId 
     */
    public void setUserId(String userId){
        SharedPreferences setting = activity.getPreferences(Context.MODE_PRIVATE);
        setting.edit().putString("userId",userId).apply();

    }

    public void setUserName(String userName){
        SharedPreferences setting = activity.getPreferences(Context.MODE_PRIVATE);
        setting.edit().putString("userName",userName).apply();
    }

    public String getUserName(){
        SharedPreferences settings = activity.getPreferences(Context.MODE_PRIVATE);
        String userId = settings.getString("userName","");
        return userId;
    }


    /**
     * Returns the personal group id
     * @return
     */
    public String getUserPersonalClubId(){
        SharedPreferences settings = activity.getPreferences(Context.MODE_PRIVATE);
        String userPersonalClubId = settings.getString("userPersonalClubId","");
        return userPersonalClubId;
    }

    /**
     * Saves the personal club id
     * @param userPersonalClubId personal club id
     */
    public void setUserPersonalClubId(String userPersonalClubId){
        SharedPreferences setting = activity.getPreferences(Context.MODE_PRIVATE);
        setting.edit().putString("userPersonalClubId",userPersonalClubId).apply();
        setting.edit().commit();
    }

}
