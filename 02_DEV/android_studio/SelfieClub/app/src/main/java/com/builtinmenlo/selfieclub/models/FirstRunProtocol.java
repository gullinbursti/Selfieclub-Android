package com.builtinmenlo.selfieclub.models;

/**
 * Created by Leonardo on 7/15/14.
 */
public interface FirstRunProtocol {

    /*
    Interface methods for requesting a new user id
     */
    public void didReceiveFreeUserId(String userId);
    public void didFailReceivingFreeUserId(FirstRunManager.FIRSTRUN_ERROR errorType, String message);

    /*
    Interface methods for validating username/phone
     */
    public void didValideUsernamePhone(Boolean isValid, String message);
    public void didFailValidatingUsernamePhone(FirstRunManager.FIRSTRUN_ERROR errorType, String message);




}
