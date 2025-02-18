package com.sam.gradientdescent.service.function.derivative;

import java.util.function.BiFunction;

@FunctionalInterface
public interface Derivative3DFunction extends BiFunction<Double,Double,Double> {
    Double step = 0.00001;
    Double apply(Double a, Double b);

    default Double getXDerivative(Double x, Double y){
        return (apply(x+step,y)-apply( x,y))/step;
    }

    default Double getYDerivative(Double x, Double y){
        return (apply(x,y+step)-apply(x,y))/step;
    }
    default Double[] getDerivative(Double x, Double y){
        return new Double[]{getXDerivative(x,y),getYDerivative(x,y)};
    }

    default Double[] getDerivative(Double[] vector){
        return getDerivative(vector[0],vector[1]);
    }

    default Double[] scale(Double[] vector, Double scalar){
        return new Double[]{vector[0]*scalar, vector[1]*scalar};
    }


    default Double[] subtract(Double[] vector, Double[] vector2){
        return new Double[]{vector[0]-vector2[0],vector[1]-vector2[1]};
    }
}
