package com.sam.imagesearch.service.computation;

import com.sam.imagesearch.service.computation.SignalComparator;

/**
 * Created by Samy Badjoudj
 */
public class SignalComparatorImpl implements SignalComparator {

    @Override
    public double similarity(double[] vector1, double[] vector2) {

        double numerator = 0 ;
        double firstTermDenominator = 0 ;
        double secondTermDenominator = 0 ;

        for(int i = 0; i<vector1.length;i++){
           numerator              += vector1[i]*vector2[i];
           firstTermDenominator  += Math.pow(vector1[i], 2);
           secondTermDenominator += Math.pow(vector2[i], 2);
        }
        return numerator / (Math.sqrt(firstTermDenominator) * Math.sqrt(secondTermDenominator));
    }

}
