package symmetry;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.DetermineSymmetryFromSingleDefinition;
import thermo.data.structure.structure.symmetry.DetermineTotalOpticalSymmetry;
import thermo.data.structure.structure.symmetry.SymmetryDefinition;
import thermo.data.structure.structure.symmetry.DB.SQLSetOfSymmetryDefinitions;
import thermo.data.structure.structure.symmetry.DB.SQLSymmetryDefinition;

public class TestDatabaseSingleOpticalSymmetry {

	@Test
	public void test() {
		try {
        ThermoSQLConnection c = new ThermoSQLConnection();
        c.connect();
		
		
        String nancy = "ch3/o/o/ch3";
		NancyLinearFormToMolecule nancyFormToMolecule;
			nancyFormToMolecule = new NancyLinearFormToMolecule(c);
		
		IAtomContainer molecule = nancyFormToMolecule.convert(nancy);
        System.out.println("Molecule  -----------------------------------------------");
        StructureAsCML cmlstruct = new StructureAsCML(molecule);
        System.out.println(cmlstruct.toString());
        System.out.println("Molecule  -----------------------------------------------");

        String symname = "OpticalSymmetry-Peroxy(Z1)2";
        SQLSetOfSymmetryDefinitions setOfDefinitions = new SQLSetOfSymmetryDefinitions(c);
        SQLSymmetryDefinition sqldef = new SQLSymmetryDefinition(c);
        HashSet<SymmetryDefinition> set = sqldef.retrieveStructuresFromDatabase(symname);
        Iterator<SymmetryDefinition> iter = set.iterator();
        SymmetryDefinition def = iter.next();
        System.out.println("Symmetry  -----------------------------------------------");
        System.out.println(symname + "\n" + def.toString());
        System.out.println("Symmetry  -----------------------------------------------");
        DetermineSymmetryFromSingleDefinition fromSingleDefinition = new DetermineSymmetryFromSingleDefinition();
        int symmetry = fromSingleDefinition.determineSymmetry(def, molecule);
        System.out.println("Symmetry of " +  symname + ": " + symmetry);
        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

}
