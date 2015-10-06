package com.derma.sebacia.classifier.algs;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jessie on 9/30/2015.
 */
public class DescriptorFactory<T,D> {

    protected List<T> input;
    protected List<D> output;

    public void run ()
    {
        T inObj;
        D outObj;
        Iterator<T> inIterator = input.iterator();
        Iterator<D> outIterator = output.iterator();
        while (inIterator.hasNext() && outIterator.hasNext())
        {
            /*compute the moments for the shape*/
            inObj = inIterator.next();
            outObj = outIterator.next();
            computeUnit(inObj, outObj);
        }
    }

    public void computeUnit (T inObj, D outObj)
    {}

    public void set (List<T> input, D original) throws InstantiationException, IllegalAccessException
    {
        this.input = input;
        this.output = new ArrayList<D>(input.size());
        for (int i = 0; i < input.size(); i++)
        {
            /* TODO : make sure this method works */
            output.add((D)original.getClass().newInstance());
        }
    }

    public List<D> getOutput ()
    {
        return output;
    }

}
