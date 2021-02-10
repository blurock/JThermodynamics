package metaatom;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.MetaAtomDefinition;
import thermo.data.structure.structure.MetaAtomInfo;
import thermo.data.structure.structure.SetOfMetaAtomsForSubstitution;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.SubstituteBackMetaAtomsIntoMolecule;
import thermo.data.structure.structure.DB.SQLMetaAtomDefinitionFromMetaAtomInfo;
import thermo.data.structure.structure.DB.SQLMetaAtomInfo;
import thermo.data.structure.structure.matching.SubstituteMetaAtom;
import thermo.data.structure.structure.MetaAtomInfo;


public class TestMetaAtomSubstitutionFromDatabase {

	@Test
	public void test() {
		try {
		ThermoSQLConnection c = new ThermoSQLConnection();
		c.connect();

		IAtomContainer molecule;
		NancyLinearFormToMolecule nancyFormToMolecule = new NancyLinearFormToMolecule(c);
		
		//String nancy = "ch3/o/o/ch3";
		String nancy = "ch(#1)&ch&ch&ch&ch&c(ch3)&1";
		//String nancy = "ch3/ch2/ch2/ch3";
		//String nancy = "c(ch3)2//c(ch3)2";
		//String nancy = "ch2//ch/ch3";
		molecule = nancyFormToMolecule.convert(nancy);
        System.out.println("Molecule  -----------------------------------------------");
        System.out.println(molecule.toString());
        System.out.println("Molecule  -----------------------------------------------");
		String bensonS = "BensonAtom.s";
		String benson = "BensonAtom.c/d";
		String benson2 = "BensonAtom.oo";
		String benson1 = "BensonAtom.c";
		String bensonA = "BensonAtom.c/a";
        
        substitute(c,molecule,bensonA);
         //substitute(c,molecule,bensonS);
        //substitute(c,molecule,benson1);
        
        
        
		} catch(java.sql.SQLException ex) {
			System.out.println(ex.toString());
		} catch(CDKException ex1) {
			System.out.println(ex1.toString());
		} catch(ClassNotFoundException ex) {
			System.out.println(ex.toString());
		} catch(IOException ex) {
			System.out.println(ex.toString());
		}
	}
	private void substitute(ThermoSQLConnection c,  IAtomContainer molecule, String benson) 
		throws java.sql.SQLException, CDKException, ClassNotFoundException , IOException{
		SQLMetaAtomInfo sqlmetaatom = new SQLMetaAtomInfo(c);
		HashSet set = sqlmetaatom.retrieveStructuresFromDatabase(benson);
		Iterator iter = set.iterator();
        MetaAtomInfo info =  (MetaAtomInfo) iter.next();
        
        SQLMetaAtomDefinitionFromMetaAtomInfo sqlmetadef = new SQLMetaAtomDefinitionFromMetaAtomInfo(c);
        MetaAtomDefinition def = sqlmetadef.createMetaAtomDefinition(info);
        
        System.out.println("MetaAtomDefinition  -----------------------------------------------");
        System.out.println(def.toString());
        System.out.println("MetaAtomDefinition  -----------------------------------------------");
        SubstituteMetaAtom substitute = new SubstituteMetaAtom(def);
        substitute.substitute(molecule);
        StructureAsCML cml = new StructureAsCML(molecule);
        System.out.println("Molecule  -----------------------------------------------");
        System.out.println(cml.toString());
        System.out.println("Molecule  -----------------------------------------------");
        

	}
}
