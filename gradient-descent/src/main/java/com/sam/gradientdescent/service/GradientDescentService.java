package com.sam.gradientdescent.service;

import com.sam.gradientdescent.service.function.derivative.Derivative2DFunction;
import com.sam.gradientdescent.service.function.derivative.Derivative3DFunction;

import java.util.*;

public class GradientDescentService {

    private final EllipseEarthSunComputationService computationService;

    public GradientDescentService(EllipseEarthSunComputationService computationService) {
        this.computationService = computationService;
    }

    public GradientDescent<Double> getMinByGradientDescent(int maxIteration, Double learningRate, Double start){
        Derivative2DFunction function = computationService.getEarthOrbitalSpeed2D();
        Double min = start;
        List<Double[]> path = new ArrayList<>();
        for (int i = 0; i < maxIteration; i++) {
            double scaledGradient = learningRate * function.getDerivative(start);
            path.add(new Double[]{start,function.apply(start)});
            Double newPoint = start - scaledGradient;
            start = newPoint;
            if (function.apply(newPoint) < function.apply(min)) {
                min = newPoint;
            }
        }
        return new GradientDescent<>(path,min);
    }

    public  Double[] gradientFunction3DDescentMinimum(int maxIteration, Double learningRate, Double[] startingPoint){
        Derivative3DFunction function = computationService.getEarthOrbitalSpeed3D();
        Double[] min = startingPoint;
        for (int i = 0; i < maxIteration; i++) {
            Double[] diff = function.scale(function.getDerivative(startingPoint), learningRate);
            Double[] newPoint = function.subtract(startingPoint, diff);
            startingPoint = newPoint;
            if (function.apply(newPoint[0], newPoint[1]) < function.apply(min[0], min[1])) {
                min = newPoint;
            }
        }
        return min;
    }
}
