/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import org.openscience.cdk.interfaces.IAtomContainer;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.exception.ThermodynamicException;

/**
 *
 * @author edwardblurock
 */
public class CalculateSymmetryCorrectionInterface {
        ThermoSQLConnection connect;

        public CalculateSymmetryCorrectionInterface(ThermoSQLConnection c) throws ThermodynamicException {
            connect = c;
    }
        public CalculateSymmetryCorrectionInterface() throws ThermodynamicException {
            connect = null;
    }

    public SetOfBensonThermodynamicBase calculate(IAtomContainer mol) throws ThermodynamicException {
            SetOfBensonThermodynamicBase corrections = new SetOfBensonThermodynamicBase();
            calculate(mol, corrections);
            
        return corrections;
    }
    public boolean calculate(IAtomContainer mol, SetOfBensonThermodynamicBase corrections) throws ThermodynamicException {
    	return false;
    }
}
