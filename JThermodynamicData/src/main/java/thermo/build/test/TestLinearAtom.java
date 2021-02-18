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
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.DB.SQLMetaAtomDefinitionFromMetaAtomInfo;
import thermo.data.structure.structure.DB.SQLMetaAtomInfo;
import thermo.data.structure.structure.matching.SubstituteLinearStructures;
import thermo.data.structure.structure.matching.SubstituteMetaAtom;

public class TestLinearAtom {
    String linearS = "LinearStructure";
    static IAtomContainer lmolecule;
    public static String linearAtomType = "LinearStructure";

	public static IAtomContainer performWithFullSet(StringBuffer buf, IAtomContainer molecule, ThermoSQLConnection connection) {
		try {
			SubstituteLinearStructures substitutions = new SubstituteLinearStructures(connection);
			StructureAsCML cml = new StructureAsCML(molecule);
			lmolecule = substitutions.substitute(cml);
		} catch (ClassNotFoundException | SQLException | CDKException | IOException e) {
			buf.append(e.toString());
		}
		return lmolecule;
	}

	@SuppressWarnings("unchecked")
	public static IAtomContainer performSingleTest(StringBuffer buf, String linear, 
			IAtomContainer molecule,
			ThermoSQLConnection connection) {
		try {
		SQLMetaAtomInfo sqlmetaatom = new SQLMetaAtomInfo(connection);
		HashSet<MetaAtomInfo> set;
			set = (HashSet<MetaAtomInfo>) sqlmetaatom.retrieveStructuresFromDatabase(linear);
		Iterator<MetaAtomInfo> iter = set.iterator();
        MetaAtomInfo info =  iter.next();
        
        SQLMetaAtomDefinitionFromMetaAtomInfo sqlmetadef = new SQLMetaAtomDefinitionFromMetaAtomInfo(connection);
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
		} catch (SQLException | ClassNotFoundException | CDKException | IOException e) {
			buf.append(e.toString());
		}

        return molecule;
	}

	public static ArrayList<String> listAllElements(StringBuffer buf, ThermoSQLConnection connection) {
		SQLMetaAtomInfo sqlmetaatom = new SQLMetaAtomInfo(connection);
		ArrayList<String> lst = new ArrayList<String>();
		try {
			@SuppressWarnings("unchecked")
			HashSet<MetaAtomInfo> set = (HashSet<MetaAtomInfo>) sqlmetaatom.retrieveMetaAtomTypesFromDatabase(linearAtomType);
			Iterator<MetaAtomInfo> iter = set.iterator();
			buf.append("Name        \t\t Substitute \t CMLStructure\n");
			while(iter.hasNext()) {
				StringBuffer line = new StringBuffer();
				MetaAtomInfo info = iter.next();
				line.append("LinearStructure." + info.getMetaAtomName());
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

	public static MetaAtomInfo getLinearAtom(String benson, StringBuffer buf, ThermoSQLConnection connection) {
		MetaAtomInfo info = null;
		try {
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
