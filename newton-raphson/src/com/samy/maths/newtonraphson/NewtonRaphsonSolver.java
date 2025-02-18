package com.samy.maths.newtonraphson;

import com.samy.maths.derivative.DerivativeCalculator;
import com.samy.maths.function.FunctionCalculator;
import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Samy Badjoudj
 * Date: 21/06/14
 */
public interface NewtonRaphsonSolver {

    public double[] tryToSolveIt();
    public List<FunctionCalculator> getTangeants();
    public Integer getNumberOfIterations() ;
    public DerivativeCalculator getDerviativeCalculator();
    public FunctionCalculator getFunctionCalculator() ;
    public int getStartingPoint();
}
