package com.derma.sebacia.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Daniel on 9/12/2015.
 */
public class Picture {

    String FilePath;
    AcneLevel Severity;
    byte[] Pic;
    Bitmap bmp;

    //FUNCTIONALITY
    public Bitmap getPicBitmap() {
        if(bmp == null) {
            if (Pic != null) {
                return BitmapFactory.decodeByteArray(Pic, 0, Pic.length);
            } else {
                return null;
            }
        } else {
            return bmp;
        }
    }


    //CONSTRUCTOR
    public Picture(String filePath, AcneLevel severity, byte[] pic) {
        FilePath = filePath;
        Severity = severity;
        Pic = pic;
    }

    public Picture(String filePath, AcneLevel severity, Bitmap bmp) {
        FilePath = filePath;
        Severity = severity;
        this.bmp = bmp;
    }
    //SETTERS
    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public void setSeverity(AcneLevel severity) {
        Severity = severity;
    }

    public void setPic(byte[] pic) {
        Pic = pic;
    }

    //GETTERS
    public String getFilePath() {
        return FilePath;
    }

    public byte[] getPic() {
        if(Pic == null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if(bmp != null) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            }
            Pic = stream.toByteArray();
        }
        return Pic;
    }

    public AcneLevel getSeverity() {
        return Severity;
    }
}
