package com.sam;

import com.sam.gradientdescent.service.function.derivative.Derivative3DFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Derivative3DFunctionTest {

    @Test
    void testDerivativeX(){

        Derivative3DFunction convexFunction = (x, y) -> (x*x) + (y*y*x);
        Derivative3DFunction derivativeConvexFunction = (x, y) -> (2*x) + (y*y);
        Double xDerivative = convexFunction.getXDerivative(5.0, 5.0);
        Assertions.assertTrue(Math.abs(derivativeConvexFunction.apply(5.0,5.0) - xDerivative) < 0.1);

    }


}