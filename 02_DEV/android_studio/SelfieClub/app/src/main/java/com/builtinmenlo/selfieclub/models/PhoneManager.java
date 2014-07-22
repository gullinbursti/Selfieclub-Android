package com.builtinmenlo.selfieclub.models;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;


import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.dataSources.User;
import com.builtinmenlo.selfieclub.listeners.CountryCodeProtocol;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Leonardo on 6/3/14.
 * This class implements the method for handling the user's contacts
 */
public class PhoneManager {

    private ContentResolver contentResolver;



    public PhoneManager(ContentResolver contentResolver){
        this.contentResolver = contentResolver;
    }

    public ArrayList<HashMap<String,String>> getContacts() {
        ArrayList<HashMap<String,String>> contactData=new ArrayList<HashMap<String,String>>();
        ContentResolver cr = this.contentResolver;
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        while (cursor.moveToNext()) {
            try{
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phones = cr.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null);
                    while (phones.moveToNext()) {
                        String phoneNumber = phones.getString(phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));
                        HashMap<String,String> map=new HashMap<String,String>();
                        map.put("name", name);
                        map.put("number", phoneNumber);
                        contactData.add(map);
                    }
                    phones.close();
                }
            }catch(Exception e){}
        }
        return contactData;
    }

    public void matchPhoneNumbers(final PhoneMatchProtocol phoneMatchInterface,String userId, String[] phoneNumbersArray){
        StringBuilder builder = new StringBuilder();
        for(String s : phoneNumbersArray){
            builder.append(s);
            builder.append("|");
        }
        String phoneNumbers = builder.toString();
        phoneNumbers = phoneNumbers.substring(0,phoneNumbers.length()-1);
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userID",userId);
        data.put("action","11");
        data.put("phone",phoneNumbers);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams(data);
        client.post(Constants.API_ENDPOINT+Constants.USER_PATH,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONArray data) {
                        ArrayList<User> matchedArray= new ArrayList<User>();
                        try{
                            for(int i=0; i<data.length();i++){
                                User user = parseUser(data.getJSONObject(0));
                                matchedArray.add(user);
                            }
                            phoneMatchInterface.didReceiveMatchedNumbers(matchedArray);
                        }
                        catch (Exception e){
                            phoneMatchInterface.didReceiveMatchedNumbersError(e.toString());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e, String response){
                        phoneMatchInterface.didReceiveMatchedNumbersError(response);
                    }

                }
        );
    }

    public void getCountryCodes(final CountryCodeProtocol countryCodeProtocol){
        TreeMap<String,String> countryCodes = new TreeMap<String, String>();
        countryCodes.put("Costa Rica","+506");
        countryCodes.put("United States","+1");
        countryCodes.put("Liechtenstein","+423");
        countryCodes.put("Australia","+61");
        countryCodeProtocol.didReceiveContryCodes(countryCodes);

    }

    private User parseUser(JSONObject jsonObject){
        try{
            User user = new User();
            user.setUsername(jsonObject.getString("username"));
            user.setUserId(jsonObject.getString("id"));
            user.setAvatarUrl(jsonObject.getString("avatar_url"));
            return user;
        }
        catch (Exception e){
            return null;
        }
    }




}
