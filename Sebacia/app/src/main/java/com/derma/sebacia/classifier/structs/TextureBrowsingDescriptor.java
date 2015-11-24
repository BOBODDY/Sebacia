package main.java.com.derma.sebacia.classifier.structs;

/**
 * Created by Jessie on 10/2/2015.
 */
public class TextureBrowsingDescriptor extends Descriptor {
    /*
    * pbc[0] = regularity
    * pbc[1] = dominant direction 1
    * pbc[2] = scale along dominant direction 1
    * pbc[3] = dominant direction 2
    * pbc[4] = scale along dominant direction 2
    * */
    public int[] pbc;

    public TextureBrowsingDescriptor ()
    {
        pbc = new int[5];
    }

}