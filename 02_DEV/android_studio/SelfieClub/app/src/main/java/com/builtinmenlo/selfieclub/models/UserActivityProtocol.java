package com.builtinmenlo.selfieclub.models;

import com.builtinmenlo.selfieclub.dataSources.ActivityItem;
import java.util.ArrayList;


public interface UserActivityProtocol
{
    public void didReceiveUserActivity(ArrayList<ActivityItem> activityList);
    public void didReceiveUserActivityError(String error);
}
