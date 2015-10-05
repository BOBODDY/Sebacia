package main.java.com.derma.sebacia.classifier.structs;

import boofcv.struct.image.ImageFloat32;

/**
 * Created by Jessie on 10/5/2015.
 */
public class ColorHistogramDescriptor extends Histogram {

    public static final int binNum = 256;
    public static final double binSize = 256 / binNum;
    /* this is in units of: color per standard deviation in healthy skin color */
    public double shift;
    /* the variance will be used as a "peakedness" measure */

    public ColorHistogramDescriptor ()
    {
        super();
        shift = 0;
        histogram = new double[binNum];
    }

    public int getHistogramIndex (double val)
    {
        return (int)Math.floor(val / binSize);
    }

}
