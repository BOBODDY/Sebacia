package main.java.com.derma.sebacia.classifier.structs;

/**
 * Created by Jessie on 9/29/2015.
 */
public class FourierDescriptor extends Descriptor {

    public int N;
    /* the coefficients */
    public ComplexNumber[] u;
    /* magnitude of the coefficients */
    public double[] magnitudes;
    /* uncomment this if you decide to drop rotation invariance
    public double[] phases; */

    public FourierDescriptor ()
    {
        N = Shape.N;
        u = new ComplexNumber[N];
        magnitudes = new double[N];
        /* uncomment this if you decide to drop rotation invariance
        phases = new double[n]; */
    }

}
