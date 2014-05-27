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
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.fragments.ClubsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.FriendsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.SettingsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.VerifyTabBtnFragment;
import com.builtinmenlo.selfieclub.listeners.TabButtonListener;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class MainActivity extends Activity {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

	//] class properties ]>
	//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
	public final static String INTENT_PAYLOAD_AS_STRING = "com.builtinmenlo.selfieclub.activity.INTENT_PAYLOAD_AS_STRING";

	Tab friendsTab, clubsTab, verifyTab, settingsTab;

	Fragment friendsFragment = new FriendsTabBtnFragment();
	Fragment clubsFragment = new ClubsTabBtnFragment();
	Fragment verifyFragment = new VerifyTabBtnFragment();
	Fragment settingsFragment = new SettingsTabBtnFragment();
	//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


	protected void onCreate(Bundle savedInstanceState) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.i("__LOCAL_CLASS_NAME", this.getLocalClassName()+"]["+this.getClass().toString());

		ActionBar topNavActionBar = getActionBar();
		topNavActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		topNavActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME);
		topNavActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

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
		settingsTab.setIcon(R.drawable.ic_action_maincamera_normal);
		settingsTab.setTabListener(new TabButtonListener(settingsFragment));
		topNavActionBar.addTab(settingsTab);
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯


	public void onMainCameraClick(View view) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		Intent intent = new Intent(this, CameraActivity.class);
		intent.putExtra(MainActivity.INTENT_PAYLOAD_AS_STRING, String.valueOf(R.string.fpo_textview_hoge));
		startActivity(intent);
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯
}
