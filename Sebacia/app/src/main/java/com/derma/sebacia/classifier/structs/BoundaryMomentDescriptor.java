package main.java.com.derma.sebacia.classifier.structs;

/**
 * Created by Jessie on 9/30/2015.
 */
public class BoundaryMomentDescriptor extends Histogram {

    public static final double binSize = 0.1;
    public int binNum;
    /* the min and max values with more than 0 probability and the mean */
    public double min, max;
    /* the number of moments */
    public static final int M = 4;
    /* the moments are central moments */
    public double[] moments;

    public BoundaryMomentDescriptor ()
    {
        super();
        min = max = 0;
        moments = new double[M];
    }

    public void setHistogram ()
    {
        /* add 1 because (max - mean) / binSize = binNum */
        binNum = (int)Math.ceil((max - min) / binSize) + 1;
        histogram = new double[binNum];
    }

    public int getHistogramIndex (double val)
    {
        return (int)Math.floor((val - mean) / binSize);
    }

}
