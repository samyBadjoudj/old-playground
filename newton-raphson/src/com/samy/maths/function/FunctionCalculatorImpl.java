package com.samy.maths.function;

import com.samy.maths.evaluator.FunctionEvaluatorEngine;
import javax.script.ScriptException;

/**
 * User: Samy Badjoudj
 * Date: 21/06/14
 */
public class FunctionCalculatorImpl implements FunctionCalculator {

    private final Integer domainX1;
    private final Integer domainX2;
    private final String function;
    private final double x[];



    private final double y[];
    private final int numberOfValues ;
    private final Double AccuracyPoints = 10.0;


    public FunctionCalculatorImpl(String function,Integer domainX1,Integer domainX2) {
        this.function = function;
        this.domainX1 = domainX1;
        this.domainX2 = domainX2;
        this.numberOfValues= (getDomainX2() - getDomainX1()+1)*AccuracyPoints.intValue();
        this.x = getXValues();
        this.y = getYValues();

    }

    private double[] getYValues() {
        double[] initY = new double[numberOfValues];
        for(int i = 0; i<x.length;i++){
            initY[i] = getValueAt(x[i]);
        }
        return initY;
    }
    private double[] getXValues() {
        double[] initX = new double[numberOfValues];
        for(int i = 0; i<this.numberOfValues;i++){
            initX[i] = this.getDomainX1() + i/ AccuracyPoints;
        }
        return initX;
    }

    @Override
    public double getValueAt(double x) {
        try {
            return (Double) FunctionEvaluatorEngine.get().eval(function.replace("x", Double.toString(x)));
        } catch (ScriptException e) {
            System.out.println(e);
        }

        return Double.NaN;
    }




    public Integer getDomainX2() {
        return domainX2;
    }

    public Integer getDomainX1() {
        return domainX1;
    }

    public double[] getY() {
        return y;
    }

    public double[] getX() {
        return x;
    }

    public int getNumberOfValues() {
        return numberOfValues;
    }

    @Override
    public double getMax() {
        double max = -100000000000.0;
        for (double aY : y) {
            max = aY >max ? aY : max;
        }
        return max;
    }
    public double getMin() {
        double min = Double.MAX_VALUE;
        for (double aY : y) {
            min = aY < min ? aY : min;
        }
        return min;
    }

    public String getFunction() {
        return function;
    }

}
