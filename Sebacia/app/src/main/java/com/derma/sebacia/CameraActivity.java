package com.derma.sebacia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.app.DialogFragment;

import com.derma.sebacia.camera.CameraPreview;
import com.derma.sebacia.data.AcneLevel;
import com.derma.sebacia.data.Picture;
import com.derma.sebacia.database.LocalDb;

import java.io.ByteArrayOutputStream;
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
    private String selfiePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        
//        db = new LocalDb(getApplicationContext());
        new loadDbTask().execute(getApplicationContext());
        
        Log.d(TAG, "in CameraActivity");

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        if(mCamera != null) {
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);

            ImageView overlay = new ImageView(this);
            Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.faceoval);
            overlay.setImageBitmap(bmp);
            overlay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            preview.addView(overlay);
        } else {
            Log.e(TAG, "no camera found");
        }

        captureButton = (Button) findViewById(R.id.camera_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCamera != null) {
                    mCamera.takePicture(null, null, mPicture);

//                    DialogFragment diagOptFrag = new DiagnosisOptionsFragment();
//                    diagOptFrag.show(getFragmentManager(), "diag_opt_dialog");

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
            Log.d(TAG, "in PictureCallback");
            
            String filename = getImageFileName();

            Display display = getWindowManager().getDefaultDisplay();
            int rotation = 0;
            switch (display.getRotation()) {
                case Surface.ROTATION_0: // This is display orientation
                    rotation = 270;
                    break;
                case Surface.ROTATION_90:
                    rotation = 0;
                    break;
                case Surface.ROTATION_180:
                    rotation = 90;
                    break;
                case Surface.ROTATION_270:
                    rotation = 180;
                    break;
            }
            
            Log.d(TAG, "using a rotation of " + rotation + " degrees");

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap = rotateBitmap(bitmap, rotation);
            
            Log.d(TAG, "rotated image width: " + bitmap.getWidth());
            Log.d(TAG, "rotated image height: " + bitmap.getHeight());
            
            Log.d(TAG, "rotated bmp is null? " + (bitmap == null));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            
            if(bitmap != null) {
                boolean compressResult = bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Log.d(TAG, "compressed bitmap result: " + compressResult);
            } else {
                Log.e(TAG, "rotated bitmap is null!!");
            }
            byte[] byteArray = stream.toByteArray();
            
            Log.d(TAG, "length of byte array: " + byteArray.length);
            
            Picture newPic = new Picture(filename, new AcneLevel(1, "1"), byteArray);
            db.addPicture(newPic);
            
            pictureData = newPic.getPic();
            
//            selfie = newPic;
            selfiePath = newPic.getFilePath();
            Log.d(TAG, "set file path to " + selfiePath);
            
            Log.d(TAG, "saved picture to " + filename);

            Toast.makeText(getApplicationContext(), "Took picture", Toast.LENGTH_SHORT).show();

            DialogFragment diagOptFrag = new DiagnosisOptionsFragment();
            diagOptFrag.show(getFragmentManager(), "diag_opt_dialog");
        }
    };
    
    private Bitmap rotateBitmap(Bitmap in, int angle) {
        Matrix mat = new Matrix();
        mat.postRotate(angle);
        return Bitmap.createBitmap(in, 0, 0, in.getWidth(), in.getHeight(), mat, true);
    }

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
        Log.d(TAG, "is path empty? " + (selfiePath == null));
        intent.putExtra("picturePath", selfiePath);
        startActivity(intent);
    }
    
    private class loadDbTask extends AsyncTask<Context, Void, Void> {
        
        protected Void doInBackground(Context... contexts) {
            
            db = new LocalDb(contexts[0]);
            
            return null;
        }
        
    }
}
