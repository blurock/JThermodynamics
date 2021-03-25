/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.renderer.RendererModel.ExternalHighlightColor;

import java.util.Iterator;

import org.openscience.cdk.exception.CDKException;

import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.structure.structure.symmetry.utilities.DetermineSetOfSymmetryAssignments;

/**
 *
 * @author edwardblurock
 */
public class DetermineInternalSymmetryFromSingleDefinition extends DetermineSymmetryFromSingleDefinition {

	boolean debug = false;
	SymmetryMatch externalSymmetryMatch;
	double externalSymmetry = 1.0;
    public SymmetryMatch getExternalSymmetryMatch() {
		return externalSymmetryMatch;
	}
	public void setExternalSymmetryMatch(SymmetryMatch externalSymmetryMatch) {
		this.externalSymmetryMatch = externalSymmetryMatch;
	}
	
	@Override
    public int determineSymmetry(SymmetryDefinition symmetry, IAtomContainer struct)  throws CDKException {
		if(debug) {
			System.out.println("DetermineInternalSymmetryFromSingleDefinition: \n" + symmetry.toString());
		}
        structure = struct;
        symmetryDefinition = symmetry;
        determineSymmetryAssignments = new DetermineSetOfSymmetryAssignments(symmetryDefinition,matchAssignments);
        symmetryDefinition = symmetry;
        determineSetOfSymmetryAssignments(structure);
		if(debug) {
			System.out.println("DetermineInternalSymmetryFromSingleDefinition: \n" + symmetryMatches.toString());
		}

        Double symmetryfactor = symmetry.getInternalSymmetryFactor();
        
        if(externalSymmetryMatch != null) {
        	if(externalSymmetry == symmetryfactor.doubleValue()) {
        		symmetryMatches.eliminateMatchingSymmetries(externalSymmetryMatch);
        	}
        }
		if(debug) {
			System.out.println("DetermineInternalSymmetryFromSingleDefinition: after elimination\n" + symmetryMatches.toString());
		}
        double n = (double) symmetryMatches.size();
        double symmD = Math.pow(symmetryfactor.doubleValue(), n);
        int symmI = (int) Math.round(symmD);
        return symmI;
    }
public double computeSymmetryContribution(int symmetry) {
        return (double) symmetry;
    }
public void setExternalSymmetry(double externalSymmetry) {
	this.externalSymmetry = externalSymmetry;
}
}
