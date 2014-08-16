package com.builtinmenlo.selfieclub.models;

import com.builtinmenlo.selfieclub.dataSources.User;

import org.json.JSONObject;

/**
 * Created by Leonardo on 8/14/14.
 */
public interface UserFinderProtocol {
    public void didFindUser(JSONObject user);
    public void didFailFindingUser(String errorMessage);
}
