package com.builtinmenlo.selfieclub.models;

import android.content.Context;


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

    /**
     * Returns the list of stikers. You need to requests the stickers first
     * @return An array list of PCContent
     */
    public ArrayList<PCContent> getStickerList(){
        return stickerList;
    }

    /**
     * Returns the list of PC groups
     * @return An array list of PCContentGroup
     */
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

    public void requestStickers(final StikersProtocol stikersProtocol){
        final ArrayList<String> ids = new ArrayList<String>();
        ids.add("883");
        ids.add("884");
        ids.add("885");
        ids.add("886");
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

    private void parseStickers(StikersProtocol stikersProtocol, ArrayList<PCContentGroup>contentGroupsList){
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

    /**
     * Returns a PC stiker given the name
     * @param name
     * @return PCContent stiker
     */
    public PCContent getStickerByName(String name){
        PCContent sticker = null;
        String filename;
        for (int i=0; i<this.stickerList.size();i++){
            filename = this.stickerList.get(i).getName();
            String[] tokens = filename.split("\\.");
            if(name.equalsIgnoreCase(tokens[0])) {
                sticker = this.stickerList.get(i);
                break;
            }
        }
        return sticker;
    }

}
