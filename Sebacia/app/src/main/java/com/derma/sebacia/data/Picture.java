package com.derma.sebacia.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Daniel on 9/12/2015.
 */
public class Picture {

    String FilePath;
    AcneLevel Severity;

    //FUNCTIONALITY
    public Bitmap getPicBitmap() {
        Bitmap bmp = null;
        if(FilePath != null && !FilePath.equals("")) {
            bmp = BitmapFactory.decodeFile(FilePath);
        }
        return bmp;
    }
    
    public Bitmap getPicThumbnail() {
        Bitmap bmp = null;
        if(FilePath != null && !FilePath.equals("")) {
            
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(FilePath, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 200, 200);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeFile(FilePath, options);
        }
        return bmp;
    }

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

    //CONSTRUCTOR
    public Picture(String filePath, AcneLevel severity) {
        FilePath = filePath;
        Severity = severity;
    }
    //SETTERS
    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public void setSeverity(AcneLevel severity) {
        Severity = severity;
    }

    //GETTERS
    public String getFilePath() {
        return FilePath;
    }

    public AcneLevel getSeverity() {
        return Severity;
    }
}
