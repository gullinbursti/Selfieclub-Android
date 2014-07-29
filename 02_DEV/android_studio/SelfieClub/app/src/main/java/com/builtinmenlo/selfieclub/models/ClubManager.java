package com.builtinmenlo.selfieclub.models;



import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.dataSources.Club;
import com.builtinmenlo.selfieclub.dataSources.NewsItem;
import com.builtinmenlo.selfieclub.dataSources.User;
import com.builtinmenlo.selfieclub.util.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Leonardo on 6/11/14.
 */
public class ClubManager {
    /**
     * Joins a user in the club
     * @param clubJoinProtocol The interface that must be implemented
     * @param userId User's id
     * @param clubId Club's id
     * @param ownedId Owner's id
     */
    public void joinClub(final ClubJoinProtocol clubJoinProtocol,
                         String userId,
                         String clubId,
                         String ownedId){
        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String,String> data = new HashMap<String, String>();
        data.put("userID",userId);
        data.put("clubID",clubId);
        data.put("ownerID",ownedId);
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.JOIN_CLUB_PATH,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try{
                            clubJoinProtocol.didJoinClub(data.getBoolean("result"));


                        }
                        catch (Exception e){
                            clubJoinProtocol.didFailJoiningClub(e.toString());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        clubJoinProtocol.didFailJoiningClub(response);
                    }

                }
        );
    }

    /**
     * Sends club invites
     * @param clubInviteProtocol The protocol that must be implemeted
     * @param userId User's id
     * @param clubId Club's id
     * @param users An array list with the friends user ids
     * @param nonUsers Delimited list of non-app contacts — specified by {F_NAME L_NAME}:::{PHONE}:: and delimited by   three pipes |||
     */
    public void sendClubInvite(final ClubInviteProtocol clubInviteProtocol,
                               String userId,
                               String clubId,
                               ArrayList<String>users,
                               ArrayList<HashMap<String,String>>nonUsers){
        String usersStr="";
        if(users.size()>0){
            usersStr=users.get(0);
            if(users.size()>1){
                for(int i=1;i<users.size();i++){
                    usersStr=usersStr+","+users.get(i);
                }
            }
        }
        String nonUsersStr="";
        if(nonUsers.size()>0){
            HashMap<String,String>buffer = nonUsers.get(0);
            nonUsersStr=buffer.get("name")+":::"+buffer.get("phone")+"::";
            if(nonUsers.size()>1){
                for(int i=1;i<nonUsers.size();i++){
                    buffer = nonUsers.get(i);
                    nonUsersStr=nonUsersStr+"|||"+buffer.get("name")+":::"+buffer.get("phone")+"::";
                }
            }
        }
        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String,String> data = new HashMap<String, String>();
        data.put("userID",userId);
        data.put("clubID",clubId);
        data.put("users",usersStr);
        data.put("nonUsers",nonUsersStr);
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.INVITE_CLUB_PATH,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try{
                            clubInviteProtocol.didSendCubInvite(data.getBoolean("result"));


                        }
                        catch (Exception e){
                            clubInviteProtocol.didFailSendingClubInvite(e.toString());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        clubInviteProtocol.didFailSendingClubInvite(response);
                    }

                }
        );




    }

    /**
     * Create's a new club
     * @param userId Owner id
     * @param clubName Club's name
     * @param clubDescription Club's description
     * @param clubImageUrl
     */
    public void createClub(final CreateClubProtocol createClubProtocol,String userId, String clubName, String clubDescription, String clubImageUrl){
        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String,String> data = new HashMap<String, String>();
        data.put("userID",userId);
        data.put("name",clubName);
        data.put("description",clubDescription);
        data.put("imgURL",clubImageUrl);
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.CREATE_CLUB_PATH,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try{
                            if(data!=null) {
                                createClubProtocol.didCreateClub();
                            }


                        }
                        catch (Exception e){
                            createClubProtocol.didFailCreatingClub(e.toString());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        createClubProtocol.didFailCreatingClub(response);
                    }

                }
        );

    }



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
     * @param imageFile The photo's file
     * @param subjects An arraylist with all the subjects
     */
    public void submitPhoto(final PhotoSubmissionProtocol photoSubmissionProtocol,
                            String userId,
                            String clubId,
                            File imageFile,
                            ArrayList<String>subjects){
        String filename = Util.generateRandomString(40);
        String imageUrl = Constants.AMAZON_S3_PATH+filename;
        uploadPhotoToS3(imageFile,filename);
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

    public void uploadPhotoToS3(File imageFile, String fileName){
        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(Constants.AMAZON_S3_KEY,Constants.AMAZON_S3_SECRET));
        s3Client.createBucket(Constants.AMAZON_S3_BUCKET);
        PutObjectRequest por = new PutObjectRequest(Constants.AMAZON_S3_BUCKET,fileName,imageFile);
        s3Client.putObject(por);
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

