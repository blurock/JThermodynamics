package thermo.compute.utilities;

import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.NASAPolynomialFromBenson;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.ThermodynamicInformation;
import thermo.exception.ThermodynamicComputeException;

public class ThermodynamicResultsOutput {
	
	public static void commandLinePrintout(String nancy, String name, IAtomContainer molecule, SetOfBensonThermodynamicBase thermodynamics, ThermodynamicInformation thermo) throws ThermodynamicComputeException {
		System.out.println("Compute From Nancy Linear Form: -->" + nancy + "<--");
		System.out.println("-------------- Corrections --------------");
		System.out.println(thermodynamics.toString());
		System.out.println("-------------- Total --------------");
		System.out.println(thermo.toString());
		NASAPolynomialFromBenson nasa = new NASAPolynomialFromBenson((BensonThermodynamicBase) thermo,
				molecule);
		nasa.name = name;
		System.out.println("-------------- NASA Polynomial --------------");
		System.out.println(nasa.toString());
		
	}


}
