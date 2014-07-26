package com.builtinmenlo.selfieclub.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.builtinmenlo.selfieclub.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by javy on 7/23/14.
 */
public class CameraFragment extends Fragment implements SurfaceHolder.Callback, OnClickListener, PictureCallback, AutoFocusCallback {

    public static final String EXTRA_IMAGE = "image";
    public static boolean isUsingFrontCamera = false;


    private SurfaceView mPreview;
    private Bundle bundle;
    private Camera mCamera;
    private ImageButton mButton;
    private static final int TEN_DESIRED_ZOOM = 27;
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");
    private static String TAG = "CameraActivity";

    @Override
    public void onDestroyView() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
        super.onDestroyView();
    }

    private Camera openCamera() {
        stopCamera();

        if (isUsingFrontCamera) {
            int cameraCount = 0;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount = Camera.getNumberOfCameras();
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    try {
                        mCamera = Camera.open(camIdx);
                        mPreview.getHolder().addCallback(this);
                        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                    }
                }
            }

        } else {
            try {
                mCamera = Camera.open();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Failed to Open Camera", Toast.LENGTH_SHORT).show();
            }
        }

        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
            mCamera.startPreview();
            mCamera.setDisplayOrientation(90);
        } catch (IOException e) {
            Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
        }
        return mCamera;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment, container, false);

        view.findViewById(R.id.btnClose).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        view.findViewById(R.id.btnFlip).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isUsingFrontCamera = !isUsingFrontCamera;
                openCamera();
            }
        });
        // get views
        bundle = getArguments();
        mPreview = (SurfaceView) view.findViewById(R.id.camera_surface);
        mPreview.getHolder().addCallback(this);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mButton = (ImageButton) view.findViewById(R.id.imagebutton_camera);
        mButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.w(getClass().getName(), "on config change");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        openCamera();
    }

    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }
        return (result);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin the preview.
        Camera.Parameters parameters = mCamera.getParameters();
        //Camera.Size size = getBestPreviewSize(w, h, parameters);
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        setFlash(parameters);
        setZoom(parameters);
        //mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);

        mCamera.startPreview();
        // mCamera.autoFocus(this);

    }

    private Camera.Size getBestPictureSize(int width, int height) {
        Camera.Size result = null;
        for (Camera.Size size : mCamera.getParameters().getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    if (size.width >= 1600 && size.height >= 1200) {
                        result = size;
                        break;
                    }
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }
        return (result);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void stopCamera() {
        System.out.println("stopCamera method");
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
            mPreview.getHolder().removeCallback(this);
            //mPreview.getHolder() = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.setPreviewCallback(null);
        mCamera.release();
        mCamera = null;

    }

    @Override
    public void onClick(View arg0) {
        mButton.setEnabled(false);
        // mButton.setText("Please Wait..");

        mCamera.autoFocus(this);

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Fragment newFragment = new FirstRunRegistrationFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        if (bundle == null)
            bundle = new Bundle();
        Bitmap thePicture = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix m = new Matrix();
        m.postRotate(90);
        thePicture = Bitmap.createBitmap(thePicture, 0, 0, thePicture.getWidth(), thePicture.getHeight(), m, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        thePicture.compress(CompressFormat.JPEG, 100, bos);
        data = bos.toByteArray();
        bundle.putByteArray(EXTRA_IMAGE, data);
        newFragment.setArguments(bundle);
        transaction.commit();
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        mCamera.takePicture(null, null, null, this);
    }

    private static int findBestMotZoomValue(CharSequence stringValues, int tenDesiredZoom) {
        int tenBestValue = 0;
        for (String stringValue : COMMA_PATTERN.split(stringValues)) {
            stringValue = stringValue.trim();
            double value;
            try {
                value = Double.parseDouble(stringValue);
            } catch (NumberFormatException nfe) {
                return tenDesiredZoom;
            }
            int tenValue = (int) (10.0 * value);
            if (Math.abs(tenDesiredZoom - value) < Math.abs(tenDesiredZoom - tenBestValue)) {
                tenBestValue = tenValue;
            }
        }
        return tenBestValue;
    }

    private void setFlash(Camera.Parameters parameters) {
        // FIXME: This is a hack to turn the flash off on the Samsung Galaxy.
        // And this is a hack-hack to work around a different value on the
        // Behold II
        if (Build.MODEL.contains("Behold II")) {
            parameters.set("flash-value", 1);
        } else {
            parameters.set("flash-value", 2);
        }
        // This is the standard setting to turn the flash off that all devices should honor.
        parameters.set("flash-mode", "off");
    }

    private void setZoom(Camera.Parameters parameters) {

        String zoomSupportedString = parameters.get("zoom-supported");
        if (zoomSupportedString != null && !Boolean.parseBoolean(zoomSupportedString)) {
            return;
        }

        int tenDesiredZoom = TEN_DESIRED_ZOOM;

        String maxZoomString = parameters.get("max-zoom");
        if (maxZoomString != null) {
            try {
                int tenMaxZoom = (int) (10.0 * Double.parseDouble(maxZoomString));
                if (tenDesiredZoom > tenMaxZoom) {
                    tenDesiredZoom = tenMaxZoom;
                }
            } catch (NumberFormatException nfe) {
                Log.w(TAG, "Bad max-zoom: " + maxZoomString);
            }
        }

        String takingPictureZoomMaxString = parameters.get("taking-picture-zoom-max");
        if (takingPictureZoomMaxString != null) {
            try {
                int tenMaxZoom = Integer.parseInt(takingPictureZoomMaxString);
                if (tenDesiredZoom > tenMaxZoom) {
                    tenDesiredZoom = tenMaxZoom;
                }
            } catch (NumberFormatException nfe) {
                Log.w(TAG, "Bad taking-picture-zoom-max: " + takingPictureZoomMaxString);
            }
        }

        String motZoomValuesString = parameters.get("mot-zoom-values");
        if (motZoomValuesString != null) {
            tenDesiredZoom = findBestMotZoomValue(motZoomValuesString, tenDesiredZoom);
        }

        String motZoomStepString = parameters.get("mot-zoom-step");
        if (motZoomStepString != null) {
            try {
                double motZoomStep = Double.parseDouble(motZoomStepString.trim());
                int tenZoomStep = (int) (10.0 * motZoomStep);
                if (tenZoomStep > 1) {
                    tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
                }
            } catch (NumberFormatException nfe) {
                // continue
            }
        }

        // Set zoom. This helps encourage the user to pull back.
        // Some devices like the Behold have a zoom parameter
        if (maxZoomString != null || motZoomValuesString != null) {
            parameters.set("zoom", String.valueOf(tenDesiredZoom / 10.0));
        }

        // Most devices, like the Hero, appear to expose this zoom parameter.
        // It takes on values like "27" which appears to mean 2.7x zoom
        if (takingPictureZoomMaxString != null) {
            parameters.set("taking-picture-zoom", tenDesiredZoom);
        }
    }

}