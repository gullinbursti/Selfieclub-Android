package com.builtinmenlo.selfieclub.models;

import android.content.Context;

import com.builtinmenlo.selfieclub.Constants;
import com.picocandy.android.PicoManager;
import com.picocandy.android.candystore.PCContentHelper;
import com.picocandy.android.data.PCContentGroup;
import com.picocandy.android.user.PicoUser;

import java.util.ArrayList;

/**
 * Created by Leonardo on 7/31/14.
 */
public class PicoCandyManager {

    public void registerApp(Context context){
        PicoManager picoManager = PicoManager.getInstance();
        picoManager.registerStore(context, Constants.PICOCANDY_APP_ID,Constants.PICOCANDY_API_KEY);
    }

    public PicoUser getCurrentUser(){
        return PicoUser.currentUser();
    }

    public void getContentGroups(ArrayList<String>ids){
        PCContentHelper contentHelper = new PCContentHelper();
        synchronized (this){
            for(int i=0;i<ids.size();i++){
                contentHelper.fetchContentGroup(ids.get(i),new PCContentHelper.FetchContentGroupListener() {
                    @Override
                    public void onSuccess(PCContentGroup pcContentGroup) {

                    }

                    @Override
                    public void onFail(String s) {

                    }
                });
            }
        }




    }
}
