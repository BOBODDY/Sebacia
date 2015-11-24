package main.java.com.derma.sebacia.classifier.structs;

import boofcv.alg.filter.binary.Contour;
import boofcv.struct.image.ImageFloat32;
import georegression.struct.point.Point2D_I32;

/**
 * Created by Jessie on 10/6/2015.
 */
public class ImageRegion {

    /* the coordinate of the left corner of this region in the image */
    public Point2D_I32 anchor;
    public Shape shape;
    public Texture texture;
    public boolean isSkin;

    public ImageRegion (ImageFloat32 image, int x, int y)
    {
        anchor = new Point2D_I32(x,y);
        texture = new Texture(image);
        isSkin = true;
    }

    public void setShape (Contour edge)
    {
        shape = new Shape(edge);
    }

}
