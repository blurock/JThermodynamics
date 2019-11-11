package info.esblurock.thermodynamics.base.nasa;

@SuppressWarnings("serial")
public class NASAPolynomialFromBensonBurcatBase extends NasaPolynomialBase {
    double cpinfinity;
    double cpzero;
    double[] burcatcoeffs;
    double[] temperatures;
    double B;

    public NASAPolynomialFromBensonBurcatBase() {
    	cpinfinity = 0.0;
    	cpzero = 0.0;
    	double[] c = {0.0,0.0,0.0,0.0};
    	burcatcoeffs = c;
    	double[] t = {1000.0,1100.0,1200.0,1500.0};
    	temperatures = t;
    	B = 0.0;
    }
}
