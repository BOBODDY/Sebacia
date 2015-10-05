package main.java.com.derma.sebacia.classifier.algs;


import boofcv.struct.image.ImageFloat32;
import main.java.com.derma.sebacia.classifier.structs.EdgeHistogramDescriptor;
import main.java.com.derma.sebacia.classifier.structs.Texture;

/**
 * Created by Jessie on 10/4/2015.
 */
public class EdgeHistogramFactory extends DescriptorFactory<Texture,EdgeHistogramDescriptor> {

    /* IMPORTANT : this implementation uses 2x2 image blocks and changes the size of subimages by adding
     *             blocks, so it does not average quadrants in an image block to get a 2x2 block of average
     *             pixel values */

    private static final int imageBlockLength = 2;
    private static final int imageBlocksPerSubImage = 1;
    private static final float[][] verticalEdgeFilter = {{1,-1},{1,-1}};
    private static final float[][] horizontalEdgeFilter = {{1,1},{-1,-1}};
    private static final float[][] dia45EdgeFilter = {{(float)Math.sqrt(2),0},{0,(float)Math.sqrt(2)}};
    private static final float[][] dia135EdgeFilter = {{0,(float)Math.sqrt(2)},{(float)Math.sqrt(2),0}};
    private static final float[][] randomEdgeFilter = {{2,-2},{-2,2}};
    private static final float[][][] edgeFilters = {verticalEdgeFilter, horizontalEdgeFilter, dia45EdgeFilter, dia135EdgeFilter, randomEdgeFilter};


    public void computeUnit (Texture texture, EdgeHistogramDescriptor ehd)
    {
        /* divide the texture image into subimages */
        ImageFloat32[][][] subimage = new ImageFloat32[4][4][imageBlocksPerSubImage];
        int startX, startY, endX, EndY;
        for (int i = 0; i < 4; i++)
        {
            startX = i * imageBlockLength * imageBlocksPerSubImage;
            endX = startX + imageBlockLength;
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < imageBlocksPerSubImage; k++)
                {
                    startY = j * imageBlockLength * imageBlocksPerSubImage;
                    subimage[i][j][k] = texture.image.subimage(startX, startY, endX, startY + imageBlockLength);
                }
            }
        }

        /* compute the edge strengths and increment the edge histogram using the maximum strength */
        float[] edgeStrengths = new float[5]; /* {vertical, horizontal, dia45, dia135, random} */
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < imageBlocksPerSubImage; k++)
                {
                    computeEdgeStrengths(subimage[i][j][k], edgeStrengths);
                    ehd.incrementBin(i, j, getMaxEdgeStrength(edgeStrengths));
                }
            }
        }
    }

    private void computeEdgeStrengths (ImageFloat32 block, float[] strengths)
    {
        for (int s = 0; s < 5; s++)
        {
            strengths[s] = 0;
            for (int x = 0; x < 2; x++)
            {
                for (int y = 0; y < 2; y++)
                {
                    strengths[s] += edgeFilters[s][x][y] * block.get(x,y);
                }
            }
            strengths[s] = Math.abs(strengths[s]);
        }
    }

    private int getMaxEdgeStrength (float[] strengths)
    {
        int maxK = 0;
        float maxS = 0;
        for (int k = 0; k < 5; k++)
        {
            if (strengths[k] > maxS)
            {
                maxS = strengths[k];
                maxK = k;
            }
        }
        return maxK;
    }

}
