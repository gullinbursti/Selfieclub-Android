package com.builtinmenlo.selfieclub.models;

import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.dataSources.ActivityItem;
import com.builtinmenlo.selfieclub.dataSources.User;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Leonardo on 5/29/14.
 */
public class UserActivity implements AsyncGetProtocol
{
    private UserActivityProtocol userActivityProtocol;
    private AsyncGet scAsyncGet = null;
    private ArrayList<ActivityItem> activityList;


    public UserActivity (UserActivityProtocol userActivityProtocol){
        this.userActivityProtocol = userActivityProtocol;
        if(this.scAsyncGet == null){
            this.scAsyncGet = new AsyncGet(this);
        }
        if(this.activityList == null){
            this.activityList = new ArrayList<ActivityItem>();
        }
    }

    public void requestUserActivity(String userId){
        this.scAsyncGet.execute(Constants.API_ENDPOINT+"/users/getActivity?userID="+userId);
    }

    public void requestUserActivity(String userId, String date){
        this.scAsyncGet.execute(Constants.API_ENDPOINT+"/users/getActivity?userID="+userId+"&lastUpdated="+date);
    }


    public void didReceiveData(JSONArray data){
        try{
            for(int i=0; i<data.length();i++){
                User user = parseUser(data.getJSONObject(0).getJSONObject("user"));
                this.activityList.add(parseActivityItem(data.getJSONObject(0),user));
            }
            this.userActivityProtocol.didReceiveUserActivity(this.activityList);
        }
        catch (Exception e){
            this.userActivityProtocol.didReceiveUserActivityError(e.toString());
        }

    }
    public void didReceiveError(String error){
        this.userActivityProtocol.didReceiveUserActivityError(error);

    }

    private ActivityItem parseActivityItem(JSONObject jsonObject, User user){
        ActivityItem activityItem = new ActivityItem();
        try {
            activityItem.setActivityItemId(jsonObject.getString("id"));
            activityItem.setActivityType(jsonObject.getInt("activity_type"));
            activityItem.setUser(user);
            activityItem.setTime(dateFromString(jsonObject.getString("time")));
            activityItem.setMessage(jsonObject.getString("message"));
            String type = jsonObject.getString("type");
            activityItem.setType(Integer.valueOf(type));
        }
        catch (Exception e){
            activityItem = null;
        }
        return activityItem;
    }
    private User parseUser(JSONObject jsonObject){
        try{
            User user = new User();
            user.setUsername(jsonObject.getString("username"));
            user.setUserId(jsonObject.getString("id"));
            user.setAvatarUrl(jsonObject.getString("avatar_url"));
            return user;
        }
        catch (Exception e){
            return null;
        }
    }

    private Date dateFromString(String currentDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try{
            date = formatter.parse(currentDate);
        }
        catch (ParseException e){
            date = null;
        }
        return date;
    }

}
