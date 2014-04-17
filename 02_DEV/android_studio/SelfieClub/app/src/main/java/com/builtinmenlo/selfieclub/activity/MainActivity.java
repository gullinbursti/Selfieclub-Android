/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.builtinmenlo.selfieclub.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.activity.CameraActivity;
import com.builtinmenlo.selfieclub.fragments.ClubsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.FriendsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.SettingsTabBtnFragment;
import com.builtinmenlo.selfieclub.fragments.VerifyTabBtnFragment;
import com.builtinmenlo.selfieclub.listeners.TabButtonListener;


public class MainActivity extends Activity {

	public final static String INTENT_PAYLOAD_AS_STRING = "com.builtinmenlo.selfieclub.activity.INTENT_PAYLOAD_AS_STRING";


	Tab friendsTab, clubsTab, verifyTab, settingsTab;

	Fragment friendsFragment = new FriendsTabBtnFragment();
	Fragment clubsFragment = new ClubsTabBtnFragment();
	Fragment verifyFragment = new VerifyTabBtnFragment();
	Fragment settingsFragment = new SettingsTabBtnFragment();

	protected void onCreate(Bundle savedInstanceState) {
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
	}

	public void onMainCameraClick(View view) {
		Intent intent = new Intent(this, CameraActivity.class);
		intent.putExtra(MainActivity.INTENT_PAYLOAD_AS_STRING, String.valueOf(R.string.fpo_textview_hoge));
		startActivity(intent);
	}
}
