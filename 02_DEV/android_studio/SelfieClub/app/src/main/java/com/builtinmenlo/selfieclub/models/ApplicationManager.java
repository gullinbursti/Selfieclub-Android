package com.builtinmenlo.selfieclub.models;

import android.app.Activity;
import android.content.Context;
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
        SharedPreferences settings = this.activity.getPreferences(Context.MODE_PRIVATE);
        Boolean response = settings.getBoolean("firstRun",true);
        if(response){
            //Log event
            KeenManager keenManager = KeenManager.sharedInstance();
            keenManager.initialize(this.activity.getApplicationContext());
            keenManager.trackEvent(Constants.KEEN_EVENT_FRESHBOOT);
        }
        return response;
    }

    public void setFirstRun(boolean firstRun){
        SharedPreferences settings = this.activity.getPreferences(Context.MODE_PRIVATE);
        settings.edit().putBoolean("firstRun",firstRun).apply();

    }
}
