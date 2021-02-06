/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.utilities.DetermineSetOfSymmetryAssignments;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.structure.structure.symmetry.utilities.DetermineSymmetryAssignmentsFromConnections;

/** DetermineSymmetryFromSingleDefinition
 *
 * @author blurock
 */
public class DetermineSymmetryFromSingleDefinition {
    SymmetryDefinition symmetryDefinition;
    DetermineSetOfSymmetryAssignments determineSymmetryAssignments;
    IAtomContainer structure;
    SetOfSymmetryMatches symmetryMatches;
    DetermineSymmetryAssignmentsFromConnections matchAssignments;

    public SetOfSymmetryMatches getSymmetryMatches() {
        return symmetryMatches;
    }
    
    public DetermineSymmetryFromSingleDefinition() {
        matchAssignments = new DetermineSymmetryAssignmentsFromConnections();
    }
    
    public int determineSymmetry(SymmetryDefinition symmetry, IAtomContainer struct)  throws CDKException {
        structure = struct;
        symmetryDefinition = symmetry;
        determineSymmetryAssignments = new DetermineSetOfSymmetryAssignments(symmetryDefinition,matchAssignments);
        symmetryDefinition = symmetry;
        determineSetOfSymmetryAssignments(structure);
        return symmetryMatches.size();
    }
    public double computeSymmetryContribution(int symmetry) {
        double symmD = (double) symmetry;
        return symmD * symmetryDefinition.getInternalSymmetryFactor();
    }
    public void determineSetOfSymmetryAssignments(IAtomContainer struct) throws CDKException {
        structure = struct;
        //StructureAsCML cml = new StructureAsCML(struct);
        //System.out.println(cml.toString());
        symmetryMatches = determineSymmetryAssignments.findIfMatchInStructures(struct);
    }

}
