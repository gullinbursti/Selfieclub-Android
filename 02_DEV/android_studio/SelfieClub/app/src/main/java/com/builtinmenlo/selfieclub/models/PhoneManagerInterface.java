package com.builtinmenlo.selfieclub.models;

import org.json.JSONArray;

/**
 * Created by Leonardo on 6/5/14.
 */
public interface PhoneManagerInterface {
    public void didReceiveMatchedNumbers(JSONArray arrayOfUsers);
    public void didReceiveMatchedNumbersError(String error);
}
