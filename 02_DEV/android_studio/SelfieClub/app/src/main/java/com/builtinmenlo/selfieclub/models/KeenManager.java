package com.builtinmenlo.selfieclub.models;
import android.content.Context;
import android.util.Log;

import com.builtinmenlo.selfieclub.Constants;
import java.util.HashMap;
import io.keen.client.android.AndroidKeenClientBuilder;
import io.keen.client.java.KeenCallback;
import io.keen.client.java.KeenClient;
import io.keen.client.java.KeenProject;
import java.util.Map;

/**
 * Created by Leonardo on 8/10/14.
 */
public class KeenManager {
    private static KeenManager instance = null;




    public static synchronized KeenManager sharedInstance(Context context){
        if(instance==null) {
            instance = new KeenManager();
            initialize(context);
        }
        return instance;
    }

    private static void initialize(Context context){

        KeenClient client = new AndroidKeenClientBuilder(context).build();
        KeenClient.initialize(client);


    }




    public void trackEvent(String sEvent){
        String[] tokens = sEvent.split("-");
        String eventConllection = tokens[0];
        String stringEvent = tokens[1];
        Map<String, Object> properties = new HashMap<String, Object>();
        Map<String, Object> event = new HashMap<String, Object>();
        event.put("action",stringEvent);

       KeenProject project = new KeenProject(Constants.KEEN_PROJECT_ID,Constants.KEEN_WRITE_KEY,Constants.KEEN_READ_KEY);
        KeenClient.client().addEventAsync(project,eventConllection,event,properties,new KeenCallback() {
            @Override
            public void onSuccess() {
                Log.w("Keen manager","Event send");
            }

            @Override
            public void onFailure(Exception e) {
                Log.w("Keen manager",e.toString());
            }
        });



    }



}
