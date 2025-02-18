package com.samy.maths.graphic;

import com.samy.maths.function.FunctionCalculator;
import com.samy.maths.newtonraphson.NewtonRaphsonSolver;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.ui.InteractivePanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FunctionPlotterImpl extends JFrame implements FunctionPlotter {

    private final NewtonRaphsonSolver newtonRaphsonSolver;
    private final double[] resultRaphson;
    private final int WAITING_TIME_BETWEEN_DERIVATIVES = 2000;
    private static final String NUMBER_ITERATIONS_TEXT_LABEL = "Number of iterations => ";
    private static final String SOLUTION_TEXT_LABEL = "                 One of the app. solution(s) f(x) = 0 is =>";
    private FunctionCalculator functionCalculator;


    public FunctionPlotterImpl(NewtonRaphsonSolver newtonRaphsonSolver) {
        this.newtonRaphsonSolver = newtonRaphsonSolver;
        this.resultRaphson = newtonRaphsonSolver.tryToSolveIt();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.functionCalculator = newtonRaphsonSolver.getDerviativeCalculator().getFunctionCalculator();


    }

    public void plotFunction() {


        DataTable data = new DataTable(Double.class, Double.class);
        final double[] x = functionCalculator.getX();
        final double[] y = functionCalculator.getY();
        for (int i = 0; i < functionCalculator.getNumberOfValues(); i++) {
            data.add(x[i], y[i]);
        }

        XYPlot plot = new XYPlot(data);
        LineRenderer lineRenderer = new DefaultLineRenderer2D();
        final InteractivePanel comp = new InteractivePanel(plot);
        comp.add(getLabelIterations());
        comp.add(getLabelSolutions());
        getContentPane().add(comp);
        plot.setLineRenderer(data, lineRenderer);
        plot.getLineRenderer(data).setColor(Color.RED);
        plot.getPointRenderer(data).setShape(new Rectangle());
        setVisible(true);
        addTangents(plot);
    }

    private JLabel getLabelSolutions() {
        final JLabel jLabel = new JLabel(SOLUTION_TEXT_LABEL + resultRaphson[1]);
        jLabel.setForeground(Color.GREEN);
        return jLabel;
    }

    private JLabel getLabelIterations() {
        final JLabel jLabel = new JLabel(NUMBER_ITERATIONS_TEXT_LABEL + resultRaphson[0]);
        jLabel.setForeground(Color.BLUE);
        return jLabel;
    }

    public void addTangents(final XYPlot plot) {

        ExecutorService executor = Executors.newFixedThreadPool(1);
        final List<FunctionCalculator> tangents = newtonRaphsonSolver.getTangeants();

        for (int currentTangent = 0; currentTangent < tangents.size(); currentTangent++) {
            final FunctionCalculator tangeant = tangents.get(currentTangent);
            final double[] xT = tangeant.getX();
            final double[] yT = tangeant.getY();
            final DataTable derivative = new DataTable(Double.class, Double.class);
            final SwingWorker<Void, Void> swingWorker = getSwingWorker(plot, tangents, tangeant, xT, yT, derivative, currentTangent);
            executor.execute(swingWorker);
        }


    }

    private SwingWorker<Void, Void> getSwingWorker(final XYPlot plot, final List<FunctionCalculator> tangeants, final FunctionCalculator tangent, final double[] xT, final double[] yT, final DataTable derivative, final int finalCurrentTangeant) {
        final SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(WAITING_TIME_BETWEEN_DERIVATIVES);
                return null;
            }
            @Override
            protected void done() {
                for (int i = 0; i < tangent.getNumberOfValues(); i++) {
                    derivative.add(xT[i], yT[i]);
                }
                plot.add(derivative);
                plot.setLineRenderer(derivative, new DefaultLineRenderer2D());
                plot.getPointRenderer(derivative).setShape(new Rectangle());
                if (finalCurrentTangeant == tangeants.size() - 1) {
                    plot.getLineRenderer(derivative).setColor(Color.GREEN);

                }

            }
        };
        final JFrame currentFrame = this;
        swingWorker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("state") && SwingWorker.StateValue.DONE.equals(evt.getNewValue()))
                    currentFrame.repaint();
            }
        });
        return swingWorker;
    }


}