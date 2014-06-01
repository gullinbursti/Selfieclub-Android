package com.builtinmenlo.selfieclub.models;
import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.models.SCAsyncGet;

/**
 * Created by Leonardo on 5/29/14.
 */
public class UserActivity implements SCAsyncGetInterface
{
    private UserActivityProtocol userActivityProtocol;
    private SCAsyncGet scAsyncGet = null;


    public UserActivity (UserActivityProtocol userActivityProtocol){
        this.userActivityProtocol = userActivityProtocol;
        if(this.scAsyncGet == null){
            this.scAsyncGet = new SCAsyncGet(this);
        }
    }

    public void doRequest()
    {
        this.scAsyncGet.execute(Constants.API_ENDPOINT+"/users/getActivity?userID=131849");

    }


    public void didReceiveData(String data){
        this.userActivityProtocol.testMethod(data);
    }
    public void didReceiveError(String error){
        this.userActivityProtocol.testMethod(error);

    }


}
