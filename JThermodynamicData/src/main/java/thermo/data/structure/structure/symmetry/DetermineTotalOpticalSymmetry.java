/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;


/**
 *
 * @author blurock
 */
public class DetermineTotalOpticalSymmetry extends DetermineTotalSymmetry {
    
    public DetermineTotalOpticalSymmetry(DetermineSymmetryFromSingleDefinition determine,
                            SetOfSymmetryDefinitions definitions) {
        super(determine,definitions);
        determineSymmetry = determine;
    }
    public void initializeSymmetry() {
        symmetryValue = 0;
    }
    public void combineInSymmetryNumber(int symmetry) {
        symmetryValue += symmetry;
    }
    public int getSymmetryValue() {
        return symmetryValue;
    }
}

