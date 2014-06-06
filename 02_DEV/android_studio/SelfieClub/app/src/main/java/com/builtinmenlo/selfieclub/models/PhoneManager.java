package com.builtinmenlo.selfieclub.models;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;


import com.builtinmenlo.selfieclub.Constants;
import com.builtinmenlo.selfieclub.dataSources.User;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Leonardo on 6/3/14.
 * This class implements the method for handling the user's contacts
 */
public class PhoneManager implements AsycPostProtocol{

    private ContentResolver contentResolver;
    private PhoneManagerInterface phoneManagerInterface;
    private AsycPost asycPost;

    public PhoneManager(PhoneManagerInterface phoneManagerInterface, ContentResolver contentResolver){
        this.contentResolver = contentResolver;
        this.phoneManagerInterface = phoneManagerInterface;
        if(this.asycPost==null){
            this.asycPost = new AsycPost(this);
        }
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

    public void matchPhoneNumbers(String userId, String[] phoneNumbersArray){
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
        this.asycPost.setmData(data);
        this.asycPost.execute(Constants.API_ENDPOINT+Constants.USER_PATH);


    }


    public void didReceiveData(JSONArray data){
        this.phoneManagerInterface.didReceiveMatchedNumbers(data);

    }
    public void didReceiveError(String error){
        this.phoneManagerInterface.didReceiveMatchedNumbersError(error);
    }

}
