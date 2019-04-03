/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import thermo.data.structure.structure.symmetry.utilities.DetermineSetOfSymmetryAssignments;
import thermo.data.structure.structure.symmetry.utilities.DetermineSymmetryAssignmentsFromConnections;

/**
 *
 * @author edwardblurock
 */
public class DetermineSetOfExternalSymmetryAssignments extends DetermineSetOfSymmetryAssignments {

    public DetermineSetOfExternalSymmetryAssignments(SymmetryDefinition definition) {
        super(definition, new DetermineSymmetryAssignmentsFromConnections());
    }


//     protected SetOfSymmetryAssignments findSymmetryAssignments(IAtomContainer structure, List<RMap> set) throws CDKException {
//        boolean ans = false;
//        //System.out.println("========================================================================================");
//        Hashtable<String, String> matched = null;
//        SetOfSymmetryAssignments assignments = null;
//        DetermineSymmetryAssignmentsFromConnections matchassignments = new DetermineSymmetryAssignmentsFromConnections();
//        Hashtable<String, IAtomContainer> connections = findConnectedSubstructures(structure, set);
//        //boolean uniquesubstructures = overlap.noOverlapInStructures(connections);
//        boolean uniquesubstructures = true;
//        if (uniquesubstructures) {
//            matchassignments.determineSymmetryAssignments(connections);
//            assignments = matchassignments.getAssignments();
//            //System.out.println("\nAssignments-----------------------------------\n" + assignments.toString());
//            findCorrespondences(assignments,set,structure);
//        }
//        return assignments;
//    }

}
