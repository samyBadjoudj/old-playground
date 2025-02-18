package com.sam.imagesearch.service.imageprocessing;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by Samy Badjoudj
 */
public interface HistogramExtractor {

    public Integer RED =0;
    public Integer GREEN =1;
    public Integer BLUE =2;
    public HashMap<Integer,double[]> getRGBHistogram(BufferedImage image);
}
