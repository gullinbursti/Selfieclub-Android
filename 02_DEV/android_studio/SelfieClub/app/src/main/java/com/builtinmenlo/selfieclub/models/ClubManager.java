package com.builtinmenlo.selfieclub.models;


import android.app.Activity;
import android.util.Log;

import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.dataSources.Club;
import com.builtinmenlo.selfieclub.dataSources.NewsItem;
import com.builtinmenlo.selfieclub.dataSources.User;
import com.builtinmenlo.selfieclub.util.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Leonardo on 6/11/14.
 */
public class ClubManager {
    /**
     * Joins a user in the club
     *
     * @param clubJoinProtocol The interface that must be implemented
     * @param userId           User's id
     * @param clubId           Club's id
     * @param ownedId          Owner's id
     */
    public void joinClub(final ClubJoinProtocol clubJoinProtocol,
                         String userId,
                         String clubId,
                         String ownedId,
                         final Activity activity) {
        AsyncHttpClient client = new AsyncHttpClient();
        if(Constants.USE_HMAC){
            client.addHeader("HMAC",Util.generateHMAC(activity));
        }
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID", userId);
        data.put("clubID", clubId);
        data.put("ownerID", ownedId);
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT + Constants.JOIN_CLUB_PATH, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try {
                            //track event
                            KeenManager keenManager = KeenManager.sharedInstance(activity.getApplicationContext());
                            keenManager.trackEvent(Constants.KEEN_EVENT_JOINCLUB);

                            clubJoinProtocol.didJoinClub(data.getBoolean("result"));


                        } catch (Exception e) {
                            clubJoinProtocol.didFailJoiningClub(e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        clubJoinProtocol.didFailJoiningClub(response);
                    }

                }
        );
    }

    /**
     * Sends club invites
     *
     * @param clubInviteProtocol The protocol that must be implemeted
     * @param userId             User's id
     * @param clubId             Club's id
     * @param users              An array list with the friends user ids
     * @param nonUsers           Delimited list of non-app contacts — specified by {F_NAME L_NAME}:::{PHONE}:: and delimited by   three pipes |||
     */
    public void sendClubInvite(final ClubInviteProtocol clubInviteProtocol,
                               String userId,
                               String clubId,
                               ArrayList<String> users,
                               ArrayList<HashMap<String, String>> nonUsers,
                               final Activity activity) {
        String usersStr = "";
        if (users.size() > 0) {
            usersStr = users.get(0);
            if (users.size() > 1) {
                for (int i = 1; i < users.size(); i++) {
                    usersStr = usersStr + "," + users.get(i);
                }
            }
        }
        String nonUsersStr = "";
        if (nonUsers.size() > 0) {
            HashMap<String, String> buffer = nonUsers.get(0);
            nonUsersStr = buffer.get("name") + ":::" + buffer.get("number") + ":::";
            if (nonUsers.size() > 1) {
                for (int i = 1; i < nonUsers.size(); i++) {
                    buffer = nonUsers.get(i);
                    nonUsersStr = nonUsersStr + "|||" + buffer.get("name") + ":::" + buffer.get("number") + ":::";
                }
            }
        }
        AsyncHttpClient client = new AsyncHttpClient();
        if(Constants.USE_HMAC){
            client.addHeader("HMAC",Util.generateHMAC(activity));
        }
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID", userId);
        data.put("clubID", clubId);
        data.put("users", usersStr);
        data.put("nonUsers", nonUsersStr);
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT + Constants.INVITE_CLUB_PATH, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try {
                            //track the event
                            KeenManager keenManager = KeenManager.sharedInstance(activity.getApplicationContext());
                            keenManager.trackEvent(Constants.KEEN_EVENT_INVITECLUB);


                            clubInviteProtocol.didSendCubInvite(data.getBoolean("result"));


                        } catch (Exception e) {
                            clubInviteProtocol.didFailSendingClubInvite(e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        clubInviteProtocol.didFailSendingClubInvite(response);
                    }

                }
        );
    }

    /**
     * Create's a new club
     *
     * @param userId          Owner id
     * @param clubName        Club's name
     * @param clubDescription Club's description
     * @param clubImageUrl
     */
    public void createClub(final CreateClubProtocol createClubProtocol,
                           String userId,
                           String clubName,
                           String clubDescription,
                           String clubImageUrl,
                           final Activity activity) {
        AsyncHttpClient client = new AsyncHttpClient();
        if(Constants.USE_HMAC){
            client.addHeader("HMAC",Util.generateHMAC(activity));
        }
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID", userId);
        data.put("name", clubName);
        data.put("description", clubDescription);
        data.put("imgURL", clubImageUrl);
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT + Constants.CREATE_CLUB_PATH, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try {
                            if (data != null) {
                                KeenManager keenManager = KeenManager.sharedInstance(activity.getApplicationContext());
                                keenManager.trackEvent(Constants.KEEN_EVENT_CREATECLUB);
                                createClubProtocol.didCreateClub(data.getString("id"),data.getString("name"));
                            }


                        } catch (Exception e) {
                            createClubProtocol.didFailCreatingClub(e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        createClubProtocol.didFailCreatingClub(response);
                    }

                }
        );

    }

    /**
     * Request the club's info
     *
     * @param clubInfoProtocol The interface that must be implemented
     * @param userId           User's id
     * @param clubId           Club's id
     */
    public void requestClubInfo(final ClubInfoProtocol clubInfoProtocol,
                                String userId,
                                String clubId,
                                Activity activity) {
        AsyncHttpClient client = new AsyncHttpClient();
        if(Constants.USE_HMAC){
            client.addHeader("HMAC",Util.generateHMAC(activity));
        }
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID", userId);
        data.put("clubID", clubId);
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT + Constants.GET_CLUB_INFO, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try {
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
                            if (clubInfoProtocol != null)
                                clubInfoProtocol.didReceiveClubInfo(club);

                        } catch (Exception e) {
                            if (clubInfoProtocol != null)
                                clubInfoProtocol.didReceiveClubInfoError(e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        if (clubInfoProtocol != null)
                            clubInfoProtocol.didReceiveClubInfoError(response);
                    }

                }
        );

    }

    /**
     * Request the user's news
     *
     * @param newsFeedProtocol The interface that must be implemented
     * @param userId           User's id
     */
    public void requestNews(final NewsFeedProtocol newsFeedProtocol,
                            String userId,
                            Activity activity) {
        AsyncHttpClient client = new AsyncHttpClient();
        if(Constants.USE_HMAC){
            client.addHeader("HMAC",Util.generateHMAC(activity));
        }
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID", userId);

        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT + Constants.GET_USERCLUBS_PATH, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        ArrayList<NewsItem> newsFeedArray = new ArrayList<NewsItem>();
                        JSONObject object;
                        JSONArray submissionsArray;
                        try {
                            JSONArray memberArray = data.getJSONArray("member");
                            for (int i = 0; i < memberArray.length(); i++) {
                                object = memberArray.getJSONObject(i);
                                submissionsArray = object.getJSONArray("submissions");
                                for (int j = 0; j < submissionsArray.length(); j++) {
                                    newsFeedArray.add(parseNewsItem(submissionsArray.getJSONObject(j), object.getString("name")));
                                }
                            }

                            JSONArray otherArray = data.getJSONArray("other");
                            for (int i = 0; i < otherArray.length(); i++) {
                                object = otherArray.getJSONObject(i);
                                submissionsArray = object.getJSONArray("submissions");
                                for (int j = 0; j < submissionsArray.length(); j++) {
                                    newsFeedArray.add(parseNewsItem(submissionsArray.getJSONObject(j), object.getString("name")));
                                }
                            }

                            JSONArray pendingArray = data.getJSONArray("pending");
                            for (int i = 0; i < pendingArray.length(); i++) {
                                object = pendingArray.getJSONObject(i);
                                submissionsArray = object.getJSONArray("submissions");
                                for (int j = 0; j < submissionsArray.length(); j++) {
                                    newsFeedArray.add(parseNewsItem(submissionsArray.getJSONObject(j), object.getString("name")));
                                }
                            }


                            JSONArray ownedArray = data.getJSONArray("owned");
                            for (int i = 0; i < ownedArray.length(); i++) {
                                object = ownedArray.getJSONObject(i);
                                submissionsArray = object.getJSONArray("submissions");
                                for (int j = 0; j < submissionsArray.length(); j++) {
                                    newsFeedArray.add(parseNewsItem(submissionsArray.getJSONObject(j), object.getString("name")));
                                }
                            }
                            Collections.sort(newsFeedArray, new Comparator<NewsItem>() {
                                @Override
                                public int compare(NewsItem lhs, NewsItem rhs) {
                                    return rhs.getTimestamp().compareTo(lhs.getTimestamp());
                                }
                            });
                            newsFeedProtocol.didReceiveNewsFeed(newsFeedArray);

                        } catch (Exception e) {
                            newsFeedProtocol.didReceiveNewsFeedError(e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        newsFeedProtocol.didReceiveNewsFeedError(response);
                    }

                }
        );

    }

    /**
     * Submit a photo into a club
     *
     * @param clubPhotoSubmissionProtocol The interface that must be implemented
     * @param activity                    Activity context. Used to grab de UIID
     * @param userId                      User's id
     * @param clubId                      Clubs's id
     * @param imageFile                   The photo's file
     * @param subjects                    An arraylist with all the subjects
     */
    public void submitPhoto(final ClubPhotoSubmissionProtocol clubPhotoSubmissionProtocol,
                            Activity activity,
                            String userId,
                            String clubId,
                            byte[] imageFile,
                            ArrayList<String> subjects) {
        String uniqueString = Util.generateUniqueString(activity);
        final String imageUrl = Constants.AMAZON_S3_PATH + uniqueString;
        //Upload large image
        String largeFilename = uniqueString + "Large_640x1136.jpg";
        File largeImage = Util.resizeImage(Util.IMAGE_SIZES.LARGE_640x1136, imageFile, activity);
        Util.uploadPhotoToS3(largeImage, largeFilename);
        //Upload tab image
        String tabFilename = uniqueString + "Tab_640x960.jpg";
        File tabImage = Util.resizeImage(Util.IMAGE_SIZES.TAB_640x960, imageFile, activity);
        Util.uploadPhotoToS3(tabImage, tabFilename);
        //Notify the server
        JSONArray emotionsJson = new JSONArray(subjects);
        AsyncHttpClient client = new AsyncHttpClient();
        if(Constants.USE_HMAC){
            client.addHeader("HMAC",Util.generateHMAC(activity));
        }
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID", userId);
        data.put("clubID", clubId);
        data.put("imgURL", imageUrl);
        data.put("subjects", emotionsJson.toString());
        data.put("challengeID", "0");
        data.put("subject", "");
        data.put("targets", "");
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT + Constants.CLUB_PHOTO_SUBMIT, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try {
                            String id = data.getString("id");
                            if (id != null) {
                                AsyncHttpClient client1 = new AsyncHttpClient();
                                HashMap<String, String> data1 = new HashMap<String, String>();
                                data1.put("imageURL", imageUrl);
                                RequestParams requestParams1 = new RequestParams(data1);
                                client1.post(Constants.API_ENDPOINT + Constants.CLUB_PHOTO_SUBMIT_VALIDATION, requestParams1, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, String content) {
                                                super.onSuccess(statusCode, content);
                                                clubPhotoSubmissionProtocol.didSubmittedPhotoInClub(true);
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
                                                super.onFailure(statusCode, headers, error, content);
                                                clubPhotoSubmissionProtocol.didFailSubmittingPhotoInClub(content);
                                            }

                                        }
                                );

                            } else
                                clubPhotoSubmissionProtocol.didSubmittedPhotoInClub(false);
                        } catch (Exception e) {
                            clubPhotoSubmissionProtocol.didFailSubmittingPhotoInClub(e.toString());
                        }

                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        clubPhotoSubmissionProtocol.didFailSubmittingPhotoInClub(response);
                    }
                }
        );
    }



    private NewsItem parseNewsItem(JSONObject data, String clubname) {
        NewsItem newsItem = new NewsItem();
        try {
            newsItem.setUserName(data.getString("username"));
            newsItem.setAvatarUrl(data.getString("avatar"));
            newsItem.setClubName(clubname);
            newsItem.setImageUrl(data.getString("img"));
            newsItem.setTimestamp(data.getString("added"));
            newsItem.setStatus(data.getJSONArray("subjects"));
        } catch (Exception e) {
        }


        return newsItem;
    }


    private User parseUser(JSONObject jsonObject) {
        try {
            User user = new User();
            user.setUsername(jsonObject.getString("username"));
            user.setUserId(jsonObject.getString("id"));
            user.setAvatarUrl(jsonObject.getString("avatar"));
            return user;
        } catch (Exception e) {
            Log.w("ClubManager", e.toString());
            return null;
        }
    }
}

