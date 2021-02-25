package metaatom;


import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.SetOfMetaAtomsForSubstitution;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.DB.SQLMetaAtomDefinitionFromMetaAtomInfo;

public class SubstittueBensonAtomsTest {

	@Test
	public void test() {
		try {
		ThermoSQLConnection connect = new ThermoSQLConnection();
        connect.connect();
		String bensonAtomType = "BensonAtom";
		SetOfMetaAtomsForSubstitution metaAtomSubstitutions;
		SQLMetaAtomDefinitionFromMetaAtomInfo sqlMetaAtom;
		sqlMetaAtom = new SQLMetaAtomDefinitionFromMetaAtomInfo(connect);
			metaAtomSubstitutions = sqlMetaAtom.createSubstitutionSets(bensonAtomType);
		
		IAtomContainer molecule;
		NancyLinearFormToMolecule nancyFormToMolecule = new NancyLinearFormToMolecule(connect);
		String nancy = "ch3/o/o/ch3";
		molecule = nancyFormToMolecule.convert(nancy);
        IAtomContainer substituted = metaAtomSubstitutions.substitute(molecule);
        System.out.println("computeThermodynamicsForMolecule  after meta-atom substitutions");
        StructureAsCML cmlstruct = new StructureAsCML(substituted);
        System.out.println(cmlstruct.toString());
		} catch (ClassNotFoundException | SQLException | CDKException | IOException e) {
			e.printStackTrace();
		}

	}

}
