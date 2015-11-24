package main.java.com.derma.sebacia.classifier.classifiers;

import java.io.*;
import java.net.URL;

/**
 * Created by Jessie on 11/17/2015.
 */
public class SkinProbabiliyTensor {

    private float disc;
    private int dim;
    private float[][] tensor;

    public SkinProbabiliyTensor() {
        BufferedReader reader = null;
        try
        {
            /* TODO: adjust file reading for Android */
            String directoryName = "C:\\Users\\Jessie\\Desktop\\GaTech\\Fall2015\\CS4911\\Project\\Sebacia_Classifier\\app\\src\\main\\res\\data\\";
            reader = new BufferedReader(new FileReader(directoryName + "DecisionTensor_499_CbCr.txt"));
            String line;
            disc = Float.parseFloat(reader.readLine());
            dim = Integer.parseInt(reader.readLine());
            tensor = new float[dim][dim];
            int count = 0;
            while ((line = reader.readLine()) != null)
            {
                String[] vals = line.split(",");
                for (int i = 0; i < vals.length; i++)
                {
                    tensor[count][i] = Float.parseFloat(vals[i]);
                    /*System.out.print(count); System.out.print(","); System.out.print(i);
                    System.out.print(","); System.out.print(tensor[count][i]);
                    System.out.print(",");*/
                }
                /*System.out.println(vals.length);
                System.out.println(line);*/
                ++count;
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getDimension() { return dim; }
    public float getOdds(int i, int j)
    {
        //System.out.println(tensor[i][j]);
        return tensor[i][j] / (1.0f-tensor[i][j]);
    }
    public int getIndex(float i) { return (int)(i * disc + disc); }

}
