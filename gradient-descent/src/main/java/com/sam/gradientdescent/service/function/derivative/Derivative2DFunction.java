package com.sam.gradientdescent.service.function.derivative;

import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface Derivative2DFunction extends Function<Double,Double> {
    Double step = 0.00001;
    Double apply(Double a);

    default Double getDerivative(Double x){
        return (apply(x+step)-apply( x))/step;
    }

}
