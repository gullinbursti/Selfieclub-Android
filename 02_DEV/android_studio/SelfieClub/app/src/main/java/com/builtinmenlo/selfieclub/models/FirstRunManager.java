package com.builtinmenlo.selfieclub.models;
import com.builtinmenlo.selfieclub.Constants;
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
        USERNAME_AND_PHONE_TAKEN
    }


    public void requestFreeUserId(final FirstRunProtocol firstRunProtocol){

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
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


    private void usernameAndPhoneCheck(final FirstRunProtocol firstRunProtocol, String userId,String username, String phoneNumber){
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
                            switch (result){
                                case 0:
                                    firstRunProtocol.didValideUsernamePhone(true,"Passed");
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





}
