package com.samy.maths.function;

/**
 * User: Samy Badjoudj
 * Date: 21/06/14
 */
public interface FunctionCalculator {

    public double getValueAt(double x);
    public String getFunction();
    public Integer getDomainX1();
    public Integer getDomainX2();
    public double[] getY();
    public double[] getX();
    public int getNumberOfValues();
    public double getMax();
    public double getMin();
}
