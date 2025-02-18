package com.sam.imagesearch.service.imageprocessing;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.awt.image.BufferedImage;

/**
 * Created by Samy Badjoudj
 */
public interface SobelFilter {
    //left is xDerivative, right is vertical ones
    public ImmutablePair<int[][],int[][]> getGradient(BufferedImage image);
    public double[] getDirection(int[][] xDerivative, int[][] yDerivative, int nbRows, int nbColumns);
}
