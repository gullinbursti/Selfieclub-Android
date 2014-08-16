package com.builtinmenlo.selfieclub.models;

import android.util.Log;

import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.dataSources.ActivityItem;
import com.builtinmenlo.selfieclub.dataSources.Club;
import com.builtinmenlo.selfieclub.dataSources.Friend;
import com.builtinmenlo.selfieclub.dataSources.FriendsViewData;
import com.builtinmenlo.selfieclub.dataSources.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;


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

                        try {
                            ArrayList<Club> clubsList = parseUserClubs(data);
                            Collections.sort(clubsList,new Comparator<Club>() {
                                @Override
                                public int compare(Club lhs, Club rhs) {
                                    return rhs.getUpdated().compareTo(lhs.getUpdated());
                                }
                            });
                            if (userClubsProtocol != null)
                                userClubsProtocol.didReceiveUserClubs(clubsList);
                        } catch (JSONException e) {
                            if (userClubsProtocol != null)
                                userClubsProtocol.didReceiveUserClubsError(e.toString());
                        }

                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        if (userClubsProtocol != null)
                            userClubsProtocol.didReceiveUserClubsError(response);
                    }

                }
        );
    }

    public void requestOtherUserClubs(final OtherUserClubsProtocol userClubsProtocol, String userId){
        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID",userId);
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.GET_USERCLUBS_PATH,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {

                        try {
                            ArrayList<Club> clubsList = parseUserClubs(data);
                            Collections.sort(clubsList,new Comparator<Club>() {
                                @Override
                                public int compare(Club lhs, Club rhs) {
                                    return rhs.getUpdated().compareTo(lhs.getUpdated());
                                }
                            });
                            if (userClubsProtocol != null)
                                userClubsProtocol.didFindOtherUserClubs(clubsList);
                        } catch (JSONException e) {
                            if (userClubsProtocol != null)
                                userClubsProtocol.didFailOtherUserClubs(e.toString());
                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {
                        if (userClubsProtocol != null)
                            userClubsProtocol.didFailOtherUserClubs(e.toString());
                    }
                    @Override
                    public void onFailure(Throwable e, String response) {
                        if (userClubsProtocol != null)
                            userClubsProtocol.didFailOtherUserClubs(response);
                    }
                    @Override
                    public void onFailure(Throwable e, JSONArray errorResponse) {
                        if (userClubsProtocol != null)
                            userClubsProtocol.didFailOtherUserClubs(errorResponse.toString());
                    }


                }
        );
    }



    /**
     * Search for users by username
     * @param userFinderProtocol
     * @param username
     */
    public void findUserByUsername(final UserFinderProtocol userFinderProtocol, final String username){

        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username",username);
        data.put("action","1");
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.SEARCH_PATH,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONArray data) {
                        JSONObject result=null;
                        for(int i=0;i<data.length();i++){
                            try{
                                JSONObject item = data.getJSONObject(i);
                                String itemUsername = item.getString("username");
                                if(username.equalsIgnoreCase(itemUsername)){
                                    result=item;
                                    break;
                                }
                            }
                            catch (Exception e){
                                userFinderProtocol.didFailFindingUser(e.toString());
                            }

                        }
                        userFinderProtocol.didFindUser(result);
                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        userFinderProtocol.didFailFindingUser(response);
                    }

                }
        );
    }

    public void requestFriends(final UserFriendsProtocol userFriendsProtocol, String userId, String phoneNumbers){
        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID",userId);
        data.put("action","5");
        data.put("phone",phoneNumbers);
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.USER_PATH,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try{
                            Friend owner = new Friend();
                            owner.setUserId(data.getString("id"));
                            owner.setAvatarUrl(data.getString("avatar_url"));
                            owner.setUsername(data.getString("username"));
                            boolean smsVerified = data.getBoolean("sms_verified");
                            owner.setState(smsVerified?1:0);
                            JSONArray friendsArray = data.getJSONArray("friends");
                            ArrayList<Friend> friends = new ArrayList<Friend>();
                            for(int i=0 ; i<friendsArray.length();i++){
                                friends.add(parseFriend(friendsArray.getJSONObject(i)));
                            }
                            FriendsViewData friendsViewData = new FriendsViewData();
                            friendsViewData.setOwner(owner);
                            friendsViewData.setFriends(friends);
                            if (userFriendsProtocol != null)
                                userFriendsProtocol.didReceiveFriendsList(friendsViewData);
                            }
                        catch (Exception e){
                            if (userFriendsProtocol != null)
                                userFriendsProtocol.didReceiveFriendsListError(e.toString());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        if (userFriendsProtocol != null)
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
            switch (activityItem.getActivityType()){
                case 1:
                    activityItem.setMessage(user.getUsername()+" Verified");
                    break;
                case 3:
                    activityItem.setMessage(user.getUsername()+" has upvotes you into"+jsonObject.getString("club_name"));
                    break;
                case 5:
                    activityItem.setMessage(user.getUsername()+ " has reply a photo into "+jsonObject.getString("club_name"));
                    break;
                case 6:
                    activityItem.setMessage(user.getUsername()+ " has accepted your invite into "+jsonObject.getString("club_name"));
                    break;
                case 7:
                    activityItem.setMessage(user.getUsername()+ " leave "+jsonObject.getString("club_name"));
                    break;
                case 8:
                    activityItem.setMessage(user.getUsername()+ " invited you into "+jsonObject.getString("club_name"));
                    break;
                case 9:
                    activityItem.setMessage(user.getUsername()+ " submitted a photo into "+jsonObject.getString("club_name"));
                    break;
                case 10:
                    activityItem.setMessage(user.getUsername()+ " send a replay into "+jsonObject.getString("club_name"));
                    break;
                case 11:
                    activityItem.setMessage(user.getUsername()+ " joined  "+jsonObject.getString("club_name"));
            }

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

    private ArrayList<Club>parseUserClubs(JSONObject data) throws JSONException {
        ArrayList<Club> clubArray = new ArrayList<Club>();
        JSONArray memberArray = data.getJSONArray("member");
        JSONArray otherArray = data.getJSONArray("other");
        JSONArray pendingArray = data.getJSONArray("pending");
        JSONArray ownedArray = data.getJSONArray("owned");
        for(int i=0;i<memberArray.length();i++)
            clubArray.add(parseClub(memberArray.getJSONObject(i)));
        for(int i=0;i<otherArray.length();i++)
            clubArray.add(parseClub(otherArray.getJSONObject(i)));
        for(int i=0;i<pendingArray.length();i++)
            clubArray.add(parseClub(pendingArray.getJSONObject(i)));
        for (int i=0;i<ownedArray.length();i++)
            clubArray.add(parseClub(ownedArray.getJSONObject(i)));
        return clubArray;
    }


    private Club parseClub(JSONObject data){
        Club club = new Club();
        try {
            club.setClubId(data.getString("id"));
            club.setClubType(data.getString("club_type"));
            club.setClubName(data.getString("name"));
            club.setClubDescription(data.getString("description"));
            club.setClubImage(data.getString("img"));
            club.setClubTotalMembers(data.getString("total_members"));
            club.setAdded(data.getString("added"));
            club.setUpdated(data.getString("updated"));
            club.setClubOwner(parseUser(data.getJSONObject("owner")));
            club.setClubMembers(data.getJSONArray("members"));
            club.setClubPendingMembers(data.getJSONArray("pending"));
            club.setClubBlockedMembers(data.getJSONArray("blocked"));
            club.setClubSubmissions(data.getJSONArray("submissions"));
        } catch (JSONException e) {
            Log.e(this.getClass().getName(),"Error parsing the club object");
            e.printStackTrace();
        }
        finally {
            return club;
        }

    }

    private Friend parseFriend(JSONObject data){
        Friend friend = new Friend();
        try {
            JSONObject user = data.getJSONObject("user");
            friend.setUserId(user.getString("id"));
            friend.setUsername(user.getString("username"));
            friend.setAvatarUrl(user.getString("avatar_url"));
            friend.setState(data.getInt("state"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return friend;
    }




}
