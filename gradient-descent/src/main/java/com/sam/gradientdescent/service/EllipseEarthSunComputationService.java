package com.sam.gradientdescent.service;

import com.sam.gradientdescent.service.function.derivative.Derivative2DFunction;
import com.sam.gradientdescent.service.function.derivative.Derivative3DFunction;

public class EllipseEarthSunComputationService {

    public static final double G = 6.67430*Math.pow(10,-11);
    public static final double AU = 1.5 * Math.pow(10, 11);
    public static final double EARTH_SUN_ECCENTRICTY = 0.0167;
    public static final double MASS_OF_SUN = 2 * Math.pow(10, 30);
    public static final  double SEMI_MAJOR_AXIS = 1.0000010178;
    public static final double SEMI_MAJOR_AXIS_IN_METER = EllipseEarthSunComputationService.AUtoMeter(SEMI_MAJOR_AXIS);

    public double getRadiusFromFocusInKm(double semiMajorAxisInMeter, double eccentricity, double angleInDegree){
        return  (semiMajorAxisInMeter * (1 - Math.pow(eccentricity, 2))) /
                (1 + (eccentricity * Math.cos(getRadianFromDegree(angleInDegree))));
    }

    private static double getRadianFromDegree(double angleInDegree) {
        return angleInDegree * (Math.PI / 180);
    }

    public double getOrbitalSpeed(double massObjectInKg,double radiusInMeter, double semiMajorAxisInMeter){
        return Math.sqrt(G * massObjectInKg * ((2 / radiusInMeter) - (1 / semiMajorAxisInMeter)));
    }

    public Derivative2DFunction getEarthOrbitalSpeed2D() {
        return (trueAnomalyDegree) ->{
            double semiMajorAxisInMeter = EllipseEarthSunComputationService.AUtoMeter(SEMI_MAJOR_AXIS);
            double radiusFromFocusInKm = this.getRadiusFromFocusInKm(SEMI_MAJOR_AXIS_IN_METER,
                    EARTH_SUN_ECCENTRICTY,trueAnomalyDegree);
            return this.getOrbitalSpeed(MASS_OF_SUN,
                    radiusFromFocusInKm,
                    semiMajorAxisInMeter);
        };
    }

    public Derivative3DFunction getEarthOrbitalSpeed3D() {
        return (x,y) ->{
            double radiusFromFocusInKm = this.getRadiusFromFocusInKm(SEMI_MAJOR_AXIS_IN_METER,
                    EARTH_SUN_ECCENTRICTY,x);
            return this.getOrbitalSpeed(MASS_OF_SUN,
                    radiusFromFocusInKm,
                    SEMI_MAJOR_AXIS_IN_METER);

        };
    }

    public static double kmToMeter(double km){
        return km * 1000;
    }

    public static double meterToKm(double m){
        return m/1000;
    }

    public static double AUtoMeter(double aus){
        return AU*aus;
    }

}
