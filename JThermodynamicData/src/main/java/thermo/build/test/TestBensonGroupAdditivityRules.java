package thermo.build.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.openscience.cdk.Atom;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

import jThergas.data.group.JThergasThermoStructureGroupPoint;
import thermo.data.benson.BensonGroupStructure;
import thermo.data.benson.BensonGroupStructuresFromMolecule;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonGroupStructures;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.StandardThergasBensonThermoType;
import thermo.data.benson.DB.SQLBensonThermodynamicBase;
import thermo.data.benson.DB.SQLSetOfBensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.thergas.BuildBensonThermodynamicFromThergas;
import thermo.data.structure.structure.SetOfMetaAtomsForSubstitution;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.DB.SQLMetaAtomDefinitionFromMetaAtomInfo;

public class TestBensonGroupAdditivityRules {
	static String bensonAtomType = "BensonAtom";
	static SetOfMetaAtomsForSubstitution metaAtomSubstitutions;
	static SQLMetaAtomDefinitionFromMetaAtomInfo sqlMetaAtom;

	public static SetOfBensonThermodynamicBase performWithFullSet(StringBuffer buf, IAtomContainer molecule,
			ThermoSQLConnection connection) {
        SetOfBensonThermodynamicBase thermodynamics = new SetOfBensonThermodynamicBase();
		try {
		    SetOfMetaAtomsForSubstitution metaAtomSubstitutions;
            sqlMetaAtom = new SQLMetaAtomDefinitionFromMetaAtomInfo(connection);
            metaAtomSubstitutions = sqlMetaAtom.createSubstitutionSets(bensonAtomType);
        	IAtomContainer substituted = metaAtomSubstitutions.substitute(molecule);
        	StructureAsCML cml = new StructureAsCML(substituted);
            buf.append("With Benson Atoms ----------------------------------------------------------\n");
            buf.append(cml.toString());
            buf.append("With Benson Atoms ----------------------------------------------------------\n");
        	
            BensonGroupStructuresFromMolecule bensonGroups = new BensonGroupStructuresFromMolecule();
            SetOfBensonGroupStructures bensonset = bensonGroups.deriveBensonGroupStructures(substituted);
            SetOfBensonThermodynamicBase bensonBase = new SetOfBensonThermodynamicBase();
            SQLSetOfBensonThermodynamicBase sqlthermodynamics = new SQLSetOfBensonThermodynamicBase(bensonBase, connection);
            sqlthermodynamics.setUpFromSetOfBensonGroupStructures(bensonset,thermodynamics);
            
            StandardThergasBensonThermoType standardbenson = new StandardThergasBensonThermoType();
            double[] temperatures = standardbenson.getTemperaturesAsDoubleValues();
            BensonThermodynamicBase combinedThermodynamics = thermodynamics.combineToOneBensonRule(temperatures);
            combinedThermodynamics.setID("Total");
            combinedThermodynamics.setReference("Sum Total");
       
        buf.append("Contributions ----------------------------------------------------------\n");
        buf.append(thermodynamics.toString());
        buf.append("Total ------------------------------------------------------------------\n");
        buf.append(combinedThermodynamics.toString());
        buf.append("------------------------------------------------------------------------\n");
		} catch(Exception e) {
			buf.append(e.toString());
		}
        return thermodynamics;
	}

	public static ArrayList<IAtom> performSingleTest(StringBuffer buf, String benson,
			IAtomContainer molecule, ThermoSQLConnection connection) {
		ArrayList<IAtom> atmlst = new ArrayList<IAtom>();
		try {
		sqlMetaAtom = new SQLMetaAtomDefinitionFromMetaAtomInfo(connection);
		metaAtomSubstitutions = sqlMetaAtom.createSubstitutionSets(bensonAtomType);
		metaAtomSubstitutions.substitute(molecule);
		buf.append("Molecule with Benson meta atoms ------------------------------\n");
		StructureAsCML cml = new StructureAsCML(molecule);
		buf.append(cml.toString());
		buf.append("Molecule with Benson meta atoms ------------------------------\n");
		BuildBensonThermodynamicFromThergas build = new BuildBensonThermodynamicFromThergas();
		JThergasThermoStructureGroupPoint thergasgrp = new JThergasThermoStructureGroupPoint();
		thergasgrp.setNancyLinearForm(benson);
		thergasgrp.parseGroupDescription(benson);
		System.out.println(thergasgrp.toString());
		BensonGroupStructure grp = build.buildBensonGroupStructure(thergasgrp);
		buf.append("Benson Group Structure ----------------------------------\n");
		buf.append(grp.toString() + "\n");
		buf.append("Benson Group Structure ----------------------------------\n");
		BensonGroupStructuresFromMolecule molgrp = new BensonGroupStructuresFromMolecule();
		atmlst = molgrp.atomsMatchingBensonGroupStructure(molecule, grp);
		buf.append("List of Atoms applied (" + atmlst.size() + ")--------------------------------------\n");
		Iterator<IAtom> iter = atmlst.iterator();
		while(iter.hasNext()) {
			Atom atm = (Atom) iter.next();
			buf.append(atm.getID() + ":\t " + atm.getSymbol());
			buf.append("\n");
		}
		buf.append("List of Atoms applied ------------------------------------------\n");
		} catch(Exception e) {
			e.printStackTrace();
			buf.append(e.toString());
		}
		return atmlst;
	}

	public static ArrayList<String> listAllElements(StringBuffer buf, ThermoSQLConnection connection) {
		SQLBensonThermodynamicBase sqlbase = new SQLBensonThermodynamicBase(connection);
		ArrayList<String> names = new ArrayList<String>();
		try {
			names = sqlbase.retrieveDatabaseNames();
		buf.append("Group Additivity Rules -----------------------------------------\n");
		Iterator<String> iter = names.iterator();
		while(iter.hasNext()) {
			buf.append(iter.next() + "\n");
		}
		buf.append("Group Additivity Rules -----------------------------------------\n");
		} catch (SQLException e) {
			buf.append(e.toString());
		}
		return names;
	}

	public static BensonThermodynamicBase getBensonAdditivtyRule(String rule, StringBuffer buf, ThermoSQLConnection connection) {
		SQLBensonThermodynamicBase sqlbase = new SQLBensonThermodynamicBase(connection);
		BensonThermodynamicBase thermo = null;
		HashSet<BensonThermodynamicBase> set;
		try {
			set = sqlbase.retrieveStructuresFromDatabase(rule);
		Iterator<BensonThermodynamicBase> iter = set.iterator();
		if(iter.hasNext()) {
			thermo = iter.next();
			buf.append("Group Additivity Rule -----------------------------------------\n");
			buf.append(thermo.toString() + "\n");
			buf.append("Group Additivity Rule -----------------------------------------\n");
		} else {
			buf.append("Benson Group Additivity Rule not found: " + rule + "\n");
		}
		} catch (SQLException e) {
			buf.append(e.toString());
		}
		return thermo;
	}
}
