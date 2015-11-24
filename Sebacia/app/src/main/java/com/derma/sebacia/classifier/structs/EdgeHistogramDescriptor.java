package com.derma.sebacia.classifier.structs;

/**
 * Created by Jessie on 10/4/2015.
 */
public class EdgeHistogramDescriptor extends Histogram {

    public EdgeHistogramDescriptor ()
    {
        super();
        histogram = new double[80];
    }

    public void incrementBin (int r, int c, int e)
    {
        /* each row has 5 columns, each column has 5 edges */
        histogram[r*25 + c*5 + e]++;
    }

}
