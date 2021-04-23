/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson.thergas;

import jThergas.data.JThermgasThermoStructureDataPoint;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLAtomCounts;
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.DB.SQLStructureType;
import thermo.data.structure.structure.AtomCounts;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.StructureType;

/**
 *
 * @author edwardblurock
 */
public class BuildThermoForSubStructures extends BuildThermoForMolecules {

	protected SQLStructureAsCML sqlCMLStructure;
	protected SQLStructureType sqlStructureType;
	protected SQLAtomCounts sqlAtomCounts;
	protected String substructureType;

	public BuildThermoForSubStructures(String type, String source) {
		substructureType = type;
		sourceS = source;
		initialize();
	}

	@Override
	protected void initialize() {
		super.initialize();
		sqlCMLStructure = new SQLStructureAsCML(connection);
		sqlStructureType = new SQLStructureType(connection);
		sqlAtomCounts = new SQLAtomCounts(connection);
	}

	/**
	 * Initialize the Benson Table
	 *
	 * Through calls to initialization methods in {@link SQLStructureAsCML} and
	 * {@link SQLBensonThermodynamicBase} the Benson table is initialized.
	 *
	 * @throws java.sql.SQLException
	 */
	@Override
	public void initializeTable(String reference, boolean storedata) throws SQLException {
		sqlthermo.initializeStructureInDatabase(reference);
		String[] names = sqlCMLStructure.findStructuresOfSource(reference);
		if (storedata) {
			sqlStructureType.deleteElements(names);
			sqlCMLStructure.deleteFromSource(reference);
			for (int i = 0; i < names.length; i++) {
				String name = names[i];
				sqlAtomCounts.deleteElement(name);
			}
		} else {
			System.out.println("===========================================");
			System.out.println("Delete all the following structures with reference '" + reference + "'");
			System.out.println("(StructureType, CMLStructures, AtomCounts)");
			for (int i = 0; i < names.length; i++) {
				String name = names[i];
				System.out.println("To delete: '" + name + "'");
				System.out.println("===========================================");
			}

		}
	}

	@Override
	protected void addMoleculeStructureToDatabase(JThermgasThermoStructureDataPoint point, ThermoSQLConnection c)
			throws CDKException, SQLException {
		try {
			IAtomContainer molecule = buildBenson.buildMolecule(point, c);
			substitute.substitute(molecule);
			StructureAsCML cmlstruct = new StructureAsCML(molecule, sourceS);
			sqlCMLStructure.deleteByKey(sqlCMLStructure.keyFromStructure(cmlstruct));
			sqlCMLStructure.addToDatabase(cmlstruct);

			StructureType structuretype = new StructureType(cmlstruct.getNameOfStructure(), substructureType);
			sqlStructureType.deleteByKey(sqlStructureType.keyFromStructure(structuretype));
			sqlStructureType.addToDatabase(structuretype);
			AtomCounts counts = new AtomCounts(molecule);
			sqlAtomCounts.deleteElement(counts);
			sqlAtomCounts.addToDatabase(counts);

		} catch (CDKException ex) {
			Logger.getLogger(BuildThermoForMolecules.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
