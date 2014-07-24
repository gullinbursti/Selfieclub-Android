package com.builtinmenlo.selfieclub.models;



import android.util.Log;

import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.dataSources.Club;
import com.builtinmenlo.selfieclub.dataSources.NewsItem;
import com.builtinmenlo.selfieclub.dataSources.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Leonardo on 6/11/14.
 */
public class ClubManager {
    /**
     * Request the club's info
     * @param clubInfoProtocol The interface that must be implemented
     * @param userId User's id
     * @param clubId Club's id
     */
    public void requestClubInfo(final ClubInfoProtocol clubInfoProtocol, String userId, String clubId ){
        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID",userId);
        data.put("clubID",clubId);
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.GET_CLUB_INFO,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try{
                            Club club = new Club();
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
                            if (clubInfoProtocol!=null)
                                clubInfoProtocol.didReceiveClubInfo(club);

                        }
                        catch (Exception e){
                            if (clubInfoProtocol!=null)
                                clubInfoProtocol.didReceiveClubInfoError(e.toString());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        if (clubInfoProtocol!=null)
                            clubInfoProtocol.didReceiveClubInfoError(response);
                    }

                }
        );

    }

    /**
     * Request the user's news
     * @param newsFeedProtocol The interface that must be implemented
     * @param userId User's id
     */
    public void requestNews(final NewsFeedProtocol newsFeedProtocol, String userId){
        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID",userId);

        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.GET_USERCLUBS_PATH,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        ArrayList<NewsItem>newsFeedArray = new ArrayList<NewsItem>();
                        JSONObject object;
                        JSONArray submissionsArray;
                        try{
                            JSONArray memberArray = data.getJSONArray("member");
                            for(int i=0;i<memberArray.length();i++){
                                object = memberArray.getJSONObject(i);
                                submissionsArray = object.getJSONArray("submissions");
                                for(int j=0;j<submissionsArray.length();j++){
                                    newsFeedArray.add(parseNewsItem(submissionsArray.getJSONObject(j),object.getString("name")));
                                }
                            }

                            JSONArray otherArray = data.getJSONArray("other");
                            for(int i=0;i<otherArray.length();i++){
                                object = otherArray.getJSONObject(i);
                                submissionsArray = object.getJSONArray("submissions");
                                for(int j=0;j<submissionsArray.length();j++){
                                    newsFeedArray.add(parseNewsItem(submissionsArray.getJSONObject(j),object.getString("name")));
                                }
                            }

                            JSONArray pendingArray = data.getJSONArray("pending");
                            for(int i=0;i<pendingArray.length();i++){
                                object = pendingArray.getJSONObject(i);
                                submissionsArray = object.getJSONArray("submissions");
                                for(int j=0;j<submissionsArray.length();j++){
                                    newsFeedArray.add(parseNewsItem(submissionsArray.getJSONObject(j),object.getString("name")));
                                }
                            }


                            JSONArray ownedArray = data.getJSONArray("owned");
                            for(int i=0;i<ownedArray.length();i++){
                                object = ownedArray.getJSONObject(i);
                                submissionsArray = object.getJSONArray("submissions");
                                for(int j=0;j<submissionsArray.length();j++){
                                    newsFeedArray.add(parseNewsItem(submissionsArray.getJSONObject(j),object.getString("name")));
                                }
                            }
                            newsFeedProtocol.didReceiveNewsFeed(newsFeedArray);

                        }
                        catch (Exception e){
                            newsFeedProtocol.didReceiveNewsFeedError(e.toString());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        newsFeedProtocol.didReceiveNewsFeedError(response);
                    }

                }
        );

    }

    /**
     * Submit a photo into a club
     * @param photoSubmissionProtocol The interface that must be implemented
     * @param userId User's id
     * @param clubId Clubs's id
     * @param imageUrl Photo url's
     * @param subjects An arraylist with all the subjects
     */
    public void submitPhoto(final PhotoSubmissionProtocol photoSubmissionProtocol,
                            String userId,
                            String clubId,
                            String imageUrl,
                            ArrayList<String>subjects){
        JSONArray emotionsJson = new JSONArray(subjects);
        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID",userId);
        data.put("clubID",clubId);
        data.put("imgURL",imageUrl);
        data.put("subjects",emotionsJson.toString());
        data.put("challengeID","0");
        data.put("subject","");
        data.put("targets","");
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.PHOTO_SUBMIT_PATH,requestParams,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject data) {
                try{
                    String id = data.getString("id");
                    if(id!=null)
                        photoSubmissionProtocol.didSubmittedPhotoInClub(true);
                    else
                        photoSubmissionProtocol.didSubmittedPhotoInClub(false);
                }
                catch (Exception e){
                        photoSubmissionProtocol.didFailSubmittingPhotoInClub(e.toString());
                }

            }

            @Override
            public void onFailure(Throwable e, String response) {
                photoSubmissionProtocol.didFailSubmittingPhotoInClub(response);
            }
        }
        );

    }

    private NewsItem parseNewsItem (JSONObject data,String clubname){
        NewsItem newsItem = new NewsItem();
        try{
            newsItem.setUserName(data.getString("username"));
            newsItem.setAvatarUrl(data.getString("avatar"));
            newsItem.setClubName(clubname);
            newsItem.setImageUrl(data.getString("img"));
            newsItem.setTimestamp(data.getString("added"));
            newsItem.setStatus(data.getJSONArray("subjects"));
        }
        catch (Exception e){}


        return newsItem;
    }


    private User parseUser(JSONObject jsonObject){
        try{
            User user = new User();
            user.setUsername(jsonObject.getString("username"));
            user.setUserId(jsonObject.getString("id"));
            user.setAvatarUrl(jsonObject.getString("avatar"));
            return user;
        }
        catch (Exception e){
            Log.w("ClubManager",e.toString());
            return null;
        }
    }
}

