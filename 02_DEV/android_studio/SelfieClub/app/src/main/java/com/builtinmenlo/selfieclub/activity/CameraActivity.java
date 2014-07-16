/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*._
 /**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~,
 *
 * @class CameraActivity
 * @project SelfieClub
 * @package com.builtinmenlo.selfieclub.activity
 *
 * @author Matt.H <matt@builtinmenlo.com>
 * @created 16-Apr-2014 @ 15:15
 * @copyright (c) 2014 Built in Menlo, LLC. All rights reserved.
 *
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~·'
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~**/



package com.builtinmenlo.selfieclub.activity;


//] includes [!]>
//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.

import android.app.ActionBar;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.views.CameraPreview;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class CameraActivity extends ActionBarActivity {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

	//] class properties ]>
	//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
	private Camera camera;
	private CameraPreview cameraPreview;
	//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[

	protected void onCreate(Bundle savedInstanceState) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_camera);
        setContentView(R.layout.fragment_camera);

		if (savedInstanceState == null)
			getSupportFragmentManager().beginTransaction().add(R.id.activity_camera_layout, new CameraFragment(getIntent().getExtras())).commit();

		//Log.i("__PAYLOAD as String__", getIntent().getExtras().toString());

		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		camera = this.getCameraInstance();
		cameraPreview = new CameraPreview(this, camera);

		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_camera_preview_layout);
		frameLayout.addView(cameraPreview);
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

	public boolean onCreateOptionsMenu(Menu menu) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		getMenuInflater().inflate(R.menu.menu_camera, menu);
		return (true);
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

	public boolean onOptionsItemSelected(MenuItem item) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		return ((item.getItemId() == R.id.action_camera_menu) || super.onOptionsItemSelected(item));
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯


	private boolean checkCameraHardware(Context context) {
		return (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA));
	}


	public static Camera getCameraInstance() {
		Camera c = null;

		try {
			c = Camera.open();

		} catch (Exception e){

		}

		return (c);
	}


	// <[!] class delaration [¡]>
	public static class CameraFragment extends Fragment {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

		//] class properties ]>
		//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
		private Bundle payload;
		//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[

		// <*] class constructors [*>
		public CameraFragment() {/*..\(^_^)/..*/}
		public CameraFragment(Bundle payload) {
		//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
			this.payload = payload;
		}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

		//]~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=[>
		//]~=~=~=~=~=~=~=~=~=[>

		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
			container.setBackgroundColor(getResources().getColor(R.color.activity_camera_bg_color));

			View view = inflater.inflate(R.layout.fragment_camera, container, false);

//			FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.fragment_camera_preview_layout);
//			frameLayout.addView(cameraPreview);

			TextView textView = (TextView) view.findViewById(R.id.fragment_camera_textview);
			textView.setText(this.payload.getString(MainActivity.INTENT_PAYLOAD_AS_STRING));

			return (view);
		}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯
}
