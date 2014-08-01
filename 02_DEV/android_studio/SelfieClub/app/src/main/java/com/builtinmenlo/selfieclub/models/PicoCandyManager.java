package com.builtinmenlo.selfieclub.models;

import android.content.Context;

import com.builtinmenlo.selfieclub.Constants;
import com.picocandy.android.PicoManager;
import com.picocandy.android.candystore.PCContentHelper;
import com.picocandy.android.data.PCContentGroup;
import com.picocandy.android.user.PicoUser;

/**
 * Created by Leonardo on 7/31/14.
 */
public class PicoCandyManager {

    public static void registerApp(Context context){
        PicoManager picoManager = PicoManager.getInstance();
        picoManager.registerStore(context, Constants.PICOCANDY_APP_ID,Constants.PICOCANDY_API_KEY);
    }

    public static PicoUser getCurrentUser(){
        return PicoUser.currentUser();
    }

    public static PCContentGroup getContentGroups(String ids){
        PCContentHelper contentHelper = new PCContentHelper();
        contentHelper.fetchContentGroup();
    }
}
