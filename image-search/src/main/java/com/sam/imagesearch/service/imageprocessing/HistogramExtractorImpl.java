package com.sam.imagesearch.service.imageprocessing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.HashMap;

/**
 * Created by Samy Badjoudj
 */
public class HistogramExtractorImpl implements HistogramExtractor {

    @Override
    public HashMap<Integer, double[]> getRGBHistogram(BufferedImage image) {

        double[] redHistogram = new double[256];
        double[] greenHistogram = new double[256];
        double[] blueHistogram = new double[256];
        Raster raster = image.getRaster();
        for(int x=0; x<image.getWidth(); x++) {
            for(int y=0; y<image.getHeight(); y++) {
                redHistogram[ raster.getSample(x,y, 0) ] ++;
                greenHistogram[ raster.getSample(x,y, 1) ] ++;
                blueHistogram[ raster.getSample(x,y, 2) ] ++;
            }
        }

        HashMap<Integer, double[]> histograms = new HashMap<>();
        histograms.put(HistogramExtractor.RED,redHistogram);
        histograms.put(HistogramExtractor.GREEN,greenHistogram);
        histograms.put(HistogramExtractor.BLUE, blueHistogram);

        return histograms;

    }
}
