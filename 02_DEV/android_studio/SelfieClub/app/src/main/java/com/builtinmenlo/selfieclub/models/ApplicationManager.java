package com.builtinmenlo.selfieclub.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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
        return settings.getBoolean("firstRun",true);
    }

    public void setFirstRun(boolean firstRun){
        SharedPreferences settings = this.activity.getPreferences(Context.MODE_PRIVATE);
        settings.edit().putBoolean("firstRun",firstRun).apply();

    }
}
