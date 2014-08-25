package com.builtinmenlo.selfieclub.models;
import android.app.Activity;

import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.util.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by Leonardo on 7/8/14.
 */
public class FirstRunManager {



    public enum FIRSTRUN_ERROR{
        USER_LOOKUP_ERROR,
        USERNAME_PASSWORD_CHECK_ERROR,
        USERNAME_TAKEN,
        PHONE_TAKEN,
        USERNAME_AND_PHONE_TAKEN,
        USER_REGISTRATION_ERROR,
    }


    public void requestFreeUserId(final FirstRunProtocol firstRunProtocol,
                                  Activity activity){

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        if(Constants.USE_HMAC){
            asyncHttpClient.addHeader("HMAC", Util.generateHMAC(activity));
        }
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("action","1");
        RequestParams requestParams = new RequestParams(data);
        asyncHttpClient.post(Constants.API_ENDPOINT + Constants.USER_PATH, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try {
                            String userId = data.getString("id");
                            firstRunProtocol.didReceiveFreeUserId(userId);
                        } catch (Exception e) {
                            firstRunProtocol.didFailReceivingFreeUserId(FIRSTRUN_ERROR.USER_LOOKUP_ERROR,e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        firstRunProtocol.didFailReceivingFreeUserId(FIRSTRUN_ERROR.USER_LOOKUP_ERROR,response);
                    }

                }
        );
    }


    public void usernameAndPhoneCheck(final FirstRunProtocol firstRunProtocol,
                                      String userId,
                                      String username,
                                      String phoneNumber,
                                      Activity activity){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        if(Constants.USE_HMAC){
            asyncHttpClient.addHeader("HMAC", Util.generateHMAC(activity));
        }
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
                            switch (result){
                                case 0:
                                    firstRunProtocol.didValidateUsernamePhone(true,"Passed");
                                    break;
                                case 1:
                                    firstRunProtocol.didFailValidatingUsernamePhone(FIRSTRUN_ERROR.USERNAME_TAKEN,"Username taken");
                                    break;
                                case 2:
                                    firstRunProtocol.didFailValidatingUsernamePhone(FIRSTRUN_ERROR.PHONE_TAKEN,"Phone number taken");
                                    break;
                                case 3:
                                    firstRunProtocol.didFailValidatingUsernamePhone(FIRSTRUN_ERROR.USERNAME_AND_PHONE_TAKEN,"Username and phone taken");
                                    break;

                            }
                        } catch (Exception e) {
                            firstRunProtocol.didFailValidatingUsernamePhone(FIRSTRUN_ERROR.USERNAME_PASSWORD_CHECK_ERROR,e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        firstRunProtocol.didFailValidatingUsernamePhone(FIRSTRUN_ERROR.USERNAME_PASSWORD_CHECK_ERROR, response);
                    }

                }
        );
    }

    public void registerUser(final FirstRunProtocol firstRunProtocol,
                             String userId,
                             String username,
                             String email,
                             String pushNotificationToken,
                             String avatarUrl,
                             Activity activity){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("action","9");
        data.put("userID",userId);
        data.put("username",username);
        data.put("password",email);
        data.put("age","0000-00-00 00:00:00");
        data.put("token",pushNotificationToken);
        data.put("imgURL",avatarUrl);
        RequestParams requestParams = new RequestParams(data);
        asyncHttpClient.post(Constants.API_ENDPOINT + Constants.USER_REGISTRATION, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try {
                            String userId = data.getString("id");
                            firstRunProtocol.didRegisteredUser(userId);


                        } catch (Exception e) {
                            firstRunProtocol.didFailRegisteringUser(FIRSTRUN_ERROR.USER_REGISTRATION_ERROR,e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        firstRunProtocol.didFailRegisteringUser(FIRSTRUN_ERROR.USER_REGISTRATION_ERROR, response);
                    }
                }
        );
    }

    public void sendPIN(final PINVerificationProtocol pinVerificationProtocol,
                        String userId,
                        String phoneNumber,
                        Activity activity){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID",userId);
        data.put("phone",phoneNumber);
        RequestParams requestParams = new RequestParams(data);
        asyncHttpClient.post(Constants.API_ENDPOINT + Constants.UPDATE_PHONE, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try {
                            Boolean result = data.getBoolean("result");
                            if(result){
                                pinVerificationProtocol.didSendPIN(result);
                            }
                            else{
                                pinVerificationProtocol.didFailSendingPIN("Phone number is already registered to another user or user does not exist");
                            }


                        } catch (Exception e) {
                            pinVerificationProtocol.didFailSendingPIN(e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        pinVerificationProtocol.didFailSendingPIN(response);
                    }

                }
        );

    }

    public void validatePIN(final PINVerificationProtocol pinVerificationProtocol,
                            String userId,
                            String phone,
                            String pin,
                            Activity activity){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID",userId);
        data.put("phone",phone);
        data.put("pin",pin);
        RequestParams requestParams = new RequestParams(data);
        asyncHttpClient.post(Constants.API_ENDPOINT + Constants.UPDATE_PHONE, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try {
                            Boolean result = data.getBoolean("result");
                            if(result){
                                pinVerificationProtocol.didValidatePIN(result);
                            }
                            else{
                                pinVerificationProtocol.didFailValidatingPIN("PIN validation failed");
                            }


                        } catch (Exception e) {
                            pinVerificationProtocol.didFailSendingPIN(e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        pinVerificationProtocol.didFailValidatingPIN(response);
                    }

                }
        );

    }
}
