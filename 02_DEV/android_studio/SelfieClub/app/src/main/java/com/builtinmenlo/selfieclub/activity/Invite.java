package com.builtinmenlo.selfieclub.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.builtinmenlo.selfieclub.R;

import java.util.ArrayList;
import java.util.Arrays;


public class Invite extends Activity {

    //private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        // Find the ListView resource.
        ListView mainListView = (ListView) findViewById(R.id.listView);

        // Create and populate a List of club names.
        String[] club = new String[] { "ucdavisx", "selfieclub", "cars", "android",
                "sports", "holidays", "pictures", "high school"};
        ArrayList<String> clubList = new ArrayList<String>();
        clubList.addAll( Arrays.asList(club) );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.main_listview, clubList);

        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
        listAdapter.add( "Club one" );
        listAdapter.add( "Club two" );
        listAdapter.add( "Club three" );
        listAdapter.add( "Club four" );
        listAdapter.add( "Club five" );

        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );
    }
}

/*
public class Invite extends Activity {

    List<Map<String, String>> clubsList = new ArrayList<Map<String,String>>();

    private void initList() {
        // We populate the planets

        clubsList.add(createClub("planet", "Mercury"));
        clubsList.add(createClub("planet", "Venus"));
        clubsList.add(createClub("planet", "Mars"));
        clubsList.add(createClub("planet", "Jupiter"));
        clubsList.add(createClub("planet", "Saturn"));
        clubsList.add(createClub("planet", "Uranus"));
        clubsList.add(createClub("planet", "Neptune"));

    }

    private HashMap<String, String> createClub(String key, String name) {
        HashMap<String, String> clubs = new HashMap<String, String>();
        clubs.put(key, name);

        return clubs;
    }

    SimpleAdapter simpleAdpt = new SimpleAdapter(this, clubsList, android.R.layout.simple_list_item_1, new String[] {"planet"}, new int[] {android.R.id.text1});


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        initList();

        // We get the ListView component from the layout
        ListView lv = (ListView) findViewById(R.id.listView);

        // This is a simple adapter that accepts as parameter
        // Context
        // Data list
        // The row layout that is used during the row creation
        // The keys used to retrieve the data
        // The View id used to show the data. The key number and the view id must match
        simpleAdpt = new SimpleAdapter(this, clubsList, android.R.layout.simple_list_item_1, new String[] {"planet"}, new int[] {android.R.id.text1});

        lv.setAdapter(simpleAdpt);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.invite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
*/