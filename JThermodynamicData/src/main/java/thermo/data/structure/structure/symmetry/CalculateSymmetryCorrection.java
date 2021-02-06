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
public class CalculateSymmetryCorrection extends CalculateSymmetryCorrectionInterface {
    CalculateOpticalSymmetryCorrection optical;
    CalculateInternalSymmetryCorrection internal;
    CalculateExternalSymmetryCorrection external;

    public CalculateSymmetryCorrection(ThermoSQLConnection c) throws ThermodynamicException {
        super(c);
            optical = new CalculateOpticalSymmetryCorrection(connect);
            internal = new CalculateInternalSymmetryCorrection(connect);
            external = new CalculateExternalSymmetryCorrection(connect);
    }
    public boolean calculate(IAtomContainer mol, SetOfBensonThermodynamicBase corrections) throws ThermodynamicException {
        boolean exfound = external.calculate(mol, corrections);
        boolean infound = internal.calculate(mol, corrections);
        boolean opfound = optical.calculate(mol, corrections);
        return exfound | infound | opfound;
    }
    void calculate(IAtomContainer R, IAtomContainer RH, SetOfBensonThermodynamicBase corrections) throws ThermodynamicException {
        SetOfBensonThermodynamicBase correctionsR = new SetOfBensonThermodynamicBase();
        SetOfBensonThermodynamicBase correctionsRH = new SetOfBensonThermodynamicBase();
        this.calculate(RH, correctionsRH);
        this.calculate(R, correctionsR);
    }

}
