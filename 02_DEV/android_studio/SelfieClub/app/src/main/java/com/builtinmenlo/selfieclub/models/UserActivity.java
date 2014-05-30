package com.builtinmenlo.selfieclub.models;

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
        this.userActivityProtocol.testMethod("Hola mundo");
    }


}
