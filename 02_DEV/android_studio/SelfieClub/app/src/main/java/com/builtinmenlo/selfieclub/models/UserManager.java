package com.builtinmenlo.selfieclub.models;

import android.util.Log;

import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.dataSources.ActivityItem;
import com.builtinmenlo.selfieclub.dataSources.User;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.loopj.android.http.*;


/**
 * Created by Leonardo on 5/29/14.
 */
public class UserManager
{



    public UserManager(){

    }

    /**
     * Request the user's activity from the server. Needs to implement the UserActivityProtocol
     * @param userId The user's id
     */
    public void requestUserActivity(final UserActivityProtocol userActivityProtocol, String userId){

        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID",userId);
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.GET_ACTIVITY_PATH,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONArray data) {
                        ArrayList<ActivityItem> activityList = new ArrayList<ActivityItem>();
                        try{
                            for(int i=0; i<data.length();i++){
                                User user = parseUser(data.getJSONObject(0).getJSONObject("user"));
                                ActivityItem activityItem = parseActivityItem(data.getJSONObject(0), user);
                                activityList.add(activityItem);
                            }
                            userActivityProtocol.didReceiveUserActivity(activityList);
                        }
                        catch (Exception e){
                            userActivityProtocol.didReceiveUserActivityError(e.toString());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        userActivityProtocol.didReceiveUserActivityError(response);
                    }

                }
                );
    }

    /**
     * Request the user's activity from the server. Needs to implement the UserActivityProtocol
     * @param userId The user's id
     * @param date Date/time in format “YYYY:MM:DD HH:MM:SS”
     */
    public void requestUserActivity(final UserActivityProtocol userActivityProtocol, String userId, String date){

        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID",userId);
        data.put("lastUpdated",date);
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.GET_ACTIVITY_PATH,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONArray data) {
                        ArrayList<ActivityItem> activityList = new ArrayList<ActivityItem>();
                        try{
                            for(int i=0; i<data.length();i++){
                                User user = parseUser(data.getJSONObject(0).getJSONObject("user"));
                                ActivityItem activityItem = parseActivityItem(data.getJSONObject(0), user);
                                activityList.add(activityItem);
                            }
                            userActivityProtocol.didReceiveUserActivity(activityList);
                        }
                        catch (Exception e){
                            userActivityProtocol.didReceiveUserActivityError(e.toString());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        userActivityProtocol.didReceiveUserActivityError(response);
                    }

                }
        );
    }

    /**
     * Requests the clubs related to a user. Needs to implement UserClubsProtocol
     * @param userClubsProtocol
     * @param userId
     */
    public void requestUserClubs(final UserClubsProtocol userClubsProtocol, String userId){
        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID",userId);
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.GET_USERCLUBS_PATH,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        userClubsProtocol.didReceiveUserClubs(data);
                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        userClubsProtocol.didReceiveUserClubsError(response);
                    }

                }
        );
    }

    public void requestFriends(final UserFriendsProtocol userFriendsProtocol, String userId){
        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID",userId);
        data.put("action","5");
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.USER_PATH,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        ArrayList<User> activityList = new ArrayList<User>();
                        try{
                                User user = parseUser(data);
                                activityList.add(user);
                                userFriendsProtocol.didReceiveFriendsList(activityList);
                            }


                        catch (Exception e){
                            userFriendsProtocol.didReceiveFriendsListError(e.toString());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        userFriendsProtocol.didReceiveFriendsListError(response);
                    }

                }
        );

    }

    private ActivityItem parseActivityItem(JSONObject jsonObject, User user){
        ActivityItem activityItem = new ActivityItem();
        try {
            activityItem.setActivityItemId(jsonObject.getString("id"));
            activityItem.setActivityType(jsonObject.getInt("activity_type"));
            activityItem.setUser(user);
            activityItem.setTime(dateFromString(jsonObject.getString("time")));

        }
        catch (Exception e){
            Log.e("UserManager",e.toString());
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
