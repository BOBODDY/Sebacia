package main.java.com.derma.sebacia.classifier.structs;

import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point2D_I32;

/**
 * Created by Jessie on 9/29/2015.
 */
public class ShapeSignature {

    public double value;

    public ShapeSignature (Point2D_I32 pt, Point2D_F32 c)
    {
        value = Math.sqrt(Math.pow(pt.x - c.x, 2) + Math.pow(pt.y - c.y, 2));
    }

}
