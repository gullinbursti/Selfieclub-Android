/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*._
 /**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~,
 *
 * @class CameraPreview
 * @project SelfieClub
 * @package com.builtinmenlo.selfieclub.views
 *
 * @author Matt H. <matt@builtinmenlo.com>
 * @created 19-Apr-2014 @ 12:06
 * @copyright (c) 2014 Built in Menlo, LLC. All rights reserved.
 *
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~·'
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~**/


package com.builtinmenlo.selfieclub.views;


//] includes [!]>
//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

	//] class properties ]>
	//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[

	// <*] class constructor [*>
	public CameraPreview(Context context, Camera camera) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		super(context);

		Log.i(".... ", this.getClass().toString() + "()");

		this.camera = camera;
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

	//]~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=[>
	//]~=~=~=~=~=~=~=~=~=[>

	public void surfaceCreated(SurfaceHolder holder) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		// The Surface has been created, now tell the camera where to draw the preview.
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();

		} catch (IOException e) {
			Log.d(VIEW_LOG_TAG, "Error setting camera preview: " + e.getMessage());
		}
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

	public void surfaceDestroyed(SurfaceHolder holder) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		// empty. Take care of releasing the Camera preview in your activity.
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.

		if (surfaceHolder.getSurface() == null)
			return;

		// stop preview before making changes
		try {
			camera.stopPreview();

		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();

		} catch (Exception e){
			Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
		}
	}//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯
}
