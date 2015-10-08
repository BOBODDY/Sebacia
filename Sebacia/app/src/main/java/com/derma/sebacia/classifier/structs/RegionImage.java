package com.derma.sebacia.classifier.structs;

import boofcv.struct.image.ImageFloat32;
import georegression.struct.point.Point2D_I32;

/**
 * Created by Jessie on 10/6/2015.
 */
public class RegionImage {

    public int width;
    public int height;
    public int dim;
    public ImageRegion[][] regions;

    public RegionImage(int width, int height, int dim)
    {
        this.width = width;
        this.height = height;
        this.dim = dim;
        regions = new ImageRegion[this.width][this.height];
    }

    public Point2D_I32 getRegionCoordinate (int x, int y)
    {
        return new Point2D_I32((int)Math.floor(x/dim), (int)Math.floor(y/dim));
    }

}
