package main.java.com.derma.sebacia.classifier.classifiers;

import java.util.Arrays;

/**
 * Created by Jessie on 10/6/2015.
 */
public class Classifier {

    public static double getMedianDistance (double[] distances)
    {
        double[] sorted = new double[distances.length];
        for(int i = 0; i < distances.length; i++) { sorted[i] = distances[i]; }
        Arrays.sort(sorted);
        double halfSum = 0;
        double diff = 0;
        for(int i = 0; i < distances.length; i++)
        {
            halfSum += sorted[i];
        }
        halfSum /= 2.0;

        for(int i = 0; i < distances.length; i++)
        {
            if(sorted[i] > halfSum)
            {
                diff = sorted[i] - sorted[i-1];
                return ((halfSum-sorted[i-1])/diff)*sorted[i-1] + ((sorted[i]-halfSum)/diff)*sorted[i];
            }
            else if(sorted[i] == halfSum)
            {
                /* return sorted[i] */
                break;
            }
        }
        return halfSum;
    }
}
