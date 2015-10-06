package com.derma.sebacia.classifier.structs;

import boofcv.abst.transform.fft.DiscreteFourierTransform;
import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.alg.misc.PixelMath;
import boofcv.alg.segmentation.slic.SegmentSlic;
import boofcv.alg.transform.fft.DiscreteFourierTransformOps;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.InterleavedF32;

/**
 * Created by Jessie on 10/1/2015.
 */
public class FourierSpectrumImage {

    public ImageFloat32 magnitude;
    public ImageFloat32 phase;
    public int blurs;
    public int enhancements;
    public int width, height;

    public FourierSpectrumImage () {}

    public FourierSpectrumImage (FourierSpectrumImage spectrum)
    {
        magnitude = spectrum.magnitude.clone();
        phase = spectrum.phase.clone();
        blurs = spectrum.blurs;
        enhancements = spectrum.blurs;
        width = spectrum.width;
        height = spectrum.height;
    }

    public FourierSpectrumImage (ImageFloat32 image)
    {
        InterleavedF32 twoband = new InterleavedF32(image.width, image.height, 2);
        DiscreteFourierTransform<ImageFloat32, InterleavedF32> dft = DiscreteFourierTransformOps.createTransformF32();
        PixelMath.divide(image, 255.0f, image);
        dft.forward(image, twoband);

        magnitude = new ImageFloat32(twoband.width, twoband.height);
        phase = new ImageFloat32(twoband.width, twoband.height);

        DiscreteFourierTransformOps.shiftZeroFrequency(twoband, true);
        DiscreteFourierTransformOps.magnitude(twoband, magnitude);
        DiscreteFourierTransformOps.phase(twoband, phase);

        blurs = 0;
        enhancements = 0;
        width = twoband.width;
        height = twoband.height;
    }

    public static FourierSpectrumImage computeSmoothedCopy (FourierSpectrumImage spectrum)
    {
        FourierSpectrumImage smoothed = new FourierSpectrumImage(spectrum);
        BlurImageOps.gaussian(smoothed.magnitude, smoothed.magnitude, -1, 5, null);
        /* TODO: find out if blurring the phase is useful, since the phase isn't generally used */
        BlurImageOps.gaussian(smoothed.phase, smoothed.phase, -1, 5, null);

        smoothed.blurs++;

        return smoothed;
    }

    public static FourierSpectrumImage computeEnhancedCopy (FourierSpectrumImage spectrum)
    {
        FourierSpectrumImage enhanced = new FourierSpectrumImage();

        InterleavedF32 twoband = new InterleavedF32(spectrum.width, spectrum.height, 2);
        DiscreteFourierTransform<ImageFloat32, InterleavedF32> dft = DiscreteFourierTransformOps.createTransformF32();
        /* TODO : figure a better way to normalize */
        PixelMath.divide(spectrum.magnitude, 255.0f, spectrum.magnitude);
        dft.forward(spectrum.magnitude, twoband);

        enhanced.magnitude = new ImageFloat32(twoband.width, twoband.height);
        enhanced.phase = new ImageFloat32(twoband.width, twoband.height);

        DiscreteFourierTransformOps.shiftZeroFrequency(twoband, true);
        DiscreteFourierTransformOps.magnitude(twoband, enhanced.magnitude);
        DiscreteFourierTransformOps.phase(twoband, enhanced.phase);

        enhanced.blurs = spectrum.blurs;
        enhanced.enhancements = spectrum.enhancements + 1;
        enhanced.width = twoband.width;
        enhanced.height = twoband.height;

        return enhanced;

        //return new FourierSpectrumImage(spectrum.magnitude);
    }

    public boolean coordinatesInImage(int x, int y)
    {
        return (x >= 0 && x < width) && (y >= 0 && y < height);
    }

}
