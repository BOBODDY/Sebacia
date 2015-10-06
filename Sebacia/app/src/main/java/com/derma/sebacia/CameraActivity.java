package com.derma.sebacia;

import android.content.Context;
import android.content.Intent;
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
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.app.DialogFragment;

import com.derma.sebacia.camera.CameraPreview;
import com.derma.sebacia.data.AcneLevel;
import com.derma.sebacia.data.Picture;
import com.derma.sebacia.database.LocalDb;

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
    private Button captureButton, diagnoseButton;
    LocalDb db;

    private static String TAG = "Sebacia";
    
    private byte[] pictureData;
    private Picture selfie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        
        db = new LocalDb(getApplicationContext());
        
        Log.d(TAG, "in CameraActivity");

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        if(mCamera != null) {
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        } else {
            Log.e(TAG, "no camera found");
        }

        captureButton = (Button) findViewById(R.id.camera_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCamera != null) {
                    mCamera.takePicture(null, null, mPicture);

                    DialogFragment diagOptFrag = new DiagnosisOptionsFragment();
                    diagOptFrag.show(getFragmentManager(), "diag_opt_dialog");

//                    Toast.makeText(getApplicationContext(), "Took picture", Toast.LENGTH_SHORT).show();
//                    finish();
                }
            }
        });

        diagnoseButton = (Button) findViewById(R.id.camera_diagnose);
        diagnoseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment diagOptFrag = new DiagnosisOptionsFragment();
                diagOptFrag.show(getFragmentManager(), "diag_opt_dialog");
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
            //TODO: write image to database
            String filename = getImageFileName();
            Picture newPic = new Picture(filename, new AcneLevel(1, "1"), data);
            db.addPicture(newPic);
            
            pictureData = data;
            selfie = newPic;
            
            Log.d(TAG, "saved picture to " + filename);

            Toast.makeText(getApplicationContext(), "Took picture", Toast.LENGTH_SHORT).show();
//            finish();
//            try {
////                FileOutputStream fos
//
//                FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
//                fos.write(data);
//                fos.close();
//            } catch (FileNotFoundException e) {
//                Log.e(TAG, "File not found", e);
//            } catch (IOException e) {
//                Log.e(TAG, "Error accessing file", e);
//            }
//
//            Log.d(TAG, "saved picture to " + filename);
        }
    };

    private String getImageFileName() {
        String filename = "";

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        filename = timeStamp + ".png";

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
            Log.e(TAG, "error getting camera instance", e);
        }
        return c; // returns null if camera is unavailable
    }

    public void beginAutoClassification(View view) {
        // TODO : add code for going to classification results page
    }

    public void beginSurvey(View view) {
        Intent intent = new Intent(this, SurveyActivity.class);
        Log.d(TAG, "is Picture null? " + (selfie == null));
        try {
            intent.putExtra("picturePath", selfie.getFilePath());
        } catch (NullPointerException e) {
            // Handle exception here
        }
        startActivity(intent);
    }
}
