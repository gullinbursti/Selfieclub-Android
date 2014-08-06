package com.builtinmenlo.selfieclub.models;

import android.content.Context;
import android.util.Log;

import com.builtinmenlo.selfieclub.Constants;
import com.picocandy.android.PicoManager;
import com.picocandy.android.candystore.PCContentHelper;
import com.picocandy.android.data.PCContent;
import com.picocandy.android.data.PCContentGroup;
import com.picocandy.android.user.PicoUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo on 7/31/14.
 * Singleton class for Pico Candy operations
 */
public class PicoCandyManager {

    private static PicoCandyManager instance = null;
    private ArrayList<PCContentGroup> contentGroupsList = null;
    private ArrayList<PCContent> stickerList = null;
    protected int processedItems = 0;

    private PicoCandyManager(){
        stickerList = new ArrayList<PCContent>();
        contentGroupsList = new ArrayList<PCContentGroup>();
    }

    public ArrayList<PCContent> getStickerList(){
        return stickerList;
    }

    public ArrayList<PCContentGroup> getContentGroupsList(){
        return contentGroupsList;
    }

    public static synchronized PicoCandyManager sharedInstance(){
        if(instance==null)
            instance = new PicoCandyManager();
        return instance;
    }

    public void registerApp(Context context){
        PicoManager picoManager = PicoManager.getInstance();
        picoManager.registerStore(context, Constants.PICOCANDY_APP_ID,Constants.PICOCANDY_API_KEY);
    }

    public PicoUser getCurrentUser(){

        return PicoUser.currentUser();
    }

    public void requestStickers(final StikersProtocol stikersProtocol, final ArrayList<String> ids){
        PCContentHelper contentHelper = new PCContentHelper();
            final ArrayList<PCContentGroup> contentGroupArrayList = new ArrayList<PCContentGroup>();
            for(int i=0;i<ids.size();i++){
                contentHelper.fetchContentGroup(ids.get(i),new PCContentHelper.FetchContentGroupListener() {
                    @Override
                    public void onSuccess(PCContentGroup pcContentGroup) {
                        processedItems+=1;
                        contentGroupArrayList.add(pcContentGroup);
                        if(ids.size()==processedItems){
                            parseStickers(stikersProtocol, contentGroupArrayList);
                        }

                    }

                    @Override
                    public void onFail(String s) {
                        processedItems+=1;
                        if(ids.size()==processedItems){
                            parseStickers(stikersProtocol,contentGroupArrayList);
                        }
                    }
                });
            }
    }

    public void parseStickers(StikersProtocol stikersProtocol, ArrayList<PCContentGroup>contentGroupsList){
        for (int i=0;i<contentGroupsList.size();i++){
            PCContentGroup pcContentGroup = contentGroupsList.get(i);
            this.contentGroupsList.add(pcContentGroup);
            List<PCContent> contents = pcContentGroup.getContents();
            for (int j=0; j<contents.size();j++){
                PCContent item = contents.get(j);
                this.stickerList.add(item);
            }
        }
        stikersProtocol.didReceiveStickers(contentGroupsList,stickerList);
    }


}
