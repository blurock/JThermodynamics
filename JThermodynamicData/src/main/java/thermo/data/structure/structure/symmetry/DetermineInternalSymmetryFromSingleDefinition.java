/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.Iterator;

import org.openscience.cdk.exception.CDKException;

import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.structure.structure.symmetry.utilities.DetermineSetOfSymmetryAssignments;

/**
 *
 * @author edwardblurock
 */
public class DetermineInternalSymmetryFromSingleDefinition extends DetermineSymmetryFromSingleDefinition {

	SymmetryMatch externalSymmetryMatch;
    public SymmetryMatch getExternalSymmetryMatch() {
		return externalSymmetryMatch;
	}
	public void setExternalSymmetryMatch(SymmetryMatch externalSymmetryMatch) {
		this.externalSymmetryMatch = externalSymmetryMatch;
	}
	
	@Override
    public int determineSymmetry(SymmetryDefinition symmetry, IAtomContainer struct)  throws CDKException {
        structure = struct;
        symmetryDefinition = symmetry;
        determineSymmetryAssignments = new DetermineSetOfSymmetryAssignments(symmetryDefinition,matchAssignments);
        symmetryDefinition = symmetry;
        determineSetOfSymmetryAssignments(structure);
        Double symmetryfactor = symmetry.getInternalSymmetryFactor();
        if(externalSymmetryMatch != null) {
        	symmetryMatches.eliminateMatchingSymmetries(externalSymmetryMatch);
        }
        double n = (double) symmetryMatches.size();
        double symmD = Math.pow(symmetryfactor.doubleValue(), n);
        int symmI = (int) Math.round(symmD);
        return symmI;
    }
public double computeSymmetryContribution(int symmetry) {
        return (double) symmetry;
    }
}
