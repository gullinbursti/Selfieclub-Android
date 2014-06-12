package com.builtinmenlo.selfieclub.models;

import android.util.Log;

import com.builtinmenlo.selfieclub.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

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
                    public void onSuccess(JSONArray data) {
                        try{
                            Log.w("","");
                        }
                        catch (Exception e){
                            clubInfoProtocol.didReceiveClubInfoError(e.toString());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        clubInfoProtocol.didReceiveClubInfoError(response);
                    }

                }
        );

    }
}
