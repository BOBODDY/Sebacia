package com.derma.sebacia.classifier.structs;

import boofcv.alg.filter.binary.Contour;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point2D_I32;

import java.util.Iterator;

/**
 * Created by Jessie on 9/29/2015.
 */
public class Shape {

    public static final int N = 10;
    public Contour outline;
    public Point2D_F32 centroid;
    public ShapeSignature[] signatures;

    public Shape (Contour outline)
    {
        this.outline = outline;
        this.centroid = new Point2D_F32(0,0);
        this.signatures = new ShapeSignature[outline.external.size()];
        this.setCentroid();
        this.setSignatures();
    }

    private void setCentroid ()
    {
        float n = 0;
        Iterator<Point2D_I32> ptIterator = outline.external.iterator();
        Point2D_I32 pt;
        while (ptIterator.hasNext())
        {
            pt = ptIterator.next();
            centroid.x += pt.x;
            centroid.y += pt.y;
            n += 1;
        }
        centroid.x /= n;
        centroid.y /= n;
    }

    private void setSignatures ()
    {
        for (int i = 0; i < N; i++)
        {
            signatures[i] = new ShapeSignature(outline.external.get(i), centroid);
        }
    }

}
