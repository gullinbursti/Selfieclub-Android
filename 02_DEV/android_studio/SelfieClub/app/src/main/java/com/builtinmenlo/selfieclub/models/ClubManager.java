package com.builtinmenlo.selfieclub.models;



import android.util.Log;

import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.dataSources.Club;
import com.builtinmenlo.selfieclub.dataSources.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Leonardo on 6/11/14.
 */
public class ClubManager {
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
