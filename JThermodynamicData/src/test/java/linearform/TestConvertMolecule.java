package linearform;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashSet;

import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.compute.utilities.StringToAtomContainer;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.MetaAtomInfo;
import thermo.data.structure.structure.StructureAsCML;
import thermo.exception.ThermodynamicComputeException;

public class TestConvertMolecule {

	@Test
	public void test() {
		String structure = "c(ch3)3/oh";
		HashSet<MetaAtomInfo> metaatoms = new HashSet<MetaAtomInfo>();
		StringToAtomContainer convert = new StringToAtomContainer(metaatoms);
		String form = "NANCY";
		IAtomContainer molecule;
		try {
			System.out.println("1 ----------------------------------------------");
			NancyLinearFormToMolecule nancyFormToMolecule = new NancyLinearFormToMolecule(metaatoms);
			molecule = nancyFormToMolecule.convert(structure);
			//molecule = convert.stringToAtomContainer(form, structure);
			System.out.println("2 ----------------------------------------------");
			StructureAsCML cml = new StructureAsCML(molecule);
			System.out.println("3 ----------------------------------------------");
			System.out.println(cml.toString());
			System.out.println("4 ----------------------------------------------");
//		} catch (ThermodynamicComputeException e) {
//			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
