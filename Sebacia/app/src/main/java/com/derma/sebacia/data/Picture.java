package com.derma.sebacia.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Daniel on 9/12/2015.
 */
public class Picture {

    String FilePath;
    AcneLevel Severity;
    byte[] Pic;

    //FUNCTIONALITY
    public Bitmap getPicBitmap() {
        if(Pic != null) {
            return BitmapFactory.decodeByteArray(Pic, 0, Pic.length);
        } else {
            return null;
        }
    }


    //CONSTRUCTOR
    public Picture(String filePath, AcneLevel severity, byte[] pic) {
        FilePath = filePath;
        Severity = severity;
        Pic = pic;
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
        return Pic;
    }

    public AcneLevel getSeverity() {
        return Severity;
    }
}
