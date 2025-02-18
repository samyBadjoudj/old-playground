package com.sam.imagesearch.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samy Badjoudj
 */
public class ImageSearchUtils {

    public static ImmutablePair<Integer,Integer> getScaled(
                  ImmutablePair<Integer,Integer> original,
                  ImmutablePair<Integer,Integer> desirable){

       ImmutablePair<Integer,Integer> imageSize;

        if(original.getLeft()>= original.getRight()){
            imageSize = new ImmutablePair<>(desirable.getLeft(),
                    (original.getRight() * desirable.getLeft())/original.getLeft());

        }else {
            imageSize = new ImmutablePair<>((original.getLeft() * desirable.getRight()) / original.getRight(), desirable.getRight())
            ;
        }
        return imageSize;
    }

    public static double[] getVector(String... vectorValues){

        //CRAP
        double[] finalVector = new double[0];
        for(String  vector : vectorValues){

            final String[] currentVector = StringUtils.split(vector, ",");
            final double[] newVector = new double[currentVector.length];

            for (int i = 0; i < currentVector.length; i++) {
                newVector[i] = Double.parseDouble(currentVector[i]);

            }
            finalVector = ArrayUtils.addAll(finalVector,newVector);

        }
        return finalVector;
    }


    public static double[] concatDimensions(double[]... vectors) {

        double[] finalVector = new double[0];
        for (double[] vector : vectors) {
            finalVector = ArrayUtils.addAll(finalVector,vector);
        }
        return finalVector;
    }
}
