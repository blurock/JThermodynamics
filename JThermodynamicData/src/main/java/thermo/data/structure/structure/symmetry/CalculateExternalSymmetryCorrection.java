/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.structure.DB.SQLSubstituteBackMetaAtomIntoMolecule;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.matching.SubstituteLinearStructures;
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
	String nancyLinearFormType = "NancyLinearForm";
	String referenceS = "External Symmetry Correction";
	SubstituteLinearStructures substitutions;
	SQLSetOfSymmetryDefinitions setOfDefinitions;
	SQLSetOfSymmetryDefinitions secondaryDefinitions;
	SQLSubstituteBackMetaAtomIntoMolecule substituteBack;
	double gasConstant;
	private final DetermineExternalSymmetryFromSingleDefinition fromSingleDefinition;
	private final DetermineExternalSymmetry determineTotal;

	public CalculateExternalSymmetryCorrection(ThermoSQLConnection c) throws ThermodynamicException {
		super(c);
		try {
			substitutions = new SubstituteLinearStructures(connect);
			setOfDefinitions = new SQLSetOfSymmetryDefinitions(connect, externalS);
			secondaryDefinitions = new SQLSetOfSymmetryDefinitions(connect, secondaryS);
			String gasconstantS = SProperties.getProperty("thermo.data.gasconstant.clasmolsk");
			gasConstant = Double.valueOf(gasconstantS).doubleValue();
			fromSingleDefinition = new DetermineExternalSymmetryFromSingleDefinition();
			determineTotal = new DetermineExternalSymmetry(fromSingleDefinition, setOfDefinitions,
					secondaryDefinitions);
			substituteBack = new SQLSubstituteBackMetaAtomIntoMolecule(nancyLinearFormType, connect);

		} catch (SQLException ex) {
			throw new ThermodynamicException(ex.toString());
		} catch (CDKException ex) {
			throw new ThermodynamicException(ex.toString());
		} catch (ClassNotFoundException ex) {
			throw new ThermodynamicException(ex.toString());
		} catch (IOException ex) {
			throw new ThermodynamicException(ex.toString());
		}
	}

	public boolean calculate(IAtomContainer mol, SetOfBensonThermodynamicBase corrections)
			throws ThermodynamicException {
		boolean found = false;
		int totalsymmetry = 1;
		try {
			StructureAsCML cmlstruct = new StructureAsCML(mol);
			IAtomContainer newmolecule = substitutions.substitute(cmlstruct);
			substituteBack.substitute(newmolecule);
			StructureAsCML cmlnew = new StructureAsCML(newmolecule);
			determineTotal.setSetOfCorrections(corrections);
			totalsymmetry = determineTotal.determineSymmetry(newmolecule);
		} catch (IOException ex) {
			Logger.getLogger(CalculateExternalSymmetryCorrection.class.getName()).log(Level.SEVERE, null, ex);
		} catch (CDKException ex) {
			throw new ThermodynamicException(ex.toString());
		}
		if(totalsymmetry > 1) {
			found =true;
		}
		return found;
	}
}
