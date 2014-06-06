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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.fragments.ClubsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.FriendsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.SettingsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.VerifyTabBtnFragment;
import com.builtinmenlo.selfieclub.listeners.TabButtonListener;

import java.lang.reflect.Field;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class MainActivity extends Activity {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

	//] class properties ]>
	//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
	public final static String INTENT_PAYLOAD_AS_STRING = "com.builtinmenlo.selfieclub.activity.INTENT_PAYLOAD_AS_STRING";

	Tab friendsTab, clubsTab, verifyTab, settingsTab, settingsTab2;

	Fragment friendsFragment = new FriendsTabBtnFragment();
	Fragment clubsFragment = new ClubsTabBtnFragment();
	Fragment verifyFragment = new VerifyTabBtnFragment();
    Fragment settingsFragment = new SettingsTabBtnFragment();
    Fragment settingsFragment2 = new SettingsTabBtnFragment();
	//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


	protected void onCreate(Bundle savedInstanceState) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        getOverflowMenu();

		Log.i("__LOCAL_CLASS_NAME", this.getLocalClassName()+"]["+this.getClass().toString());

		ActionBar topNavActionBar = getActionBar();
		topNavActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		topNavActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME);
		topNavActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        topNavActionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

		friendsTab = topNavActionBar.newTab();
		friendsTab.setText(R.string.tab_friends_title);
		friendsTab.setTabListener(new TabButtonListener(friendsFragment));
		topNavActionBar.addTab(friendsTab);

		clubsTab = topNavActionBar.newTab();
		clubsTab.setText(R.string.tab_clubs_title);
		clubsTab.setTabListener(new TabButtonListener(clubsFragment));
		topNavActionBar.addTab(clubsTab);

		verifyTab = topNavActionBar.newTab();
		verifyTab.setText(R.string.tab_verify_title);
		verifyTab.setTabListener(new TabButtonListener(verifyFragment));
		topNavActionBar.addTab(verifyTab);

        settingsTab = topNavActionBar.newTab();
        settingsTab.setCustomView(R.layout.notifications_tab_bar);
        settingsTab.setText("4");
        settingsTab.setTabListener(new TabButtonListener(settingsFragment));
        topNavActionBar.addTab(settingsTab);

        settingsTab2 = topNavActionBar.newTab();
        settingsTab2.setCustomView(R.layout.overflow_tab_bar);
        settingsTab2.setTabListener(new TabButtonListener(settingsFragment2));
        topNavActionBar.addTab(settingsTab2);
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯


	public void onMainCameraClick(View view) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		Intent intent = new Intent(this, CameraActivity.class);
		intent.putExtra(MainActivity.INTENT_PAYLOAD_AS_STRING, String.valueOf(R.string.fpo_textview_hoge));
		startActivity(intent);
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
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
