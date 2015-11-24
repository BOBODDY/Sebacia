package main.java.com.derma.sebacia.classifier.structs;

import boofcv.struct.image.ImageFloat32;

/**
 * Created by Jessie on 10/2/2015.
 */
public class Texture {

    /* TODO : figure out how to make it so that there are the same number of image blocks for all phone screen types,
     *        the image blocks will be treated as the regions within which the other descriptors are computed */
    /* For the time being the image is treated as the 4x4 structure of sub images in the EHD algorithm */
    public ImageFloat32 image;
    public FourierSpectrumImage spectrum;

    public Texture (ImageFloat32 image)
    {
        this.image = image;
    }

    public void setSpectrum ()
    {
        this.spectrum = new FourierSpectrumImage(this.image);
    }

}
