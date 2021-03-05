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
import thermo.data.structure.utilities.MoleculeUtilities;
import thermo.exception.ThermodynamicException;
import thermo.properties.SProperties;

/**
 *
 * @author edwardblurock
 */
public class CalculateInternalSymmetryCorrection extends CalculateSymmetryCorrectionInterface  {
	boolean debug = true;
    String internalS = "InternalSymmetry";
    String referenceS = "Internal Symmetry Correction";
    SQLSetOfSymmetryDefinitions setOfDefinitions;
    DetermineInternalSymmetryFromSingleDefinition fromSingleDefinition;
    DetermineInternalSymmetry determineTotal;
    double gasConstant;
    boolean externalsymmetry;
    
    SymmetryMatch externalSymmetryMatch;
    double externalSymmetryValue = 1.0;
    double externalSymmetry = 1.0;
    CalculateExternalSymmetryCorrection externalCorrection;

    public CalculateInternalSymmetryCorrection(ThermoSQLConnection c,
    		CalculateExternalSymmetryCorrection externalCorrection) throws ThermodynamicException {
        super(c);
        this.externalCorrection = externalCorrection;
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
    	if(debug) {
    		System.out.println("CalculateInternalSymmetryCorrection.calculate");
    		System.out.println(MoleculeUtilities.toString(mol));
    	}
    	boolean found = false;
        this.externalSymmetryMatch = externalCorrection.getSymmetryMatch();
        this.externalSymmetryValue = externalCorrection.getExternalSymmetryValue();

    	try {
        	determineTotal.setSetOfCorrections(corrections);
        	fromSingleDefinition.setExternalSymmetryMatch(externalSymmetryMatch);
        	fromSingleDefinition.setExternalSymmetry(externalSymmetryValue);
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
