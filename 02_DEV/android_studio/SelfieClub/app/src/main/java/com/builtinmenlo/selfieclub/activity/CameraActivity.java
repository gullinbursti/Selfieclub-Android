package com.builtinmenlo.selfieclub.activity;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;

public class CameraActivity extends ActionBarActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		if (savedInstanceState == null)
			getSupportFragmentManager().beginTransaction().add(R.id.activity_camera_layout, new PlaceholderFragment(getIntent().getExtras())).commit();

		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		Log.i("__PAYLOAD as String__", getIntent().getExtras().toString());
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_camera, menu);
		return (true);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return ((item.getItemId() == R.id.action_camera_menu) || super.onOptionsItemSelected(item));
	}


	public static class PlaceholderFragment extends Fragment {

		private Bundle payload;

		public PlaceholderFragment() {/*..\(^_^)/..*/}
		public PlaceholderFragment(Bundle payload) {
			this.payload = payload;
		}

		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			container.setBackgroundColor(getResources().getColor(R.color.activity_camera_bg_color));

			View view = inflater.inflate(R.layout.fragment_camera, container, false);

			TextView textView = (TextView) view.findViewById(R.id.fragment_camera_textview);
			textView.setText(this.payload.getString(MainActivity.INTENT_PAYLOAD_AS_STRING));

			return (view);
		}
	}
}
