/*
import com.sam.chart.ChartService;
import com.sam.gradientdescent.service.EllipseEarthSunComputationService;
import com.sam.gradientdescent.service.GradientDescentService;
import com.sam.gradientdescent.service.function.FunctionFactory;
import org.jzy3d.chart.Chart;

public class EarthOrbitalSpeedSurface {

    public static void main(String[] args) {

        EllipseEarthSunComputationService computationService = new EllipseEarthSunComputationService();
        GradientDescentService gradientDescentService = new GradientDescentService(computationService);
        ChartService chartService = new ChartService();
        //f(x,y)=x^(4)+(x+2)^(3)+y^(6)+y^(3)
        Double[] function3DDescentMinimum = gradientDescentService.gradientFunction3DDescentMinimum(721, 10.0, new Double[]{2.0, 2.0});
        //Chart chart = chartService.createChart(computationService.getEarthOrbitalSpeed3D(), function3DDescentMinimum);
        Chart chart = chartService.createChart(FunctionFactory.getFunctionWithLocalMinimum(), function3DDescentMinimum);

        chart.open();
        chart.addMouse();
    }





}*/
