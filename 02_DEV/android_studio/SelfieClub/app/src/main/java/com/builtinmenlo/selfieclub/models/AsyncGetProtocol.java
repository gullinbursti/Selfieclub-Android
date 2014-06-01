package com.builtinmenlo.selfieclub.models;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Leonardo on 5/31/14.
 */
public interface AsyncGetProtocol {
    public void didReceiveData(JSONArray data);
    public void didReceiveError(String error);
}
