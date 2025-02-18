package com.samy.maths.newtonraphson;

import com.samy.maths.derivative.DerivativeCalculator;
import com.samy.maths.function.FunctionCalculator;
import com.samy.maths.function.FunctionCalculatorImpl;

import java.util.*;

/**
 * User: Samy Badjoudj
 * Date: 21/06/14
 */
public class NewtonRaphsonSolverImpl implements NewtonRaphsonSolver {



    private final Integer numberOfIterations;
    private final DerivativeCalculator derviativeCalculator;
    private final FunctionCalculator functionCalculator;
    private List<FunctionCalculator> tangents = new ArrayList<FunctionCalculator>();

    public List<FunctionCalculator>  getTangeants() {
        return tangents;
    }

    private final int startingPoint;

    public NewtonRaphsonSolverImpl(DerivativeCalculator derivativeCalculator, Integer numberOfIterations,Integer offsetStart) {
        this.derviativeCalculator = derivativeCalculator;
        this.numberOfIterations = numberOfIterations;
        this.functionCalculator = derivativeCalculator.getFunctionCalculator();
        this.startingPoint =( (functionCalculator.getDomainX1() + functionCalculator.getDomainX2()) / 2)+offsetStart;

    }

    @Override
    public double[] tryToSolveIt() {
        double currentX = startingPoint;
        addTangeant(currentX);
        for (int i = 0; i < numberOfIterations; i++) {
            currentX = currentX - functionCalculator.getValueAt(currentX) / derviativeCalculator.getDerivativeValue(currentX);
            addTangeant(currentX);
            if (Math.abs(functionCalculator.getValueAt(currentX)) < derviativeCalculator.getAccuracy()) {
                return new double[]{i+1,currentX};
            }
        }
        return new double[]{numberOfIterations,Double.NaN};
    }

    private void addTangeant(double currentX) {
        String patternDerivative = functionCalculator.getValueAt(currentX)
                + "+" + derviativeCalculator.getDerivativeValue(currentX) + "*((x)-" + currentX +")";

        tangents.add(
                new FunctionCalculatorImpl(patternDerivative
                        , functionCalculator.getDomainX1()
                        , functionCalculator.getDomainX2()));
    }

    public Integer getNumberOfIterations() {
        return numberOfIterations;
    }

    public DerivativeCalculator getDerviativeCalculator() {
        return derviativeCalculator;
    }

    public FunctionCalculator getFunctionCalculator() {
        return functionCalculator;
    }

    public int getStartingPoint() {
        return startingPoint;
    }

}
