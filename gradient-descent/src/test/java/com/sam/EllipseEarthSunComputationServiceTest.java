package com.sam;

import com.sam.gradientdescent.service.EllipseEarthSunComputationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EllipseEarthSunComputationServiceTest {

    @Test
    void getRadiusFromFocusInKm() {
        EllipseEarthSunComputationService computationService = new EllipseEarthSunComputationService();
        double radiusFromFocusInKm = computationService
                .getRadiusFromFocusInKm(40000, 0.5, 45);
        Assertions.assertTrue(Math.abs(radiusFromFocusInKm-22163.88) < 0.1);
    }

    @Test
    void getOrbitalSpeed() {
        EllipseEarthSunComputationService computationService = new EllipseEarthSunComputationService();
        double radiusInMeter = EllipseEarthSunComputationService.AUtoMeter(0.98329);
        double semiMajorAxisInMeter = EllipseEarthSunComputationService.AUtoMeter(1.0000010178);
        double orbitalSpeed = computationService.getOrbitalSpeed(1.989*Math.pow(10, 30), radiusInMeter, semiMajorAxisInMeter);
        double kmTo = EllipseEarthSunComputationService.meterToKm(orbitalSpeed);
        Assertions.assertTrue(Math.abs(kmTo -30.29) < 0.1);

    }
}