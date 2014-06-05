package com.builtinmenlo.selfieclub.models;

import org.json.JSONArray;

/**
 * Created by Leonardo on 6/4/14.
 */
public interface AsycPostProtocol {
    public void didReceiveData(JSONArray data);
    public void didReceiveError(String error);
}
