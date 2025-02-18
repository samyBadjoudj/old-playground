package com.sam.imagesearch.service.imageprocessing;

import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Samy Badjoudj
 */
public class SobelFilterImpl implements SobelFilter {


    private int[][] convertTo2DUsingGetRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[width][height];

        for (int col = 0; col < width; col++) {
            for (int row = 0; row < width; row++) {
                result[col][row] = image.getRGB(col, row)& 0xFF;
            }
        }

        return result;
    }

    @Override
    public ImmutablePair<int[][],int[][]> getGradient(BufferedImage image) {

        BufferedImage imgGrayscale = getGrayScaleBufferedImage(image);

        int [][] img = convertTo2DUsingGetRGB(imgGrayscale);
        int nrows = image.getHeight();
        int ncols = image.getWidth();

        int[][] xDerivative = new int[ncols][nrows];
        int[][] yDerivative = new int[ncols][nrows];

       /* SEE SOBEL FILTER
        BufferedImage imgGx = new BufferedImage(ncols,nrows, BufferedImage.TYPE_BYTE_GRAY) ;
        BufferedImage imgGy = new BufferedImage(ncols,nrows, BufferedImage.TYPE_BYTE_GRAY) ;
        BufferedImage imgSobel = new BufferedImage(ncols,nrows, BufferedImage.TYPE_BYTE_GRAY);
        */

        for (int i=0; i<ncols; i++) {
            for (int j=0; j<nrows; j++) {
                if (i==0 || i==ncols-1 || j==0 || j==nrows-1)
                    xDerivative[i][j] = yDerivative[i][j] = 0;
                else{



                    xDerivative[i][j] =  img[i+1][j-1] + 2*img[i+1][j] + img[i+1][j+1]
                                        -img[i-1][j-1] - 2*img[i-1][j] - img[i-1][j+1];

                    yDerivative[i][j] =  img[i-1][j-1] + 2*img[i][j-1] + img[i+1][j-1]
                                        -img[i-1][j+1] - 2*img[i][j+1] - img[i+1][j+1];

                /*  SEE SOBEL FILTER
                    imgGy.setRGB(i, j, yDerivative[i][j]);
                    imgGx.setRGB(i, j, xDerivative[i][j]);
                    imgSobel.setRGB(i, j, (int) Math.sqrt(Math.pow(new Integer(xDerivative[i][j]).doubleValue(), 2.0) * Math.pow(new Integer(yDerivative[i][j]).doubleValue(), 2.0)));
                 */

                }
            }
        }

     /* SEE SOBEL FILTER
        final String s = UUID.randomUUID().toString();
        File outputFileGx = new File("/static/images/thumbs/imageX"+ s +".png");
        File outputFileGy = new File("/static/images/thumbs/imageY"+ s +".png");
        File outputFileGray = new File("/static/images/thumbs/imagegry"+ s +".png");
        File outputFileSobel = new File("/static/images/thumbs/imagesobel"+ s +".png");
        try {
            ImageIO.write(imgGx, "PNG", outputFileGx);
            ImageIO.write(imgGy, "PNG", outputFileGy);
            ImageIO.write(imgGrayscale, "PNG", outputFileGray);
            ImageIO.write(imgSobel, "PNG", outputFileSobel);
        } catch (IOException e) {

        }
    */

        return new ImmutablePair<>(xDerivative,yDerivative);
    }

    private BufferedImage getGrayScaleBufferedImage(BufferedImage image) {
        BufferedImage imgGrayscale = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);

        Graphics g = imgGrayscale.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return imgGrayscale;
    }


    @Override
    public double[] getDirection(int[][] xDerivative, int[][] yDerivative, int nbRows, int nbColumns) {
        double[] direction = new double[nbColumns*nbRows];

            for(int columns=0; columns<nbColumns;columns++) {
                for(int rows=0; rows<nbRows;rows++) {
                    direction[(rows*nbColumns) +columns] = Math.atan2(yDerivative[columns][rows],xDerivative[columns][rows]);
            }
        }

        return direction;
    }
}
