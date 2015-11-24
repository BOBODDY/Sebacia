package main.java.com.derma.sebacia.classifier.structs;

/**
 * Created by Jessie on 10/5/2015.
 */
public class Histogram extends Descriptor {

    public double[] histogram;
    public double mean, median, mode, variance;

    public Histogram ()
    {
        mean = median = mode = variance = 0;
    }

}
