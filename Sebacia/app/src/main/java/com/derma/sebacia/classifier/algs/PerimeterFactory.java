package main.java.com.derma.sebacia.classifier.algs;


import georegression.struct.point.Point2D_I32;
import main.java.com.derma.sebacia.classifier.structs.PerimeterDescriptor;
import main.java.com.derma.sebacia.classifier.structs.Shape;

import java.util.Iterator;

/**
 * Created by Jessie on 9/30/2015.
 */
public class PerimeterFactory extends DescriptorFactory<Shape, PerimeterDescriptor> {

    public void computeUnit (Shape shape, PerimeterDescriptor perimeter)
    {
        perimeter.value = 0;
        Iterator<Point2D_I32> ptIterator = shape.outline.external.iterator();
        Point2D_I32 pt1, pt2;

        if (!ptIterator.hasNext())
        {
            return;
        }

        /* start with a point */
        pt1 = ptIterator.next();
        /* move around the shape adding distance between points around the shape */
        while (ptIterator.hasNext())
        {
            pt2 = ptIterator.next();
            perimeter.value += Math.sqrt(Math.pow(pt2.x - pt1.x, 2) + Math.pow(pt2.y - pt1.y, 2));
            pt1 = pt2;
        }
    }

}
