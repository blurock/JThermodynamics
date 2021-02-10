/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.symmetry;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.structure.symmetry.DB.SQLSetOfSymmetryDefinitions;
import thermo.exception.ThermodynamicException;
import thermo.properties.SProperties;

/**
 *
 * @author edwardblurock
 */
public class CalculateInternalSymmetryCorrection extends CalculateSymmetryCorrectionInterface  {

    String internalS = "InternalSymmetry";
    String referenceS = "Internal Symmetry Correction";
    SQLSetOfSymmetryDefinitions setOfDefinitions;
    DetermineSymmetryFromSingleDefinition fromSingleDefinition;
    DetermineInternalSymmetry determineTotal;
    double gasConstant;
    boolean externalsymmetry;

    public CalculateInternalSymmetryCorrection(ThermoSQLConnection c) throws ThermodynamicException {
        super(c);
        try {
            setOfDefinitions = new SQLSetOfSymmetryDefinitions(connect, internalS);
            //System.out.println("CalculateInternalSymmetryCorrection\n" +  setOfDefinitions.toString());
            fromSingleDefinition = new DetermineInternalSymmetryFromSingleDefinition();
            determineTotal = new DetermineInternalSymmetry(fromSingleDefinition, setOfDefinitions);
            String gasconstantS = SProperties.getProperty("thermo.data.gasconstant.clasmolsk");
            gasConstant = Double.valueOf(gasconstantS).doubleValue();
        } catch (SQLException ex) {
            Logger.getLogger(CalculateInternalSymmetryCorrection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean calculate(IAtomContainer mol, SetOfBensonThermodynamicBase corrections) throws ThermodynamicException {
    	boolean found = false;
    	try {
        	determineTotal.setSetOfCorrections(corrections);
            int internalsymmetry = calculateInternalSymmetry(mol,corrections);
            if (internalsymmetry > 0.0 && internalsymmetry != 1.0) {
            	found = true;
                double correction = -gasConstant * Math.log(internalsymmetry);
                String name = internalS + "(" + Integer.toString(internalsymmetry) + ")";
                BensonThermodynamicBase benson = new BensonThermodynamicBase(name, null, 0.0, correction);
                benson.setReference(name);
                //corrections.add(benson);
            }
        } catch (CDKException ex) {
            Logger.getLogger(CalculateInternalSymmetryCorrection.class.getName()).log(Level.SEVERE, null, ex);
        }
    	return found;
    }

    public int calculateInternalSymmetry(IAtomContainer mol, SetOfBensonThermodynamicBase corrections) throws CDKException {
        return determineTotal.determineSymmetry(mol,corrections);
    }

	public boolean isExternalsymmetry() {
		return externalsymmetry;
	}

	public void setExternalsymmetry(boolean externalsymmetry) {
		this.externalsymmetry = externalsymmetry;
	}
    
}
