/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.properties.SProperties;

/**
 *
 * @author edwardblurock
 */
public class DetermineInternalSymmetry extends DetermineTotalSymmetry {
	String referenceS = "Internal Symmetry Correction";
	SetOfBensonThermodynamicBase setOfCorrections;
	double gasConstant;
    public DetermineInternalSymmetry(DetermineSymmetryFromSingleDefinition determine,
                                        SetOfSymmetryDefinitions definitions) {
        super(determine,definitions);
        determineSymmetry = determine;
        String gasconstantS = SProperties.getProperty("thermo.data.gasconstant.clasmolsk");
        gasConstant = Double.valueOf(gasconstantS).doubleValue();

    }
    public void initializeSymmetry() {
        symmetryValue = 1;
    }
    
    public SetOfBensonThermodynamicBase getSetOfCorrections() {
        return setOfCorrections;
    }

    public void setSetOfCorrections(SetOfBensonThermodynamicBase set) {
        this.setOfCorrections = set;
    }

    
    public void combineInSymmetryNumber(int symmetry) {
        symmetryValue *= symmetry;
    	if(symmetry > 1.0) {
    		String symname = this.determineSymmetry.symmetryDefinition.getMetaAtomName();
    		String name = symname + " (" + symmetry + ")";
    		double symmD = symmetry;
    		double correction = -gasConstant * Math.log(symmD);
    	    BensonThermodynamicBase benson = new BensonThermodynamicBase(referenceS, null, 0.0, correction);
    	    benson.setReference(name);
    	    if(setOfCorrections != null) {
    	    	setOfCorrections.add(benson);
    	    }
    	}
    }
    public int getSymmetryValue() {
        return symmetryValue;
    }

}
