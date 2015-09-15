package com.derma.sebacia;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.derma.sebacia.camera.CameraPreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Camera mCamera;
    private CameraPreview mPreview;
    private Button captureButton;

    private static String TAG = "CameraActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        if(mCamera != null) {
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        }

        captureButton = (Button) findViewById(R.id.camera_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCamera != null) {
                    mCamera.takePicture(null, null, mPicture);

                    Toast.makeText(getApplicationContext(), "Took picture", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if(mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
//            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
//            if (pictureFile == null){
//                Log.d(TAG, "Error creating media file, check storage permissions");
//                return;
//            }
            String filename = getImageFileName();
            try {
//                FileOutputStream fos

                FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "File not found", e);
            } catch (IOException e) {
                Log.e(TAG, "Error accessing file", e);
            }

            Log.d(TAG, "saved picture to " + filename);
        }
    };

    private String getImageFileName() {
        String filename = "";

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        filename = timeStamp + ".jpg";

        return filename;
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            if(Camera.getNumberOfCameras() > 1) {
                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
