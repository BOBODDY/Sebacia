package com.derma.sebacia.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.DialogFragment;

import com.derma.sebacia.R;
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
    private ImageView overlay;
    private ProgressBar loading;
    LocalDb db;

    private static String TAG = "Sebacia";
    
//    private Picture selfie;
    private String selfiePath;
    private byte[] thumbData; // Byte array of picture thumbnail to send to Survey

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        
        loading = (ProgressBar) findViewById(R.id.camera_loading);
        
//        db = new LocalDb(getApplicationContext());
        new loadDbTask().execute(getApplicationContext());
        
        Log.d(TAG, "in CameraActivity");

        // Create an instance of Camera
        mCamera = getCameraInstance();
        
        overlay = (ImageView) findViewById(R.id.camera_overlay);

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
//                    captureButton.setEnabled(false);
//                    diagnoseButton.setEnabled(false);
                    loading.setVisibility(View.VISIBLE);
                    
                    mCamera.takePicture(null, null, mPicture);
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
    
    protected void onStart() {
        super.onStart();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.camera_tutorial);
        builder.setTitle("Tutorial");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        AlertDialog dialog = builder.create();
        dialog.show();
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
            Log.v(TAG, "in PictureCallback");

            String s = getFilesDir() + File.separator + getImageFileName();
            File tmpF = new File(s);
            try {
                selfiePath = tmpF.getCanonicalPath();
            } catch(IOException ioe) {

            }
            
            DialogFragment diagOptFrag = new DiagnosisOptionsFragment();
            diagOptFrag.show(getFragmentManager(), "diag_opt_dialog");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
            
            options.inSampleSize = calculateInSampleSize(options, 200, 200);
            
            options.inJustDecodeBounds = false;
            
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            int displayRotation = getDisplayRotation();
            bmp = rotateBitmap(bmp, displayRotation);
            
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            
            bmp.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            
            thumbData = byteStream.toByteArray();
            
            new SavePictureTask().execute(data);
        }
    };

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    
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
            Log.e(TAG, "Camera is not available (in use or does not exist)", e);
        }
        return c; // returns null if camera is unavailable
    }

    public void beginAutoClassification(View view) {
        // TODO : add code for going to classification results page
    }

    public void beginSurvey(View view) {
        Intent intent = new Intent(this, SurveyActivity.class);
        Log.d(TAG, "is path empty? " + (selfiePath == null));
        try {
            intent.putExtra("picturePath", selfiePath);
            intent.putExtra("thumbnail", thumbData);
        } catch (NullPointerException e) {
            // TODO: Exit the app or handle when there is no camera
        }
        startActivity(intent);
    }
    
    private int getDisplayRotation() {
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
        return rotation;
    }
    
    private class SavePictureTask extends AsyncTask<byte[], Void, Void> {
        protected Void doInBackground(byte[]... bytes) {
            byte[] data = bytes[0];

            Log.d(TAG, "in PictureCallback");

            String filename = selfiePath;

            Picture newPic = new Picture(filename, new AcneLevel(1, "1"));

            db.addPicture(newPic);
            Log.d(TAG, "saved picture to " + filename);

            int rotation = getDisplayRotation();

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap = rotateBitmap(bitmap, rotation);
            
            File f = new File(filename);
            Log.d(TAG, "Saving picture to: " + f.getAbsolutePath());

            try {
                FileOutputStream fos = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                
                filename = f.getCanonicalPath();
            } catch(FileNotFoundException fnfe) {
                Log.e(TAG, "failed to write image", fnfe);
            } catch(IOException ioe) {
                Log.e(TAG, "error closing file output stream", ioe);
            }

            return null;
        }
    }
    
    private class loadDbTask extends AsyncTask<Context, Void, Void> {
        
        protected Void doInBackground(Context... contexts) {
            
            db = new LocalDb(contexts[0]);
            
            return null;
        }
        
    }
}
