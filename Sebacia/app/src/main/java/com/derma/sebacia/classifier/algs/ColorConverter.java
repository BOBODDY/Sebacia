package main.java.com.derma.sebacia.classifier.algs;

import boofcv.alg.misc.PixelMath;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.MultiSpectral;

/**
 * Created by Jessie on 10/26/2015.
 */
public class ColorConverter {
    /* TODO: MAKE SURE EVERYTHING IS DEALNG WITH IMAGES THAT ARE ON 0-1 SCALE TO AVOID CONFUSION */

    public static MultiSpectral<ImageFloat32> YCbCrFromRGB (MultiSpectral<ImageFloat32> rgb)
    {
        for (int i = 0; i < 3; i++)
        {
            PixelMath.divide(rgb.getBand(i), 255.0f, rgb.getBand(i));
        }

        MultiSpectral<ImageFloat32> ycbcr = new MultiSpectral<ImageFloat32>(ImageFloat32.class, rgb.getWidth(), rgb.getHeight(), 3);
        float r, g, b;
        for (int i = 0; i < rgb.getWidth(); i++)
        {
            for (int j = 0; j < rgb.getHeight(); j++)
            {
                r = rgb.getBand(0).get(i,j);
                g = rgb.getBand(1).get(i,j);
                b = rgb.getBand(2).get(i,j);
                ycbcr.getBand(0).set(i,j, (float)(0.299 * (double)r + 0.587 * (double)g + 0.114 * (double)b));
                ycbcr.getBand(1).set(i,j, (float)(-0.169 * (double)r - 0.331 * (double)g + 0.500 * (double)b));
                ycbcr.getBand(2).set(i,j, (float)(0.500 * (double)r - 0.419 * (double)g - 0.081 * (double)b));
            }
        }

        for (int i = 0; i < 3; i++)
        {
            PixelMath.multiply(rgb.getBand(i), 255.0f, rgb.getBand(i));
        }

        return ycbcr;
    }

    public static MultiSpectral<ImageFloat32> LABFromRGB (MultiSpectral<ImageFloat32> rgb)
    {
        for (int i = 0; i < 3; i++)
        {
            PixelMath.divide(rgb.getBand(i), 255.0f, rgb.getBand(i));
        }

        MultiSpectral<ImageFloat32> lab = getLABFromXYZ(getXYZFromRGB(rgb));

        /* normalize the L channel */
        PixelMath.divide(lab.getBand(0), 100.0f, lab.getBand(0));
        /* normalize the A channel */
        PixelMath.minus(lab.getBand(1), -128.0f, lab.getBand(1));
        PixelMath.divide(lab.getBand(1), 256.0f, lab.getBand(1));
        /* normalize the B channel */
        PixelMath.minus(lab.getBand(2), -128.0f, lab.getBand(2));
        PixelMath.divide(lab.getBand(2), 256.0f, lab.getBand(2));

        for (int i = 0; i < 3; i++)
        {
            PixelMath.multiply(rgb.getBand(i), 255.0f, rgb.getBand(i));
        }

        return lab;
    }

    private static MultiSpectral<ImageFloat32> getXYZFromRGB (MultiSpectral<ImageFloat32> imgRGB)
    {
        MultiSpectral<ImageFloat32> imgXYZ = new MultiSpectral<ImageFloat32>(ImageFloat32.class, imgRGB.getWidth(), imgRGB.getHeight(), 3);
        /* TODO : makes these static in the color conversion class */
        double Xn = 95.047;
        double Yn = 100.000;
        double Zn = 108.883;

        float r, g, b;
        for (int i = 0; i < imgRGB.getWidth(); i++)
        {
            for (int j = 0; j < imgRGB.getHeight(); j++)
            {
                r = imgRGB.getBand(0).get(i,j);
                g = imgRGB.getBand(1).get(i,j);
                b = imgRGB.getBand(2).get(i,j);
                imgXYZ.getBand(0).set(i,j, (float)(Yn * (0.412453 * (double)r + 0.357580 * (double)g + 0.180423 * (double)b)));
                imgXYZ.getBand(1).set(i,j, (float)(Yn * (0.212671 * (double)r + 0.715160 * (double)g + 0.072169 * (double)b)));
                imgXYZ.getBand(2).set(i,j, (float)(Yn * (0.019334 * (double)r + 0.119193 * (double)g + 0.950227 * (double)b)));
            }
        }

        return imgXYZ;
    }

    private static MultiSpectral<ImageFloat32> getLABFromXYZ (MultiSpectral<ImageFloat32> imgXYZ)
    {
        MultiSpectral<ImageFloat32> imgLAB = new MultiSpectral<ImageFloat32>(ImageFloat32.class, imgXYZ.getWidth(), imgXYZ.getHeight(), 3);
        /* TODO : makes these static in the color conversion class */
        double Xn = 95.047;
        double Yn = 100.000;
        double Zn = 108.883;

        float x, y, z;
        double xr, yr, zr, xt, yt ,zt;
        for (int i = 0; i < imgXYZ.getWidth(); i++)
        {
            for (int j = 0; j < imgXYZ.getHeight(); j++)
            {
                xr = imgXYZ.getBand(0).get(i,j) / Xn;
                yr = imgXYZ.getBand(1).get(i,j) / Yn;
                zr = imgXYZ.getBand(2).get(i,j) / Zn;
                if (yr > 0.008856)
                {
                    imgLAB.getBand(0).set(i,j, (float)(116.0 * Math.pow(yr, 1.0 / 3.0) - 16.0));
                }
                else
                {
                    imgLAB.getBand(0).set(i,j, (float)(903.3 * yr));
                }
                xt = f(xr);
                yt = f(yr);
                zt = f(zr);
                imgLAB.getBand(1).set(i,j, (float)(500.0 * (xt - yt)));
                imgLAB.getBand(2).set(i,j, (float)(200.0 * (yt - zt)));
            }
        }

        return imgLAB;
    }

    /* This is purely a helper method for XYZ -> LAB conversion */
    public static double f (double t)
    {
        return (t > 0.008856) ? Math.pow(t, 1.0/3.0) : 7.787 * t + 16.0/116.0;
    }

}
