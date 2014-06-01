package com.builtinmenlo.selfieclub.models;

/**
 * Created by Leonardo on 5/31/14.
 */
public interface AsyncGetProtocol {
    public void didReceiveData(String data);
    public void didReceiveError(String error);
}
