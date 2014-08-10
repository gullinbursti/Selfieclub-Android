package com.builtinmenlo.selfieclub.models;
import android.content.Context;
import com.builtinmenlo.selfieclub.Constants;
import java.util.HashMap;
import io.keen.client.android.AndroidKeenClientBuilder;
import io.keen.client.java.KeenClient;
import io.keen.client.java.KeenProject;

/**
 * Created by Leonardo on 8/10/14.
 */
public class KeenManager {
    private static KeenClient client = null;
    private static KeenManager instance = null;


    private KeenManager(){}

    public static synchronized KeenManager sharedInstance(){
        if(instance==null) {
            instance = new KeenManager();
        }
        return instance;
    }

    public void initialize(Context context){
        this.client = new AndroidKeenClientBuilder(context).build();
        KeenProject project = new KeenProject(Constants.KEEN_PROJECT_ID,Constants.KEEN_WRITE_KEY,Constants.KEEN_READ_KEY);
        this.client.setDefaultProject(project);
    }

    public void trackEvent(String strEvent){
        HashMap<String,Object> event = new HashMap<String,Object>();
        event.put("action",strEvent);
        String eventConllection = "";
        if(Constants.PLAYSTOREBUILD){
            eventConllection = Constants.KEEN_PROD_COLLECTION;
        }
        else{
            eventConllection = Constants.KEEN_DEV_COLLECTION;
        }
        this.client.addEvent(eventConllection,event);
    }



}
