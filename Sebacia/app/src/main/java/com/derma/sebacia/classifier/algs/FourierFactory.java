package main.java.com.derma.sebacia.classifier.algs;


import main.java.com.derma.sebacia.classifier.structs.FourierDescriptor;
import main.java.com.derma.sebacia.classifier.structs.Shape;

/**
 * Created by Jessie on 9/29/2015.
 */
public class FourierFactory extends DescriptorFactory<Shape,FourierDescriptor> {

    public void computeUnit(Shape shape, FourierDescriptor fourier)
    {
        /* for each coefficient */
        for (int c = 0; c < fourier.N; ++c)
        {
            fourier.u[c].real = 0;
            fourier.u[c].img = 0;

            /* calculate the coefficient */
            for (int i = 0; i < fourier.N; ++i)
            {
                double x = 2 * Math.PI * c * i / fourier.N;
                fourier.u[c].real += shape.signatures[i].value * Math.cos(x);
                fourier.u[c].img -= shape.signatures[i].value * Math.sin(x);
            }
            fourier.u[c].real /= fourier.N;
            fourier.u[c].img /= fourier.N;

            /* compute and use the magnitude for rotation invariance
             * the descriptor can also be made scale invariant, but size is important for this application */
            fourier.magnitudes[c] = Math.sqrt(Math.pow(fourier.u[c].real, 2) + Math.pow(fourier.u[c].img, 2));

            /*
              phase may be useful as input since rotation invariance isn't really needed
            if (fourier.u[c].real > 0 && fourier.u[c].img > 0)
            {
                fourier.phases[c] = Math.atan2(fourier.u[c].img, fourier.u[c].real);
            }
            else if (fourier.u[c].real < 0 && fourier.u[c].img > 0)
            {
                fourier.phases[c] = Math.atan2(fourier.u[c].img, fourier.u[c].real) + Math.PI;
            }
            else if (fourier.u[c].real < 0 && fourier.u[c].img < 0)
            {
                fourier.phases[c] = Math.atan2(fourier.u[c].img, fourier.u[c].real) - Math.PI;
            }
            else if (fourier.u[c].real == 0 && fourier.u[c].img] > 0)
            {
                fourier.phases[c] = Math.PI / 2;
            }
            else if (fourier.u[c].real == 0 && fourier.u[c].img < 0)
            {
                fourier.phases[c] = -Math.PI / 2;
            }
            else
            {
                // x = 0, y = 0 => indeterminate phase
                fourier.phases[c] = -1;
            }
            */
        }
    }

}
