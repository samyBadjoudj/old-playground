/*
package com.sam.chart;

import com.sam.gradientdescent.service.EllipseEarthSunComputationService;
import com.sam.gradientdescent.service.GradientDescentService;
import com.sam.gradientdescent.service.function.derivative.Derivative3DFunction;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartFactory;
import org.jzy3d.chart.factories.IChartFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.painters.Font;
import org.jzy3d.plot3d.builder.Func3D;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.SurfaceBuilder;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.text.drawable.DrawableText;

public class ChartService {

    //computationService.getEarthOrbitalSpeed3D().apply(x, y)
    //gradientDescentService.gradientFunction3DDescentMinimum(721, 10.0, new Double[]{2.0, 2.0})
    public Chart createChart(Derivative3DFunction earthOrbitalSpeed3D, final Double[] dDescentMinimum){
        Range range = new Range(-720, 720);
        int steps = 50;
        Mapper func = new Func3D(earthOrbitalSpeed3D);
        final Shape surface = new SurfaceBuilder().orthonormal(new OrthonormalGrid(range, steps), func);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface, new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.BLACK);
        final Shape surface2 = new SurfaceBuilder().orthonormal(new OrthonormalGrid(range, steps), new Mapper() {
            @Override
            public double f(double v, double v1) {
                Double min = dDescentMinimum[0];
                Double miny = dDescentMinimum[1];
                return func.f(min, miny);
            }
        });
        surface2.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface, new Color(0, 255, 0, 0.f)));
        surface2.setFaceDisplayed(true);
        surface2.setWireframeDisplayed(true);
        surface2.setWireframeColor(Color.GREEN);

        // Create a chart
        IChartFactory f = new AWTChartFactory();
        Chart chart = f.newChart(Quality.Advanced().setHiDPIEnabled(true));
        chart.getScene().getGraph().add(surface);
        chart.getScene().getGraph().add(surface2);
        chart.getScene().add(new DrawableText("Minimum",new Coord3d(10.0,10.0,0.0),Color.BLUE));
        chart.getAxisLayout().setXAxisLabel("True anomaly in degrees");
        chart.getAxisLayout().setZAxisLabel("Orbital speed in m/s");
        chart.getAxisLayout().setAxisLabelDistance(4);
        chart.getAxisLayout().setFont(new Font("Courrier",20));
        return chart;
    }
}
*/
