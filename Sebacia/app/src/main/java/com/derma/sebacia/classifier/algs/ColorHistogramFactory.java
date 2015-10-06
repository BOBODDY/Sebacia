package com.derma.sebacia.classifier.algs;


import boofcv.struct.image.ImageFloat32;
import com.derma.sebacia.classifier.structs.ColorHistogramDescriptor;

/**
 * Created by Jessie on 10/5/2015.
 */
public class ColorHistogramFactory extends DescriptorFactory<ImageFloat32, ColorHistogramDescriptor> {

    public static ColorHistogramDescriptor healthyHistogram;

    public static void computeHealthyHistogram (ColorHistogramFactory chf, ImageFloat32 healthySkin)
    {
        chf.computeUnit(healthySkin, healthyHistogram);
    }

    public void computeUnit (ImageFloat32 image, ColorHistogramDescriptor histogram)
    {
        /* compute the histogram of colors and the mean */
        double total = image.width * image.height;
         for (int i = 0; i < image.width; i++)
        {
            for (int j = 0; j < image.width; j++)
            {
                histogram.histogram[histogram.getHistogramIndex(image.get(i,j))] += 1;
                histogram.mean += image.get(i,j);
            }
        }
        histogram.mean /= total;

        /* make the histogram a probability distribution */
        for (int i = 0; i < ColorHistogramDescriptor.binNum; i++)
        {
            histogram.histogram[i] /= total;
        }

        /* compute the variance and the mean */
        int k0 = 0, k1 = 0;
        double mass0 = 0, mass1 = 0 , p = 0;
        for (int i = 0; i < image.width; i++)
        {
            for (int j = 0; j < image.width; j++)
            {
                k1 = histogram.getHistogramIndex(image.get(i, j));
                p = histogram.histogram[k1];
                histogram.variance += Math.pow(image.get(i,j) - histogram.mean, 2) * p;

                mass1 += p;
                if (mass1 == 0.5)
                {
                    histogram.median = k1;
                }
                else if (mass1 > 0.5)
                {
                    double diff = mass1 - mass0;
                    histogram.median = (mass0/diff)*k0 + (mass1/diff)*k1;
                }
            }
            k0 = k1;
            mass0 = mass1;
        }

        /* compute the distance between the means of this histogram and the healthy histogram */
        histogram.shift = Math.abs(healthyHistogram.mean - histogram.mean) / Math.sqrt(healthyHistogram.variance);
    }

}
