/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.structure.structure.symmetry.utilities.DetermineSetOfSymmetryAssignments;

/**
 *
 * @author edwardblurock
 */
public class DetermineInternalSymmetryFromSingleDefinition extends DetermineSymmetryFromSingleDefinition {

    @Override
    public int determineSymmetry(SymmetryDefinition symmetry, IAtomContainer struct)  throws CDKException {
        structure = struct;
        symmetryDefinition = symmetry;
        determineSymmetryAssignments = new DetermineSetOfSymmetryAssignments(symmetryDefinition,matchAssignments);
        symmetryDefinition = symmetry;
        determineSetOfSymmetryAssignments(structure);
        Double symmetryfactor = symmetry.getInternalSymmetryFactor();
        double n = (double) symmetryMatches.size();
        double symmD = Math.pow(symmetryfactor.doubleValue(), n);
        int symmI = (int) Math.round(symmD);
        return symmI;
    }
public double computeSymmetryContribution(int symmetry) {
        return (double) symmetry;
    }
}
