package com.derma.sebacia.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by nick on 9/8/15.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    
    private Context context;

    private static String TAG = "Sebacia";

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        this.context = context;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        Camera.Parameters parameters = mCamera.getParameters();
        Camera.Size size = getBestPreviewSize(w, h);
        
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        switch (display.getRotation()) {
            case Surface.ROTATION_0: // This is display orientation
                if (size.height > size.width) parameters.setPreviewSize(size.height, size.width);
                else parameters.setPreviewSize(size.width, size.height);
                mCamera.setDisplayOrientation(90);
                break;
            case Surface.ROTATION_90:
                if (size.height > size.width) parameters.setPreviewSize(size.height, size.width);
                else parameters.setPreviewSize(size.width, size.height);
                mCamera.setDisplayOrientation(0);
                break;
            case Surface.ROTATION_180:
                if (size.height > size.width) parameters.setPreviewSize(size.height, size.width);
                else parameters.setPreviewSize(size.width, size.height);
                mCamera.setDisplayOrientation(270);
                break;
            case Surface.ROTATION_270:
                if (size.height > size.width) parameters.setPreviewSize(size.height, size.width);
                else parameters.setPreviewSize(size.width, size.height);
                mCamera.setDisplayOrientation(180);
                break;
        }
        
        Camera.Size pictureSize = getBestPictureSize(mCamera.getParameters());
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        mCamera.setParameters(parameters);
        

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
    
    private Camera.Size getBestPictureSize(Camera.Parameters parameters) {
        Camera.Size result = null;
        
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        for(int i = 0; i < sizes.size(); i++) {
            Camera.Size size = sizes.get(i);
            if(result == null) {
                result = size;
            } else {
                int resultArea = result.width * result.height;
                int newArea = size.width * size.height;
                
                if(newArea > resultArea) {
                    result = size;
                }
            }
        }
        
        return result;
    }

    private Camera.Size getBestPreviewSize(int width, int height) {
        Camera.Size result=null;
        Camera.Parameters p = mCamera.getParameters();
        for (Camera.Size size : p.getSupportedPreviewSizes()) {
            if (size.width<=width && size.height<=height) {
                if (result==null) {
                    result=size;
                } else {
                    int resultArea=result.width*result.height;
                    int newArea=size.width*size.height;

                    if (newArea>resultArea) {
                        result=size;
                    }
                }
            }
        }
        return result;
    }
}
