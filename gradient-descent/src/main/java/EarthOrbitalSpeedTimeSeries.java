
import com.sam.gradientdescent.service.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.general.SeriesException;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.util.ArrayList;

import static com.sam.gradientdescent.service.EllipseEarthSunComputationService.*;

public class EarthOrbitalSpeedTimeSeries extends ApplicationFrame {

    private static final int CHART_WIDTH = 560;
    private static final int CHART_HEIGHT = 370;
    private static final int MAX_ITERATIONS = 100;
    private static final double STARTING_POINT = 25.0;
    private static final double LEARNING_RATE = 10.0;
    private static final int DEGREE_RANGE_LOWER = 0;
    private static final int DEGREE_RANGER_HIGHER = 360;
    private static final int SPEED_RANGE_LOWER = 29000;
    private static final int SPEED_RANGE_HIGHER = 31000;
    private static final String CHART_TITLE = "Earth orbital speed around sun";
    private static final String X_AXIS_LABEL = "True Anomaly in Degree";
    private static final String Y_AXIS_LABEL = "Orbital speed v in m/s";
    private static final float STROKE = 2.0f;

    public EarthOrbitalSpeedTimeSeries(final String title) {
        super(title);
        final XYDataset dataset = createOrbitalSpeedDataSet();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(CHART_WIDTH, CHART_HEIGHT));
        chartPanel.setMouseZoomable(true, false);
        setContentPane(chartPanel);
    }

    private XYDataset createOrbitalSpeedDataSet() {
        final XYSeries orbitalSpeeds = new XYSeries("earth_sun_orbital_speed");
        final XYSeries gradientPath = new XYSeries("gradient_path");
        final XYSeries localMinimumTangent = new XYSeries("local_minimum_tan");
        final EllipseEarthSunComputationService computationService = new EllipseEarthSunComputationService();
        final GradientDescentService gradientDescentService = new GradientDescentService(computationService);
        GradientDescent<Double> minByGradientDescent = gradientDescentService.getMinByGradientDescent(MAX_ITERATIONS,
                LEARNING_RATE,
                STARTING_POINT);

        double radiusFromFocusInKm = computationService.getRadiusFromFocusInKm(SEMI_MAJOR_AXIS_IN_METER,
                EARTH_SUN_ECCENTRICTY,
                minByGradientDescent.getLocalMinimum());
        double orbitalSpeedMin = computationService.getOrbitalSpeed(MASS_OF_SUN,
                radiusFromFocusInKm,
                SEMI_MAJOR_AXIS_IN_METER);

        for (int i = 1; i < 361; i++) {
            try {
                if (i < minByGradientDescent.getPath().size()) {
                    Double[] currentPathPoint = minByGradientDescent.getPath().get(i);
                    gradientPath.add(currentPathPoint[0], currentPathPoint[1]);
                }
                radiusFromFocusInKm = computationService.getRadiusFromFocusInKm(SEMI_MAJOR_AXIS_IN_METER,
                        EARTH_SUN_ECCENTRICTY,
                        i);
                double orbitalSpeed = computationService.getOrbitalSpeed(MASS_OF_SUN,
                        radiusFromFocusInKm,
                        SEMI_MAJOR_AXIS_IN_METER);
                orbitalSpeeds.add(i, orbitalSpeed);
                localMinimumTangent.add(i, orbitalSpeedMin);
            } catch (SeriesException e) {
                System.err.println("Error adding to orbitalSpeeds");
            }
        }
        System.out.println(orbitalSpeedMin);
        System.out.println(minByGradientDescent.getLocalMinimum());
        return createXYSeriesCollection(orbitalSpeeds, gradientPath, localMinimumTangent);
    }

    private static XYSeriesCollection createXYSeriesCollection(XYSeries orbitalSpeeds, XYSeries gradientPath, XYSeries locMinPath) {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeriesCollection.addSeries(locMinPath);
        xySeriesCollection.addSeries(gradientPath);
        xySeriesCollection.addSeries(orbitalSpeeds);
        return xySeriesCollection;
    }

    private JFreeChart createChart(final XYDataset dataset) {
        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                CHART_TITLE,
                X_AXIS_LABEL,
                Y_AXIS_LABEL,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false);
        XYPlot plot = (XYPlot) xyLineChart.getPlot();
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(STROKE));
        renderer.setSeriesStroke(1, new BasicStroke(STROKE));
        renderer.setSeriesStroke(2, new BasicStroke(STROKE));
        ((AbstractRenderer) renderer).setAutoPopulateSeriesStroke(false);
        plot.getDomainAxis().setRange(new Range(DEGREE_RANGE_LOWER, DEGREE_RANGER_HIGHER));
        plot.getRangeAxis().setRange(new Range(SPEED_RANGE_LOWER, SPEED_RANGE_HIGHER));
        return xyLineChart;
    }

    public static void main(final String[] args) {
        final String title = "Gradient descent example";
        final EarthOrbitalSpeedTimeSeries demo = new EarthOrbitalSpeedTimeSeries(title);
        demo.pack();
        RefineryUtilities.positionFrameRandomly(demo);
        demo.setVisible(true);
    }
}   