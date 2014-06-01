package com.builtinmenlo.selfieclub.models;
import com.builtinmenlo.selfieclub.Constants;

/**
 * Created by Leonardo on 5/29/14.
 */
public class UserActivity implements AsyncGetProtocol
{
    private UserActivityProtocol userActivityProtocol;
    private AsyncGet scAsyncGet = null;


    public UserActivity (UserActivityProtocol userActivityProtocol){
        this.userActivityProtocol = userActivityProtocol;
        if(this.scAsyncGet == null){
            this.scAsyncGet = new AsyncGet(this);
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
