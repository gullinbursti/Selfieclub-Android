package com.builtinmenlo.selfieclub.models;

import com.builtinmenlo.selfieclub.dataSources.ActivityItem;
import java.util.ArrayList;


public interface UserActivityProtocol
{
    /**
     * Called when the activity list have been fetched from the server
     * @param activityList An array list of ActivityItem objects
     */
    public void didReceiveUserActivity(ArrayList<ActivityItem> activityList);

    /**
     * Called if an error happens while fetching the activity list
     * @param error The error's message
     */
    public void didReceiveUserActivityError(String error);
}
