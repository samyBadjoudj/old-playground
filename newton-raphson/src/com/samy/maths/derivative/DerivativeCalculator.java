package com.samy.maths.derivative;

import com.samy.maths.function.FunctionCalculator;

/**
 * User: Samy Badjoudj
 * Date: 21/06/14
 */
public interface DerivativeCalculator {

    public double getDerivativeValue(double atX);
    public FunctionCalculator getFunctionCalculator();
    public double getAccuracy();

}
