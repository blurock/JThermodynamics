/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;

/**
 *
 * @author blurock
 */
public class DetermineOpticalSymmetryFromSingleDefinition extends DetermineSymmetryFromSingleDefinition {

    public DetermineOpticalSymmetryFromSingleDefinition() {
    }
    @Override
    public int determineSymmetry(SymmetryDefinition symmetry,IAtomContainer structure)  throws CDKException {
        return super.determineSymmetry(symmetry,structure);
    }
}
