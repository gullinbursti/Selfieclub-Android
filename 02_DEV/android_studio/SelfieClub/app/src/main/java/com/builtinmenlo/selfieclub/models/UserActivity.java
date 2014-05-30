package com.builtinmenlo.selfieclub.models;

import android.content.res.Resources;

import com.builtinmenlo.selfieclub.Constants;

/**
 * Created by Leonardo on 5/29/14.
 */
public class UserActivity
{
    private UserActivityProtocol userActivityProtocol;


    public UserActivity (UserActivityProtocol userActivityProtocol){
        this.userActivityProtocol = userActivityProtocol;
    }

    public void doRequest()
    {

        this.userActivityProtocol.testMethod(Constants.API_ENDPOINT);
    }


}
