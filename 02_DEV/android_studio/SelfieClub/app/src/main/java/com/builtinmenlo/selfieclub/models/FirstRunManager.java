package com.builtinmenlo.selfieclub.models;

import android.util.Log;

import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.dataSources.ActivityItem;
import com.builtinmenlo.selfieclub.dataSources.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Leonardo on 7/10/14.
 */
public class FirstRunManager {

    public enum FIRSTRUN_ERROR{
        USER_LOOKUP_ERROR,
        USERNAME_PASSWORD_CHECK_ERROR,
        USERNAME_TAKEN,
        PHONE_TAKEN,
        USERNAME_AND_PHONE_TAKEN
    }


    public void registerUser(final String username , final String phoneNumber){

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("action","1");
        RequestParams requestParams = new RequestParams(data);
    }


    private void usernameAndPhoneCheck(String userId,String username, String phoneNumber){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID",userId);
        data.put("username",username);
        data.put("password",phoneNumber);
        RequestParams requestParams = new RequestParams(data);
        asyncHttpClient.post(Constants.API_ENDPOINT + Constants.CHECK_USERNAMEPASSWORD_PATH, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try {
                            int result = data.getInt("result");
                        } catch (Exception e) {
                            onError(FIRSTRUN_ERROR.USERNAME_PASSWORD_CHECK_ERROR,e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        onError(FIRSTRUN_ERROR.USERNAME_PASSWORD_CHECK_ERROR, response);
                    }

                }
        );
    }




    private void onError(FIRSTRUN_ERROR errorType, String message ){

    }




}
