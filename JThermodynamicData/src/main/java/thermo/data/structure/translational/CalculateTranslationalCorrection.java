package thermo.data.structure.translational;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.structure.utilities.MoleculeUtilities;
import thermo.properties.ChemicalConstants;

public class CalculateTranslationalCorrection {
	static double HydrogenMolecularWeight = 1.00784;
	
	
	public static Double calculate(IAtomContainer mol) {
		
		double rconstant = ChemicalConstants.getGasConstantInCalsMolesK();
		double RHmw = AtomContainerManipulator.getMolecularWeight(mol);
		double Rmw = RHmw - HydrogenMolecularWeight;
		double trans = 3.0 * rconstant * Math.log(Rmw/RHmw);
		
		Double transD = new Double(trans);
		
		return transD;
	}
	
	public static BensonThermodynamicBase translationalEnergy(IAtomContainer mol) {
		
		Double transEntropyCorrectionD = calculate(mol);
		Double zeroD = new Double(0.0);
		String translationS = "Translational Energy Correction";
		String typeS = "Correction";
        BensonThermodynamicBase thermo = new BensonThermodynamicBase(typeS,
        		null,zeroD,transEntropyCorrectionD);
        thermo.setReference(translationS);

        return thermo;
	}

}
