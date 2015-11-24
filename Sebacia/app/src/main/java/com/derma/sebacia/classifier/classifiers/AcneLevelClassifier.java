package main.java.com.derma.sebacia.classifier.classifiers;

import boofcv.alg.feature.detect.edge.CannyEdge;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.factory.feature.detect.edge.FactoryEdgeDetectors;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt16;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.image.MultiSpectral;
import main.java.com.derma.sebacia.classifier.algs.ColorConverter;
import main.java.com.derma.sebacia.classifier.algs.ImageSubdivider;
import main.java.com.derma.sebacia.classifier.structs.RegionImage;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by Jessie on 10/6/2015.
 */
public class AcneLevelClassifier extends Classifier {
    /* TODO: MAKE SURE EVERYTHING IS DEALNG WITH IMAGES THAT ARE ON 0-1 SCALE TO AVOID CONFUSION */


    public static void classify (BufferedImage image, Results results)
    {
        MultiSpectral<ImageFloat32> rgb = ConvertBufferedImage.convertFromMulti(image, null, true, ImageFloat32.class);
        MultiSpectral<ImageFloat32> selfie = ColorConverter.LABFromRGB(rgb);

        /*
        * 1. smooth image
        *    - this isn't necessary if the edge detector does enough of this on its own
        * 2. get edges
        * 3. divide images
        * 4. record which regions are of skin
        * 4. compute descriptors for all the regions for regions of skin
        * 5. see if regions have acne and/or are inflamed
        *     - should there be separate classifiers for acne-classification and affected-skin-classification
        *       or should all that be done here?
        * 6. use results from step 5 to classify acne
        * 7. return results
        * */

         /* 1. smooth image */
        ImageFloat32 smoothed = new ImageFloat32(selfie.width, selfie.height);
        BlurImageOps.gaussian(selfie.getBand(0), smoothed, -1, 2, null);

        /* 2. get edges */
        ImageUInt8 edgeImg = new ImageUInt8(smoothed.width, smoothed.height);
        CannyEdge<ImageFloat32, ImageSInt16> canny = FactoryEdgeDetectors.canny(2, true, true, ImageFloat32.class, ImageSInt16.class);
        canny.process(smoothed, 0.05f, 0.03f, edgeImg);

        /* 3. divide images */
        /* TODO : make sure this method works, if it doesn't then get a List of Contours for the whole image and then
         *        call ImageSubdivider.divideContours() */
        RegionImage regions = ImageSubdivider.divideImage(smoothed);

        /* 4. record which regions are of skin */

        /* 5. compute descriptors for regions of skin */
        ImageSubdivider.divideEdges(regions, edgeImg);
        ImageSubdivider.setSpectrumOfRegions(regions);

    }

}
