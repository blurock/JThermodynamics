package thermo.build.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.MetaAtomDefinition;
import thermo.data.structure.structure.MetaAtomInfo;
import thermo.data.structure.structure.SetOfMetaAtomsForSubstitution;
import thermo.data.structure.structure.DB.SQLMetaAtomDefinitionFromMetaAtomInfo;
import thermo.data.structure.structure.DB.SQLMetaAtomInfo;
import thermo.data.structure.structure.matching.SubstituteMetaAtom;

public class TestBensonAtom {
	static String bensonAtomType = "BensonAtom";
	static SetOfMetaAtomsForSubstitution metaAtomSubstitutions;
	static SQLMetaAtomDefinitionFromMetaAtomInfo sqlMetaAtom;
	
	public static IAtomContainer performWithFullSet(StringBuffer buf, 
			IAtomContainer molecule,
			ThermoSQLConnection connection) {
		try {
			sqlMetaAtom = new SQLMetaAtomDefinitionFromMetaAtomInfo(connection);
			metaAtomSubstitutions = sqlMetaAtom.createSubstitutionSets(bensonAtomType);

			metaAtomSubstitutions.substitute(molecule);
		} catch (ClassNotFoundException | CDKException | IOException | SQLException e) {
			buf.append(e.toString());
		}
		
		return molecule;
	}
	
	public static IAtomContainer performSingleTest(StringBuffer buf, 
			String benson,
			IAtomContainer molecule,
			ThermoSQLConnection connection) {
		try {
		sqlMetaAtom = new SQLMetaAtomDefinitionFromMetaAtomInfo(connection);
		metaAtomSubstitutions = sqlMetaAtom.createSubstitutionSets(bensonAtomType);
		
			SQLMetaAtomInfo sqlmetaatom = new SQLMetaAtomInfo(connection);
			@SuppressWarnings("unchecked")
			HashSet<MetaAtomInfo> set = (HashSet<MetaAtomInfo>) sqlmetaatom.retrieveStructuresFromDatabase(benson);
			Iterator<MetaAtomInfo> iter = set.iterator();
			if(iter.hasNext()) {
				MetaAtomInfo info =  (MetaAtomInfo) iter.next();
				SQLMetaAtomDefinitionFromMetaAtomInfo sqlmetadef = new SQLMetaAtomDefinitionFromMetaAtomInfo(connection);
				MetaAtomDefinition def = sqlmetadef.createMetaAtomDefinition(info);
        
				buf.append("MetaAtomDefinition  -----------------------------------------------\n");
				buf.append(def.toString());
				buf.append("MetaAtomDefinition  -----------------------------------------------\n");
				SubstituteMetaAtom substitute = new SubstituteMetaAtom(def);
				substitute.substitute(molecule);
			} else {
				buf.append("Not a valid benson atom name (not found): " + benson + "\n");
			}
		} catch(Exception e) {
			buf.append(e.toString());
			molecule = null;
		}
		
		return molecule;
		
	}
	
	public static ArrayList<String> listAllElements(StringBuffer buf, 
			ThermoSQLConnection connection) {
		SQLMetaAtomInfo sqlmetaatom = new SQLMetaAtomInfo(connection);
		ArrayList<String> lst = new ArrayList<String>();

		try {
			@SuppressWarnings("unchecked")
			HashSet<MetaAtomInfo> set = (HashSet<MetaAtomInfo>) sqlmetaatom.retrieveMetaAtomTypesFromDatabase(bensonAtomType);
			Iterator<MetaAtomInfo> iter = set.iterator();
			buf.append("Name        \t\t Substitute \t CMLStructure\n");
			while(iter.hasNext()) {
				StringBuffer line = new StringBuffer();
				MetaAtomInfo info = iter.next();
				line.append("BensonAtom." + info.getMetaAtomName());
				line.append("\t\t");
				line.append(info.getMetaAtomName());
				line.append("        \t");
				line.append(info.getElementName());
				lst.add(line.toString());
				buf.append(line + "\n");
			}	
		} catch (SQLException e) {
			buf.append(e.toString());
		}
		return lst;
	}
	
	public static MetaAtomInfo getBensonAtom(String benson,
			StringBuffer buf, 
			ThermoSQLConnection connection) {
		MetaAtomInfo info = null;
		try {
			sqlMetaAtom = new SQLMetaAtomDefinitionFromMetaAtomInfo(connection);
			metaAtomSubstitutions = sqlMetaAtom.createSubstitutionSets(bensonAtomType);
			
				SQLMetaAtomInfo sqlmetaatom = new SQLMetaAtomInfo(connection);
				@SuppressWarnings("unchecked")
				HashSet<MetaAtomInfo> set = (HashSet<MetaAtomInfo>) sqlmetaatom.retrieveStructuresFromDatabase(benson);
				Iterator<MetaAtomInfo> iter = set.iterator();
				if(iter.hasNext()) {
					info =  iter.next();
					SQLMetaAtomDefinitionFromMetaAtomInfo sqlmetadef = new SQLMetaAtomDefinitionFromMetaAtomInfo(connection);
					MetaAtomDefinition def = sqlmetadef.createMetaAtomDefinition(info);
	        
					buf.append("MetaAtomDefinition  -----------------------------------------------\n");
					buf.append(def.toString());
					buf.append("MetaAtomDefinition  -----------------------------------------------\n");
				} else {
					buf.append("Not a valid benson atom name (not found): " + benson + "\n");					
				}
		} catch(Exception e) {
			buf.append(e.toString());
		}
		return info;
	}

}
