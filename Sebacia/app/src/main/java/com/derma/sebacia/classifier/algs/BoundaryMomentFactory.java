package main.java.com.derma.sebacia.classifier.algs;


import main.java.com.derma.sebacia.classifier.structs.BoundaryMomentDescriptor;
import main.java.com.derma.sebacia.classifier.structs.Shape;

/**
 * Created by Jessie on 9/30/2015.
 */
public class BoundaryMomentFactory extends DescriptorFactory<Shape, BoundaryMomentDescriptor> {

    public void computeUnit (Shape shape, BoundaryMomentDescriptor moments)
    {
        /* compute the minimum and maximum values with more than 0 mass and the mean */
        double min = 0.0, max = Double.POSITIVE_INFINITY, mean = 0;
        for (int i = 0; i < shape.N; i++)
        {
            moments.min = Math.min(moments.min, shape.signatures[i].value);
            moments.max = Math.max(moments.max, shape.signatures[i].value);
            moments.mean += shape.signatures[i].value;
        }
        moments.mean /= shape.N;

        /* set the histogram data structure */
        moments.setHistogram();

        /* compute the mass of the histogram */
        for (int i = 0; i < shape.N; i++)
        {
            moments.histogram[moments.getHistogramIndex(shape.signatures[i].value)] += 1;
        }

        /* normalize the histogram so that it is a probability distribution */
        for (int i = 0; i < moments.binNum; i++)
        {
            moments.histogram[i] /= shape.N;
        }

        /* compute all the central moments */
        double p;
        moments.moments[0] = 0;
        for (int r = 1; r < moments.M; r++)
        {
            for (int i = 0; i < shape.N; i++)
            {
                p = moments.histogram[moments.getHistogramIndex(shape.signatures[i].value)];
                moments.moments[r] += Math.pow(shape.signatures[i].value - moments.mean, r+1) * p ;
            }
        }
    }
}
