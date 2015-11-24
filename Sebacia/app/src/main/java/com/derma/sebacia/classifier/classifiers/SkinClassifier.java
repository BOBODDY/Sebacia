package main.java.com.derma.sebacia.classifier.classifiers;

import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.image.MultiSpectral;

/**
 * Created by Jessie on 10/26/2015.
 */
public class SkinClassifier {
    /* TODO: MAKE SURE EVERYTHING IS DEALNG WITH IMAGES THAT ARE ON 0-1 SCALE TO AVOID CONFUSION */

    private static SkinProbabiliyTensor decisionTensor = new SkinProbabiliyTensor();
    private final static float costRatio = 0.8f;
    private final static float skinThresh = 2.0f;

    public static void classify(MultiSpectral<ImageFloat32> image, ImageUInt8 skinMask)
    {
        /* classify each pixel by seeing how far away the value is from the distribution representing,
           each classification */
        float cb, cr, odds;
        for(int i = 0; i < image.width; i++)
        {
            for(int j = 0; j < image.height; j++)
            {
                cb = image.getBand(1).get(i,j);
                cr = image.getBand(2).get(i,j);
                odds = decisionTensor.getOdds(decisionTensor.getIndex(cb), decisionTensor.getIndex(cr));
                if(costRatio*odds > skinThresh)
                {
                    //System.out.println("this pixel is skin");
                    skinMask.set(i,j,1);
                }
                else
                {
                    skinMask.set(i,j,0);
                }
                //System.out.println(odds);

                /*System.out.print(cb); System.out.print(","); System.out.print(cr);
                System.out.print(","); System.out.print(decisionTensor.getIndex(cb));
                System.out.print(","); System.out.print(decisionTensor.getIndex(cr));
                System.out.print(","); System.out.print(odds);
                System.out.println();*/
            }
        }
    }
}
