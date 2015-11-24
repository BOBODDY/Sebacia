package main.java.com.derma.sebacia.classifier.algs;


import georegression.struct.point.Point2D_I32;
import main.java.com.derma.sebacia.classifier.structs.FourierSpectrumImage;
import main.java.com.derma.sebacia.classifier.structs.Texture;
import main.java.com.derma.sebacia.classifier.structs.TextureBrowsingDescriptor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jessie on 10/2/2015.
 */
public class TextureBrowsingFactory extends DescriptorFactory<Texture, TextureBrowsingDescriptor> {

    private static final double wedgeSize = 36;
    private static final int numWedges = (int)(360 / wedgeSize);
    private static final double[] wedgeCutOffs = {0, 36, 72, 108, 144, 180, 216, 252, 288, 324, 360};
    /* TODO: ensure these values work, they were determinded empirically for the Brodatz album dataset */
    private static final double[] regularityCutoffs = {0, 5, 17, 30, Integer.MAX_VALUE};
    private static final double cutoffPercentage = 0.75;
    private static final double minEigenRatioForDirectionalClass = 2;
    private static final double houghThetaBinSize = 3;
    private static final int houghThetaNumBins = (int)(360 / houghThetaBinSize);
    private static final double houghRhoBinSize = 5;
    private static final int FtoGRNumSteps = 100;
    private static final double FtoGRStep = 1.0 / FtoGRNumSteps;
    private static double[] FtoGRUnits = new double[FtoGRNumSteps];
    private static final double[] scaleRBinCutoffs = {1.0, 1.0/2, 1.0/4, 1.0/8, 1.0/16};

    public static class FTGRSteps
    {
        public FTGRSteps ()
        {
            for (int i = 0; i <= FtoGRNumSteps; i++)
            {
                FtoGRUnits[i] = FtoGRStep * i;
            }
        }
    }

    public void computeUnit (Texture texture, TextureBrowsingDescriptor tbd)
    {
        /* 1. compute the smoothed the spectrum image, F' */
        FourierSpectrumImage smoothed = FourierSpectrumImage.computeSmoothedCopy(texture.spectrum);

        /* 2. compute the enhanced spectrum image, F'' */
        FourierSpectrumImage enhanced = FourierSpectrumImage.computeEnhancedCopy(smoothed);

        /* 3. compute regularity (v1) using F'' */
        computeTBDRegularity(enhanced, tbd);

        /* 4. compute the directionality (v2, v4) using F' */
        double[] theta = computeTBDDirectionality(smoothed, tbd);

        /* 5. compute scale (v3, v5) along the dominant directions using the spectrum image F */
        computeTBDScale(texture.spectrum, theta, tbd);
     }

    private void computeTBDRegularity (FourierSpectrumImage enhanced, TextureBrowsingDescriptor tbd)
    {
        /* 3.1 compute wedge distribution */
        double[] radialWedgeDistribution = new double[numWedges];
        double angle = 0, totalEnergy = 0;
        int w;
        for (int u = 0; u < enhanced.width; u++)
        {
            for (int v = 0; v < enhanced.height; v++)
            {
                /* TODO : angle variable can be precalculated after you figure our range of image dimensions */
                angle = Math.toDegrees(Math.PI + Math.atan2(u,v));
                w = 0;
                while (!(wedgeCutOffs[w] <= angle && angle < wedgeCutOffs[w+1]))
                {
                    w++;
                }
                radialWedgeDistribution[w] += enhanced.magnitude.get(u,v);
                totalEnergy += enhanced.magnitude.get(u,v);
            }
        }

        /* 3.2 normalize wedge distribution */
        for (int i = 0; i < numWedges; i++)
        {
            radialWedgeDistribution[i] /= totalEnergy;
        }

        /* 3.3 compute wedge mean */
        double wedgeMean = 0;
        for (int i = 0; i < numWedges; i++)
        {
            wedgeMean += radialWedgeDistribution[i];
        }
        wedgeMean /= numWedges;

        /* 3.3 compute wedge variance */
        double wedgeVariance = 0;
        for (int i = 0; i < numWedges; i++)
        {
            wedgeVariance += Math.pow(radialWedgeDistribution[i] - wedgeMean, 2);
        }
        wedgeVariance /= numWedges;

        /* 3.4 set regularity based on variance */
        int v = 0;
        while (!(regularityCutoffs[v] <= wedgeVariance && wedgeVariance < regularityCutoffs[v+1]))
        {
            v++;
        }
        tbd.pbc[0] = v;
    }

    private double[] computeTBDDirectionality (FourierSpectrumImage smoothed, TextureBrowsingDescriptor tbd) {
        /* the return value */
        double[] rawDirections = new double[2];

        /* 4.1 compute the maximum magnitude of the spectrum image */
        double maxResponseF = 0;
        for (int u = 0; u < smoothed.width; u++)
        {
            for (int v = 0; v < smoothed.height; v++)
            {
                maxResponseF = Math.max(maxResponseF, smoothed.magnitude.get(u, v));
            }
        }

        /* 4.2 compute the histogram of magnitude values */
        int maxResponseI = (int) Math.ceil(maxResponseF);
        double[] histogram = new double[maxResponseI + 1];
        for (int u = 0; u < smoothed.width; u++)
        {
            for (int v = 0; v < smoothed.height; v++)
            {
                histogram[(int) Math.ceil(smoothed.magnitude.get(u, v))] += 1;
            }
        }

        /* 4.3 compute the total magnitude of the spectrum image */
        double energy = 0;
        for (int u = 0; u < smoothed.width; u++)
        {
            for (int v = 0; v < smoothed.height; v++)
            {
                energy += smoothed.magnitude.get(u, v);
            }
        }

        /* 4.4 compute cumulative energy of the histogram, note that lower cumulative energies are on the right
         *     of the histograms */
        double[] cumulativeEnergies = new double[histogram.length];
        for (int i = 0; i < cumulativeEnergies.length; i++)
        {
            for (int j = i; j < cumulativeEnergies.length; j++)
            {
                cumulativeEnergies[i] += histogram[j];
            }
        }

        /* 4.5 determine threshold for high value spectral pixels by determining the value of energy for which a large
         *     percentage of pixels are lower than */
        int l = cumulativeEnergies.length - 1;
        while ((cumulativeEnergies[l] / energy) < cutoffPercentage)
        {
            --l;
        }

        /* 4.6 get all the high value pixels */
        List<Point2D_I32> highValPixels = new ArrayList<Point2D_I32>();
        for (int u = 0; u < smoothed.width; u++)
        {
            for (int v = 0; v < smoothed.height; v++)
            {
                if (smoothed.magnitude.get(u, v) >= cumulativeEnergies[l]) {
                    highValPixels.add(new Point2D_I32(u, v));
                }
            }
        }

        /*
        /* 4.7 compute the mean location of the spectral values (meanpt = sum(pt_i * (response_i / totalresponse))) */
        Point2D_I32 highValPix;
        float u_mean = 0, v_mean = 0, response = 0, totalHighValResponse = 0;
        Iterator<Point2D_I32> pixelIterator = highValPixels.iterator();
        while (pixelIterator.hasNext())
        {
            highValPix = pixelIterator.next();
            response = smoothed.magnitude.get(highValPix.x, highValPix.y);
            u_mean += response * highValPix.x;
            v_mean += response * highValPix.y;
            totalHighValResponse += response;
        }
        u_mean /= totalHighValResponse;
        v_mean /= totalHighValResponse;

        /* 4.8 compute the covariance matrix of the responses for PCA */
        double c_uu = 0, c_vv = 0, c_uv = 0;
        pixelIterator = highValPixels.iterator();
        while (pixelIterator.hasNext())
        {
            highValPix = pixelIterator.next();
            response = smoothed.magnitude.get(highValPix.x, highValPix.y);
            c_uu = response * Math.pow(highValPix.x - u_mean, 2);
            c_vv = response * Math.pow(highValPix.y - v_mean, 2);
            c_uv = response * (highValPix.x - u_mean) * (highValPix.y - v_mean);
        }
        c_uu /= totalHighValResponse;
        c_vv /= totalHighValResponse;
        c_uv /= totalHighValResponse;

        /* 4.9 get the principal components */
        double[] eigenVals = new double[2];
        double[] eigenVec1 = new double[2];
        double[] eigenVec2 = new double[2];
        getEigenValuesOf2x2(c_uu, c_uv, c_uv, c_vv, eigenVals, eigenVec1, eigenVec2);

        /* 4.10 if there is one dominant direction, then there is one line in the magnitude image of very
                high spectral values, the dominant direction is perpendicular to this line */
        if ((eigenVals[0] / eigenVals[1]) >= minEigenRatioForDirectionalClass)
        {
            rawDirections[0] = rawDirections[1] = Math.atan2(eigenVec1[1], eigenVec1[0]);
            tbd.pbc[1] = tbd.pbc[3] = (int) Math.floor(Math.toDegrees(rawDirections[0])) + 90;
            return rawDirections;
        }

        /*
        * TODO: steps 4.7-4.10 figure out if a texture has one dominant direction and if it does only accounts
        * for one dominant direction, but the original tbd always assumes 2 dominant directions, this
        * implementation is still much faster and very accurate so see what difference steps 4.7-4.10 make in
        * the results
        * */

        /* 4.11 compute the hough transform of the high value pixels to get the two dominant directions */
        int houghRhoNumBins = (int) (Math.ceil(Math.sqrt(smoothed.width * smoothed.width + smoothed.height * smoothed.height)) / houghRhoBinSize);
        int[][] accumulator = new int[houghThetaNumBins][houghRhoNumBins];
        int thetabin = 0, rhobin = 0;
        double theta = 0, rho = 0;
        pixelIterator = highValPixels.iterator();
        while (pixelIterator.hasNext())
        {
            highValPix = pixelIterator.next();
            for (thetabin = 0; thetabin < houghThetaNumBins; thetabin++)
            {
                theta = thetabin * houghThetaNumBins - 180;
                rho = highValPix.x*Math.cos(theta) + highValPix.y*Math.sin(theta);
                rhobin = (int)Math.floor(rho/houghRhoNumBins);
                accumulator[thetabin][rhobin]++;
            }
        }

        /* 4.12 get the two angles with the most votes from the hough accumulator, those are the dominant directions */
        int maxvotes1 = 0, maxvotes2 = 0;
        for (int i = 0; i < houghThetaNumBins; i++)
        {
            for (int j = 0; j < houghRhoNumBins; j++)
            {
                if (accumulator[i][j] > maxvotes2)
                {
                    if (accumulator[i][j] > maxvotes1)
                    {
                        maxvotes1 = accumulator[i][j];
                        rawDirections[0] = i * houghThetaNumBins - 180;
                    }
                    else
                    {
                        maxvotes2 = accumulator[i][j];
                        rawDirections[1] = i * houghThetaNumBins - 180;
                    }
                }
            }
        }

        tbd.pbc[1] = ((int)rawDirections[0] + 45) / 30;
        tbd.pbc[3] = ((int)rawDirections[1] + 45) / 30;

        return rawDirections;
    }

    private void computeTBDScale (FourierSpectrumImage spectrum, double[] theta, TextureBrowsingDescriptor tbd)
    {
        /* 5.1 transform points F(u,v) into G(r,theta) for the two dominant directions */
        double[][] G = new double[FtoGRNumSteps + 1][2];
        int x, y;
        double[] coeffDimOVer2 = {spectrum.width / 2.0, spectrum.height / 2.0};
        double[] coeffPerUnit = {0,0};
        double[][] cosSinTheta = {{Math.cos(theta[0]), Math.sin(theta[0])}, {Math.cos(theta[1]), Math.sin(theta[1])}};
        for (int i = 0; i <= FtoGRNumSteps; i++)
        {
            coeffPerUnit[0] = coeffDimOVer2[0] * FtoGRUnits[i];
            coeffPerUnit[1] = coeffDimOVer2[1] * FtoGRUnits[i];
            for (int j = 0; j < 2; j++)
            {
                x = (int)Math.floor(coeffPerUnit[0] * cosSinTheta[j][0]);
                y = (int)Math.floor(coeffPerUnit[1] * cosSinTheta[j][1]);

                if (spectrum.coordinatesInImage(x,y))
                {
                    G[i][j] = spectrum.magnitude.get(x,y);
                }
            }
        }

        /* 5.2 for each dominant direction partition the energy into frequency bins, high frequency to low frequency */
        double[][] frequencyBins = {{0,0,0,0},{0,0,0,0}};
        int b = 0;
        for (int i = 0; i <= FtoGRNumSteps; i++)
        {
            while (!(scaleRBinCutoffs[b] <= FtoGRUnits[i] && FtoGRUnits[i] < scaleRBinCutoffs[b + 1]) && b < 4)
            {
                ++b;
            }
            if (b < 4)
            {
                frequencyBins[0][b] += G[i][0];
                frequencyBins[1][b] += G[i][1];
            }
        }

        /* 5.3 for each dominant direction, its scale is the index of the bin with the highest frequency  */
        double[] maxEnergy = {0,0};
        for (int j = 0; j < 4; j++)
        {
            if (frequencyBins[0][j] > maxEnergy[0])
            {
                maxEnergy[0] = frequencyBins[0][j];
                tbd.pbc[2] = j;
            }
            if (frequencyBins[1][j] > maxEnergy[1])
            {
                maxEnergy[1] = frequencyBins[1][j];
                tbd.pbc[4] = j;
            }
        }
    }

    private void getEigenValuesOf2x2 (double x_11, double x_12, double x_21, double x_22,
                                      double[] eigenVals, double[] eigenVec1, double[] eigenVec2)
    {
        double T = x_11 + x_22;
        double D = x_11 * x_22 - x_12 * x_21;
        double left = T / 2;
        double right = Math.sqrt(T*T / (4-D));

        eigenVals[0] = left + right;
        eigenVals[1] = left - right;

        if (x_21 != 0)
        {
            eigenVec1[0] = eigenVals[0] - x_22;
            eigenVec1[1] = x_21;
            eigenVec2[0] = eigenVals[1] - x_22;
            eigenVec2[1] = x_21;
        }
        else if (x_12 != 0)
        {
            eigenVec1[0] = x_12;
            eigenVec1[1] = eigenVals[0] - x_22;
            eigenVec2[0] = x_12;
            eigenVec2[1] = eigenVals[1] - x_22;
        }
        else if (x_12 == 0 && x_21 == 0)
        {
            eigenVec1[0] = 1;
            eigenVec1[1] = 0;
            eigenVec2[0] = 0;
            eigenVec2[1] = 1;
        }
    }

    private double getAngleOfVectorWithHoriontalAxis(double[] vec)
    {
        /* return acos (dot(vec, x-axis) / (||vec||*||x-axis||))*/
        return Math.acos((vec[0]) / (Math.sqrt(vec[0]*vec[0] + vec[1]*vec[1])));
    }

}
