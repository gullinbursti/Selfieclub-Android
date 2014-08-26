package com.builtinmenlo.selfieclub.models;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.provider.ContactsContract;


import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.dataSources.User;
import com.builtinmenlo.selfieclub.util.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xmlwise.Plist;

/**
 * Created by Leonardo on 6/3/14.
 * This class implements the method for handling the user's contacts
 */
public class PhoneManager {

    /**
     * Retuens the number list of contacts stored in the phone's address book
     * @param contentResolver The caller activity's ContentResolver
     * @return An array list with the contacts
     */
    public ArrayList<HashMap<String,String>> getContacts(ContentResolver contentResolver) {
        ArrayList<HashMap<String,String>> contactData=new ArrayList<HashMap<String,String>>();
        ContentResolver cr = contentResolver;
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

    public String getContactsPhones(ContentResolver contentResolver){
        ArrayList<HashMap<String,String>> contacts = getContacts(contentResolver);
        String strContacts="";
        if(contacts.size()>0){
            strContacts = contacts.get(0).get("number");
        }
        for(int i=1;i<contacts.size();i++){
            HashMap<String,String> contact = contacts.get(i);
            strContacts = strContacts + "|" + contact.get("number");
        }
        return strContacts;
    }

    /**
     * Matches the list of phone numbers with the user's id
     * @param phoneMatchInterface The interface that must be implemented
     * @param userId User's id
     * @param phoneNumbersArray The array of phone numbers separated by |
     */
    public void matchPhoneNumbers(final PhoneMatchProtocol phoneMatchInterface,
                                  String userId,
                                  String[] phoneNumbersArray,
                                  Activity activity){
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
        if(Constants.USE_HMAC){
            client.addHeader("HMAC", Util.generateHMAC(activity));
        }
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

    /**
     * Returns an array list with all the country phone codes
     * @param context
     * @return An array list with all the country phone codes
     */
    public ArrayList<HashMap<String,String>> getCountryCodes(Context context){
        AssetManager assetManager = context.getResources().getAssets();
        InputStream inputStream;
        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        Map<String, Object> properties;
        String xml;
        ArrayList listOfCountries=null;
        try {
            inputStream = assetManager.open("CountryCodes.plist");
            br = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line=br.readLine())!=null) {
                sb.append(line);
            }
            xml = sb.toString();
            properties = Plist.fromXml(xml);
            listOfCountries = (ArrayList<HashMap<String,String>>)properties.get("data");

        }
        catch (Exception e){

        }

        return listOfCountries;

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
