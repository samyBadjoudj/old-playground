package com.samy.maths.derivative;

import com.samy.maths.function.FunctionCalculator;

/**
 * User: Samy Badjoudj
 * Date: 21/06/14
 */
public class DerivativeCalculatorImpl implements DerivativeCalculator {


    public static final double H_VALUE =  1.0e-12;
    private final FunctionCalculator functionCalculator;

    public DerivativeCalculatorImpl(FunctionCalculator functionCalculator) {
        this.functionCalculator = functionCalculator;

    }

    @Override
    public double getDerivativeValue(double atX) {
        double x1 = atX - H_VALUE;
        double x2 = atX + H_VALUE;
        return  (functionCalculator.getValueAt(x2) - functionCalculator.getValueAt(x1)) / (x2 - x1);
    }

    @Override
    public FunctionCalculator getFunctionCalculator() {
        return functionCalculator;
    }

    @Override
    public double getAccuracy() {
        return H_VALUE;
    }


}
