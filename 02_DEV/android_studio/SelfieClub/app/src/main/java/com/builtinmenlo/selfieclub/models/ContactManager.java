package com.builtinmenlo.selfieclub.models;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;



import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Leonardo on 6/3/14.
 * This class implements the method for handling the user's contacts
 */
public class ContactManager {

    private ContentResolver contentResolver;
    public ContactManager(ContentResolver contentResolver){
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

}
