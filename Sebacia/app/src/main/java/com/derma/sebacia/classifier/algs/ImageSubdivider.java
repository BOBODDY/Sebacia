package com.derma.sebacia.classifier.algs;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;
import georegression.struct.point.Point2D_I32;
import com.derma.sebacia.classifier.structs.ImageRegion;
import com.derma.sebacia.classifier.structs.RegionImage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jessie on 10/4/2015.
 */
public class ImageSubdivider {

    public static final int imageWidth = 1080;
    public static final int imageHeight = 1920;
    public static final int regionDim = 8;
    public static final int widthInRegions = imageWidth / regionDim;
    public static final int heightInRegions = imageHeight / regionDim;

    public static RegionImage divideImage (ImageFloat32 image)
    {
        RegionImage regions = new RegionImage(widthInRegions, heightInRegions, regionDim);
        int x, y;
        for (int i = 0; i < widthInRegions; i++)
        {
            x = i * regionDim;
            for (int j = 0; j < heightInRegions; j++)
            {
                y = j * regionDim;
                regions.regions[i][j] = new ImageRegion(image.subimage(x, y, x+regionDim, y+regionDim), x, y);
            }
        }

        return regions;
    }

    public static void divideEdges (RegionImage regions, ImageUInt8 edgeImage)
    {
        int x, y;
        ImageUInt8 edgeSubImage;
        for (int i = 0; i < widthInRegions; i++)
        {
            x = i * regionDim;
            for (int j = 0; j < heightInRegions; j++)
            {
                if (regions.regions[i][j].isSkin)
                {
                    y = j * regionDim;
                    edgeSubImage = edgeImage.subimage(x, y, x+regionDim, y+regionDim);
                    /* TODO : the assumption is that the largest contour in the region is the shape if there
                     *        is a visible shape, make sure this is a good assumption */
                    regions.regions[i][j].setShape(getLargestContour(BinaryImageOps.contour(edgeSubImage, ConnectRule.EIGHT, null)));
                }
            }
        }
    }

    public static void setSpectrumOfRegions (RegionImage regions)
    {
        for (int i = 0; i < widthInRegions; i++)
        {
            for (int j = 0; j < heightInRegions; j++)
            {
                if (regions.regions[i][j].isSkin)
                {
                    regions.regions[i][j].texture.setSpectrum();
                }
            }
        }
    }

    private static Contour getLargestContour (List<Contour> contours)
    {
        int largestSize = 0, largestI = 0, i = 0;
        Iterator<Contour> contourIterator = contours.iterator();
        Contour contour;
        while (contourIterator.hasNext())
        {
            contour = contourIterator.next();
            if (contour.external.size() > largestSize)
            {
                largestSize = contour.external.size();
                largestI = i;
            }
            ++i;
        }

        return contours.get(largestI);
    }

    public static void divideContours (RegionImage regions, List<Contour> contourList)
    {
        /* iterate through all the Contours adding coordinates from each contour to its respective region */
        Iterator<Contour> contourIterator = contourList.iterator();
        Iterator<Point2D_I32> ptIterator;
        Contour edge;
        Point2D_I32 ptC, ptR;
        Contour[][] contours = new Contour[regions.width][regions.height];
        while (contourIterator.hasNext())
        {
            edge = contourIterator.next();
            ptIterator = edge.external.iterator();
            while (ptIterator.hasNext())
            {
                ptC = ptIterator.next();
                ptR = regions.getRegionCoordinate(ptC.x, ptC.y);
                contours[ptR.x][ptR.y].external.add(ptC);
            }
        }

        /* make sure all the Contours have the points ordered such that the list traces the shape */
        List<Point2D_I32> sorted, unsorted;
        for (int i = 0; i < widthInRegions; i++)
        {
            for (int j = 0; j < heightInRegions; j++)
            {
                unsorted = contours[i][j].external;
                sorted = new ArrayList<Point2D_I32>(unsorted.size());

                if (unsorted.size() == 0)
                {
                    continue;
                }

                ptC = unsorted.remove(0);
                sorted.add(ptC);
                while (unsorted.size() > 0)
                {
                    ptC = removeClosest(ptC, unsorted);
                    sorted.add(ptC);
                }

                contours[i][j].external = sorted;
            }
        }

        /* set the shapes for all the regions */
        for (int i = 0; i < widthInRegions; i++)
        {
            for (int j = 0; j < heightInRegions; j++)
            {
                regions.regions[i][j].setShape(contours[i][j]);
            }
        }
    }

    private static Point2D_I32 removeClosest (Point2D_I32 pt, List<Point2D_I32> points)
    {
        if (points.size() == 1)
        {
            return points.remove(0);
        }

        double leastDist = Double.POSITIVE_INFINITY, dist = 0;
        int maxI = 0, i = 0;
        Iterator<Point2D_I32> ptIterator = points.iterator();
        Point2D_I32 cand;
        while (ptIterator.hasNext())
        {
            cand = ptIterator.next();
            dist = pt.distance(cand);
            if (dist < leastDist)
            {
                leastDist = dist;
                maxI = i;
            }

            ++i;
        }

        return points.remove(maxI);
    }

}
