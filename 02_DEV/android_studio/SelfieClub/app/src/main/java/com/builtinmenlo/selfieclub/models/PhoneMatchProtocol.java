package com.builtinmenlo.selfieclub.models;

import com.builtinmenlo.selfieclub.dataSources.User;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Leonardo on 6/5/14.
 */
public interface PhoneMatchProtocol {
    public void didReceiveMatchedNumbers(ArrayList<User> matchedNumbers);
    public void didReceiveMatchedNumbersError(String error);
}
