package com.builtinmenlo.selfieclub.models;

/**
 * Created by Leonardo on 7/21/14.
 */
public interface PINVerificationProtocol {
    /*
    Protocol methods for sending PIN
     */
    public void didSendPIN(Boolean result);
    public void didFailSendingPIN(String message);

    /*
    Protocol methods for validating PIN
     */

    public void didValidatePIN(Boolean result);
    public void didFailValidatingPIN(String message);


}
