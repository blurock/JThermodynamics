/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import java.sql.SQLException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.structure.symmetry.DB.SQLSetOfSymmetryDefinitions;
import thermo.exception.ThermodynamicException;
import thermo.properties.SProperties;

/**
 *
 * @author edwardblurock
 */
public class CalculateExternalSymmetryCorrection extends CalculateSymmetryCorrectionInterface {
	String linearS = "LinearStructure";
	String externalS = "ExternalSymmetry";
	String secondaryS = "SecondaryExternalSymmetry";
	String referenceS = "External Symmetry Correction";
	SQLSetOfSymmetryDefinitions setOfDefinitions;
	SQLSetOfSymmetryDefinitions secondaryDefinitions;
	double gasConstant;
	private final DetermineExternalSymmetryFromSingleDefinition fromSingleDefinition;
	private final DetermineExternalSymmetry determineTotal;

	public CalculateExternalSymmetryCorrection(ThermoSQLConnection c) throws ThermodynamicException {
		super(c);
		try {
			setOfDefinitions = new SQLSetOfSymmetryDefinitions(connect, externalS);
			secondaryDefinitions = new SQLSetOfSymmetryDefinitions(connect, secondaryS);
			String gasconstantS = SProperties.getProperty("thermo.data.gasconstant.clasmolsk");
			gasConstant = Double.valueOf(gasconstantS).doubleValue();
			fromSingleDefinition = new DetermineExternalSymmetryFromSingleDefinition();
			determineTotal = new DetermineExternalSymmetry(fromSingleDefinition, setOfDefinitions,
					secondaryDefinitions);

		} catch (SQLException ex) {
			throw new ThermodynamicException(ex.toString());
		}
	}
	
	public SymmetryMatch getSymmetryMatch() {
		return determineTotal.getSymmetryMatch();
	}

	public void setSymmetryMatch(SymmetryMatch symmetryMatch) {
		determineTotal.setSymmetryMatch(symmetryMatch);
	}
	public double getExternalSymmetryValue() {
		return Double.valueOf(determineTotal.getSymmetryValue());
	}
	

	public boolean calculate(IAtomContainer mol, SetOfBensonThermodynamicBase corrections)
			throws ThermodynamicException {
		boolean found = false;
		int totalsymmetry = 1;
		try {
			determineTotal.setSetOfCorrections(corrections);
			totalsymmetry = determineTotal.determineSymmetry(mol,corrections);
		} catch (CDKException ex) {
			throw new ThermodynamicException(ex.toString());
		}
		if(totalsymmetry > 1) {
			found =true;
		}
		return found;
	}

}
