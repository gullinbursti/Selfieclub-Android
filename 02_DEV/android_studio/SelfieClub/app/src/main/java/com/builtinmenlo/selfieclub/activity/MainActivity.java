/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*._
 /**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~,
 *
 * @class MainActivity
 * @project SelfieClub
 * @package com.builtinmenlo.selfieclub.activity
 *
 * @author Matt.H <matt@builtinmenlo.com>
 * @created 16-Apr-2014 @ 11:50
 * @copyright (c) 2014 Built in Menlo, LLC. All rights reserved.
 *
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~·'
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~**/


package com.builtinmenlo.selfieclub.activity;


//] includes [!]>
//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.fragments.ActivityTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.ClubsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.FriendsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.NewsTabBtnFragment;
import com.builtinmenlo.selfieclub.listeners.TabButtonListener;

import java.lang.reflect.Field;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class MainActivity extends Activity {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

    //] class properties ]>
    //]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
    public final static String INTENT_PAYLOAD_AS_STRING = "com.builtinmenlo.selfieclub.activity.INTENT_PAYLOAD_AS_STRING";

    Tab friendsTab, clubsTab, newsTab, notificationsTab;

    Fragment friendsFragment = new FriendsTabBtnFragment();
    Fragment clubsFragment = new ClubsTabBtnFragment();
    Fragment newsFragment = new NewsTabBtnFragment();
    Fragment notificationsFragment = new ActivityTabBtnFragment();
    //]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


    protected void onCreate(Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getOverflowMenu();

        Log.i("__LOCAL_CLASS_NAME", this.getLocalClassName() + "][" + this.getClass().toString());

        ActionBar topNavActionBar = getActionBar();
        topNavActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        topNavActionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.white)));

        LayoutInflater mInflater = LayoutInflater.from(this);

        friendsTab = topNavActionBar.newTab();
        View friendsTabCustomView = mInflater.inflate(R.layout.item_tab_bar, null);
        TextView friendsTitleTextView = (TextView) friendsTabCustomView.findViewById(R.id.lblTitleText);
        friendsTitleTextView.setText(R.string.tab_friends_title);
        friendsTab.setCustomView(friendsTabCustomView);
        //friendsTab.setText(R.string.tab_friends_title);
        friendsTab.setTabListener(new TabButtonListener(friendsFragment));
        topNavActionBar.addTab(friendsTab);

        newsTab = topNavActionBar.newTab();
        View newsTabCustomView = mInflater.inflate(R.layout.item_tab_bar, null);
        TextView newsTitleTextView = (TextView) newsTabCustomView.findViewById(R.id.lblTitleText);
        newsTitleTextView.setText(R.string.tab_news_title);
        newsTab.setCustomView(newsTabCustomView);
        //newsTab.setText(R.string.tab_news_title);
        newsTab.setTabListener(new TabButtonListener(newsFragment));
        topNavActionBar.addTab(newsTab);

        clubsTab = topNavActionBar.newTab();
        View clubsTabCustomView = mInflater.inflate(R.layout.item_tab_bar, null);
        TextView clubsTitleTextView = (TextView) clubsTabCustomView.findViewById(R.id.lblTitleText);
        clubsTitleTextView.setText(R.string.tab_clubs_title);
        clubsTab.setCustomView(clubsTabCustomView);
        //clubsTab.setText(R.string.tab_clubs_title);
        clubsTab.setTabListener(new TabButtonListener(clubsFragment));
        topNavActionBar.addTab(clubsTab);


        notificationsTab = topNavActionBar.newTab();
        View mCustomView = mInflater.inflate(R.layout.notifications_tab_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.lblTitleText);
        mTitleTextView.setText("0");
        notificationsTab.setCustomView(mCustomView);
        notificationsTab.setTabListener(new TabButtonListener(notificationsFragment));
        topNavActionBar.addTab(notificationsTab);

    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯


    public void onMainCameraClick(View view) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        //Intent intent = new Intent(this, CameraActivity.class);
        Intent intent = new Intent(this, CameraPreview.class);
        //intent.putExtra(MainActivity.INTENT_PAYLOAD_AS_STRING, String.valueOf(R.string.fpo_textview_hoge));
        startActivity(intent);
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯


    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // inflate for action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.items, menu);
        return true;
    }

    // handle click events for action bar items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            /*case R.id.refresh:
                showToast("Refresh was clicked.");
                return true;

            case R.id.copyLink:
                showToast("Copy link was clicked.");
                return true;

            case R.id.share:
                showToast("Share was clicked.");
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
