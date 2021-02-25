package symmetry;

import java.util.HashSet;
import java.util.Iterator;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.DetermineSymmetryFromSingleDefinition;
import thermo.data.structure.structure.symmetry.SymmetryDefinition;
import thermo.data.structure.structure.symmetry.DB.SQLSymmetryDefinition;

public class TestSingleSymmetryDefinition {

	@Test
	public void test() {
        try {
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            String moleculename = "ch3/ch2/ch3";
            String symname = "ExternalSymmetry-C(B1)(B1)(B2)(B2)";
            //String symname = "ExternalSymmetry-C(B1)(B1)(B1)(B1)-X";
            
            NancyLinearFormToMolecule nancy = new NancyLinearFormToMolecule(connect);
            IAtomContainer mol = nancy.convert(moleculename);
            StructureAsCML cml = new StructureAsCML(mol);
            System.out.println("------------------------------------------\n");
            System.out.println(cml.toString());
            
            SQLSymmetryDefinition sqlSymmetry = new SQLSymmetryDefinition(connect);
            HashSet vec = sqlSymmetry.retrieveStructuresFromDatabase(symname);
            Iterator<SymmetryDefinition> siter = vec.iterator();
            SymmetryDefinition symmetry = siter.next();
            System.out.println("------------------------------------------\n");
            System.out.println(symmetry.toString());
            System.out.println("------------------------------------------\n");
            
            DetermineSymmetryFromSingleDefinition single = new DetermineSymmetryFromSingleDefinition();
            int result = single.determineSymmetry(symmetry, mol);
            System.out.println("Result = " + result);

        } catch(Exception ex) {
        	ex.printStackTrace();
        }
	}

}
