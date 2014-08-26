package com.builtinmenlo.selfieclub.models;

import java.util.TreeMap;

/**
 * Created by Leonardo on 7/21/14.
 */
public interface CountryCodeProtocol {
    public void didReceiveContryCodes(TreeMap<String,String> codes);
    public void didFailReceivingContryCodes();
}
