import com.samy.maths.derivative.DerivativeCalculator;
import com.samy.maths.derivative.DerivativeCalculatorImpl;
import com.samy.maths.function.FunctionCalculator;
import com.samy.maths.function.FunctionCalculatorImpl;
import com.samy.maths.graphic.FunctionPlotter;
import com.samy.maths.graphic.FunctionPlotterImpl;
import com.samy.maths.newtonraphson.NewtonRaphsonSolverImpl;

public class Launcher {

    public static void main(String[] args) {

        //HERE I'AM USING THE SCRIPT ENGINE TO EVAL THE FUNCTION, SO x HAS TO BE BETWEEN ROUND BRACKETS LIKE THIS (x) IN LOWERCASE

        //FunctionCalculator functionCalculator = new FunctionCalculatorImpl("((x)*(x)*(x)- 2*(x)*(x)-4)",-20,20);
        FunctionCalculator functionCalculator = new FunctionCalculatorImpl("((x)*(x)-4)",-20,20);
        //FunctionCalculator functionCalculator = new FunctionCalculatorImpl("Math.sin(x)",-20,20);
        DerivativeCalculator derivativeCalculator = new DerivativeCalculatorImpl(functionCalculator);
        final NewtonRaphsonSolverImpl newtonRaphsonSolver = new NewtonRaphsonSolverImpl(derivativeCalculator,20,10);
        FunctionPlotter functionPlotter = new FunctionPlotterImpl(newtonRaphsonSolver);
        functionPlotter.plotFunction();
    }
}