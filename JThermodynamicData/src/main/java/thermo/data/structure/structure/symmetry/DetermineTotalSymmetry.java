/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import java.util.Iterator;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.benson.SetOfBensonThermodynamicBase;

import org.openscience.cdk.exception.CDKException;

/**
 *
 * @author blurock
 */
public class DetermineTotalSymmetry {
    DetermineSymmetryFromSingleDefinition determineSymmetry;
    SetOfSymmetryDefinitions symmetryDefinitions;
    int symmetryValue;
    
    public DetermineTotalSymmetry(DetermineSymmetryFromSingleDefinition determine,
                            SetOfSymmetryDefinitions definitions) {
        determineSymmetry = determine;
        symmetryDefinitions = definitions;
    }
    public void initializeSymmetry() {
        symmetryValue = 0;
    }
    public int determineSymmetry(IAtomContainer structure, SetOfBensonThermodynamicBase corrections) throws CDKException {
        initializeSymmetry();
        Iterator<SymmetryDefinition> idef = symmetryDefinitions.iterator();
        while(idef.hasNext()) {
            SymmetryDefinition defintion = idef.next();
            int symmetry = determineSymmetry.determineSymmetry(defintion, structure);
            double sym = determineSymmetry.computeSymmetryContribution(symmetry);
            int symI = (int) Math.floor(sym);
            
            
            
            
            //System.out.println("DetermineTotalSymmetry:\n" + defintion.getMetaAtomName() + "Symmetry=" + symI);
            combineInSymmetryNumber(symI);
        }
        return getSymmetryValue();
    }
    public void combineInSymmetryNumber(int symmetry) {
        symmetryValue += symmetry;
    }
    public int getSymmetryValue() {
        return symmetryValue;
    }
}
