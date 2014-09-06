package com.builtinmenlo.selfieclub.models;
import android.app.Application;
import android.util.Log;

import com.builtinmenlo.selfieclub.Constants;
import com.tapstream.sdk.Config;
import com.tapstream.sdk.Event;
import com.tapstream.sdk.Tapstream;

import java.util.HashMap;
import io.keen.client.android.AndroidKeenClientBuilder;
import io.keen.client.java.KeenCallback;
import io.keen.client.java.KeenClient;
import io.keen.client.java.KeenProject;
import java.util.Map;

/**
 * Created by Leonardo on 8/10/14.
 */
public class AnalyticsManager {
    private static AnalyticsManager instance = null;




    public static synchronized AnalyticsManager sharedInstance(Application application){
        if(instance==null) {
            instance = new AnalyticsManager();
            initialize(application);
        }
        return instance;
    }

    private static void initialize(Application application){

        KeenClient client = new AndroidKeenClientBuilder(application.getApplicationContext()).build();
        KeenClient.initialize(client);

        Config config = new Config();
        Tapstream.create(application, Constants.TAPSTREAM_PROJECT_NAME, Constants.TAPSTREAM_SECRET_KEY, config);

    }




    public void trackEvent(String sEvent){
        String[] tokens = sEvent.split("-");
        String eventConllection = tokens[0];
        String stringEvent = tokens[1];
        Map<String, Object> properties = new HashMap<String, Object>();
        Map<String, Object> event = new HashMap<String, Object>();
        event.put("action",stringEvent);

       KeenProject project = new KeenProject(Constants.KEEN_PROJECT_ID,Constants.KEEN_WRITE_KEY,Constants.KEEN_READ_KEY);
       KeenClient.client().addEventAsync(project, eventConllection, event, properties, new KeenCallback() {
            @Override
            public void onSuccess() {
                //Log.w("Keen manager", "Event send");
            }

            @Override
            public void onFailure(Exception e) {
                Log.w("Keen manager", e.toString());
            }
        });

        trackTapStream(eventConllection,tokens[1]);


    }


    private void trackTapStream(String group, String action){
        Tapstream tracker = Tapstream.getInstance();
        Event event = new Event(group,false);
        event.addPair(group,action);
        tracker.fireEvent(event);
    }

}
