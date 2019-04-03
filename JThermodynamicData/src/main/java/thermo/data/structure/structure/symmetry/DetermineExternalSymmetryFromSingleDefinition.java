/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.symmetry;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.structure.structure.symmetry.utilities.DetermineSetOfSymmetryAssignments;
import thermo.data.structure.structure.symmetry.utilities.DetermineSymmetryAssignmentsFromConnections;

/**
 *
 * @author edwardblurock
 */
public class DetermineExternalSymmetryFromSingleDefinition extends DetermineSymmetryFromSingleDefinition {

    public DetermineExternalSymmetryFromSingleDefinition() {
        matchAssignments = new DetermineSymmetryAssignmentsFromConnections();
    }

    @Override
    public int determineSymmetry(SymmetryDefinition symmetry, IAtomContainer struct) throws CDKException {
        structure = struct;
        symmetryDefinition = symmetry;
        determineSymmetryAssignments = new DetermineSetOfSymmetryAssignments(symmetryDefinition, matchAssignments);
        determineSetOfSymmetryAssignments(structure);
        Double symmetryfactor = symmetry.getInternalSymmetryFactor();
        double n = (double) symmetryMatches.size();
        if(n>0){
            System.out.println("External Symmetry: " + symmetry.getMetaAtomName() + "(symmetry factor =" + symmetryfactor + "):" + "found " + n + " times");
        }
        double symmD = Math.pow(symmetryfactor.doubleValue(), n);
        int symmI = (int) Math.round(symmD);
        return symmI;
    }

}
